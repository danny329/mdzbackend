package com.mdz.mdzbackend;
import com.mdz.mdzbackend.model.Mdz;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class MdzbackendApplicationTests {

	@Test
	public void createPerson() throws InterruptedException, ExecutionException, TimeoutException {
		final String url = "ws://localhost:8080/handler";
		CountDownLatch latch = new CountDownLatch(1);

		WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));
		stompClient.setMessageConverter(new StringMessageConverter());

		WebSocketHttpHeaders header = new WebSocketHttpHeaders();
		header.add("fileType","CSV");

		StompSession stompSession = stompClient.connect(url, header, new StompSessionHandlerAdapter() {}).get(10, SECONDS);
		stompSession.subscribe("/topics/persons", new StompFrameHandler() {

			@Override
			public Type getPayloadType(StompHeaders headers) {
				return String.class;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				System.out.println("Received message: " + payload);
				latch.countDown();
			}
		});
		Mdz.Person person = Mdz.Person.newBuilder().setName("testname").setDob("2001-02-03").setSalary(852145).setAge(25).build();
		String response = Base64.getEncoder().encodeToString(person.toByteArray());

		stompSession.send("/app/persons", response);
		if (!latch.await(10, TimeUnit.SECONDS)) {
			fail("Message not received");
		}
		else {
			assertTrue(Boolean.TRUE);
		}
	}
	@Test
	public void getALlpersons() throws IOException {
		final String url = "http://localhost:8080/api/persons";
		HttpClient client = HttpClients.custom().build();
		HttpUriRequest request = RequestBuilder.get()
				.setUri(url)
				.setHeader("fileType", "CSV")
				.build();
		client.execute(request);
		// When
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

		// Then
		MatcherAssert.assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(200));
	}

	@Test
	public void getperson() throws IOException {
		final String filename = "e0042cbf-08dc-11ec-8ef3-771f99cffc95.CSV";
		final String url = "http://localhost:8080/api/persons/"+filename;
		HttpClient client = HttpClients.custom().build();
		HttpUriRequest request = RequestBuilder.get()
				.setUri(url)
				.setHeader("fileType", "CSV")
				.build();
		client.execute(request);
		// When
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

		// Then
		MatcherAssert.assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(200));
	}
}
