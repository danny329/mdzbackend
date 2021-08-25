package com.mdz.mdzbackend.controller;

import com.mdz.mdzbackend.model.Person;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;



@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class PersonWebsocketController {
    //    create - post - websocket
    @MessageMapping("/persons")
    @SendTo("/topic/persons")
    public Person CreatePerson(Person person) throws Exception{
        return person;
    }
//    update - put - websocket
}
