package com.mdz.mdzbackend.controller;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.mdz.mdzbackend.model.Mdz;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.InputStream;
import java.util.Base64;

import static com.google.protobuf.Internal.toByteArray;
import static org.springframework.util.Base64Utils.decodeFromString;


@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class PersonWebsocketController {
    //    create - post - websocket
    @MessageMapping("/persons")
    @SendTo("/topic/persons")
    public String CreatePerson(String person) throws Exception{

        byte[] bytear = Base64.getDecoder().decode(person);
        Mdz.Person type = Mdz.Person.parseFrom(bytear);

//        Mdz.Person reqbody = Mdz.Person.newBuilder().setName("dan").setAge(18).setSalary(8523).setDob("2020-03-25").build();
//        Base64.getEncoder().encodeToString(originalInput.getBytes());
        return person;
    }

//    update - put - websocket
}
