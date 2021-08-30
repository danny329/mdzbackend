package com.mdz.mdzbackend.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;

import com.fasterxml.uuid.Generators;
import com.mdz.mdzbackend.model.Mdz;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;



@Service
@Slf4j
public class ObjectStorageService {
    @Value("${azure.storage.container-csv}")
    private String csvfolder;
    @Value("${azure.storage.container-xml}")
    private String xmlfolder;
    @Value("${azure.storage.connectionstring}")
    private String connectionstring;


    //    get all files
    public String listFiles(String type) throws URISyntaxException, InvalidKeyException, StorageException {
        String containerName;
        if(type.equals("CSV")) {
            containerName = csvfolder;
        }
        else {
            containerName = xmlfolder;
        }
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionstring);
        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        // Retrieve reference to a previously created container.
        CloudBlobContainer container = blobClient.getContainerReference(containerName);

        Mdz.ListOfFiles.Builder listOfFiles = Mdz.ListOfFiles.newBuilder();

        List<String> list = new ArrayList<String>();
        for(ListBlobItem blobItem: container.listBlobs()){
            list.add(blobItem.getUri().toString());
        }
        Mdz.ListOfFiles listOfFile = listOfFiles.addAllFilename(list).build();
        String response = Base64.getEncoder().encodeToString(listOfFile.toByteArray());

        return response;
    }
    // upload file
    public Boolean storeFile(String filename, Mdz.Person person, String type) throws IOException, JDOMException, URISyntaxException, InvalidKeyException, StorageException {
        String containerName;
        if(type.equals("CSV")) {
            containerName = csvfolder;
        }
        else {
            containerName = xmlfolder;
        }
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionstring);
        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        // Retrieve reference to a previously created container.
        CloudBlobContainer container = blobClient.getContainerReference(containerName);
        CloudBlockBlob blob = container.getBlockBlobReference(filename);
        ConverterService converterService = new ConverterService();
        InputStream inputStream = converterService.ProtoToCsv(person);
        blob.upload(inputStream, inputStream.available());
        return Boolean.TRUE;
    }
    // download file
    public String downloadFile(String filename, String type) throws JDOMException, IOException, URISyntaxException, InvalidKeyException, StorageException {
        Mdz.Person person;
        String containerName;
        if(type.equals("CSV")) {
            containerName = csvfolder;
        }
        else {
            containerName = xmlfolder;
        }
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionstring);
        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        // Retrieve reference to a previously created container.
        CloudBlobContainer container = blobClient.getContainerReference(containerName);
        // Retrieve reference to a blob named "myimage.jpg".
        CloudBlockBlob blob = container.getBlockBlobReference(filename);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        blob.download(bos);
        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
        ConverterService converterService = new ConverterService();

        if(type.equals("CSV")) {
            person = converterService.CsvToProto(inputStream);
        }
        else {
            person = converterService.XmlToProto(inputStream);
        }
        String response = Base64.getEncoder().encodeToString(person.toByteArray());

        return response;
    }
    // delete file
    public Boolean deleteFile(String filename, String type) throws URISyntaxException, InvalidKeyException, StorageException {
        String containerName;
        if(type.equals("CSV")) {
            containerName = csvfolder;
        }
        else {
            containerName = xmlfolder;
        }
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionstring);
        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        // Retrieve reference to a previously created container.
        CloudBlobContainer container = blobClient.getContainerReference(containerName);
        // Retrieve reference to a blob named "myimage.jpg".
        CloudBlockBlob blob = container.getBlockBlobReference(filename);

        // Delete the blob.
        blob.deleteIfExists();
        return Boolean.TRUE;
    }

}
