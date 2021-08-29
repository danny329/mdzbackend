package com.mdz.mdzbackend.controller;

import com.mdz.mdzbackend.model.Mdz;
import com.mdz.mdzbackend.service.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.jdom2.JDOMException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "https://gentle-forest-0423d3a00.azurestaticapps.net/")
public class PersonRestController {
    private final ObjectStorageService objectStorageService;
    //    fetch all- get - http
    @GetMapping(value = "/persons", produces = "application/x-protobuf")
    public String getAllpersons(@RequestHeader("fileType") String fileType){
        return objectStorageService.listFiles(fileType);
    }
    //    fetch/filename - get - http
    @GetMapping(value = "/persons/{filename}", produces = "application/x-protobuf")
    public String getperson(@PathVariable String filename, @RequestHeader("fileType") String fileType) throws JDOMException, IOException {
        return objectStorageService.downloadFile(filename, fileType);
    }
}
