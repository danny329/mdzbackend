package com.mdz.mdzbackend.controller;

import com.mdz.mdzbackend.model.Mdz;
import com.mdz.mdzbackend.service.ObjectStorageService;
import com.microsoft.azure.storage.StorageException;
import lombok.RequiredArgsConstructor;
import org.jdom2.JDOMException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "https://nice-pond-0fa3a9900.azurestaticapps.net")
public class PersonRestController {
    private final ObjectStorageService objectStorageService;
    //    fetch all- get - http
    @GetMapping(value = "/persons", produces = "application/x-protobuf")
    public String getAllpersons(@RequestHeader("fileType") String fileType) throws InvalidKeyException, StorageException, URISyntaxException {
        return objectStorageService.listFiles(fileType);
    }
    //    fetch/filename - get - http
    @GetMapping(value = "/persons/{filename}", produces = "application/x-protobuf")
    public String getperson(@PathVariable String filename, @RequestHeader("fileType") String fileType) throws JDOMException, IOException, InvalidKeyException, StorageException, URISyntaxException {
        return objectStorageService.downloadFile(filename, fileType);
    }
}
