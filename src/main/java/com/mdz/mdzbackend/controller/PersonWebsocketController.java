package com.mdz.mdzbackend.controller;
import com.fasterxml.uuid.Generators;
import com.mdz.mdzbackend.model.Mdz;
import com.mdz.mdzbackend.service.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Base64;



@Controller
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "https://gentle-forest-0423d3a00.azurestaticapps.net/")
public class PersonWebsocketController {
    private final ObjectStorageService objectStorageService;
    //    create - post - websocket
    @MessageMapping("/persons")
    @SendTo("/topic/persons")
    public String CreatePerson(String bodyBase64String, @Header(name = "fileType") String fileType) throws Exception{
        String filename = Generators.timeBasedGenerator().generate().toString()+"."+fileType;
        byte[] bytearray = Base64.getDecoder().decode(bodyBase64String);
        Mdz.Person person = Mdz.Person.parseFrom(bytearray);
        Boolean isSaved = objectStorageService.storeFile(filename, person, fileType);
        Mdz.ResponsePerson responsePerson = Mdz.ResponsePerson.newBuilder()
                .setId(filename)
                .setName(person.getName())
                .setAge(person.getAge())
                .setSalary(person.getSalary())
                .setDob(person.getDob())
                .build();
        String response = Base64.getEncoder().encodeToString(responsePerson.toByteArray());
        return response;
    }

    //    update - put - websocket
    @MessageMapping("/persons/{filename}")
    @SendTo("/topic/person")
    public String UpdatePerson(@DestinationVariable String filename, String bodyBase64String, @Header(name = "fileType") String fileType) throws Exception{
        byte[] bytearray = Base64.getDecoder().decode(bodyBase64String);
        Mdz.Person person = Mdz.Person.parseFrom(bytearray);
        System.out.println(filename);
        System.out.println(bodyBase64String);
        System.out.println(fileType);
        Boolean isDeleted = objectStorageService.deleteFile(filename, fileType);
        Boolean isSaved = objectStorageService.storeFile(filename, person, fileType);

        Mdz.ResponsePerson responsePerson = Mdz.ResponsePerson.newBuilder()
                .setId(filename)
                .setName(person.getName())
                .setAge(person.getAge())
                .setSalary(person.getSalary())
                .setDob(person.getDob())
                .build();
        String response = Base64.getEncoder().encodeToString(responsePerson.toByteArray());
        return response;
    }

}
