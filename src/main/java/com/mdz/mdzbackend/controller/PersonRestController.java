package com.mdz.mdzbackend.controller;

import com.mdz.mdzbackend.service.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PersonRestController {
    private final ObjectStorageService objectStorageService;
    //    fetch all- get - http
    @GetMapping("/persons/csvs")
    public List<String> getAllpersonscsvs(){
        return objectStorageService.listFiles();
    }
//    fetch/id - get - http
}
