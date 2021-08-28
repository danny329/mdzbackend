package com.mdz.mdzbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;


@SpringBootApplication
public class MdzbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdzbackendApplication.class, args);
	}

}
