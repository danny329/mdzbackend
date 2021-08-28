package com.mdz.mdzbackend.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.uuid.Generators;
import com.mdz.mdzbackend.model.Mdz;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;



@Service
@Slf4j
public class ObjectStorageService {
    @Value("${azure.storage.container-csv}")
    private String csvfolder;
    @Value("${azure.storage.container-xml}")
    private String xmlfolder;
    @Value("${azure.storage.connectionstring}")
    private String connectionstring;

    private BlobContainerClient csvContainer(){
        BlobServiceClient serviceClient = new BlobServiceClientBuilder().connectionString(connectionstring).buildClient();
        BlobContainerClient container = serviceClient.getBlobContainerClient(csvfolder);
        return container;
    }
    private BlobContainerClient xmlContainer(){
        BlobServiceClient serviceClient = new BlobServiceClientBuilder().connectionString(connectionstring).buildClient();
        BlobContainerClient container = serviceClient.getBlobContainerClient(xmlfolder);
        return container;
    }
    //    get all files
    public List<String> listFiles(){
        BlobContainerClient containerCsv = csvContainer();
        List<String> list = new ArrayList<String>();
        for(BlobItem blobItem: containerCsv.listBlobs()){
            list.add(blobItem.getName());
        }
        return list;
    }
    // upload file
    public String storeFile(Mdz.Person person, String type) throws IOException {
        String filename = Generators.timeBasedGenerator().generate().toString()+"."+type;
        if(type.equals("CSV")){
            BlobClient client = csvContainer().getBlobClient(filename);
            if(client.exists()){
                return "Failed";
            }
            else {
                CsvMapper mapper = new CsvMapper();
                CsvSchema.Builder schemaBuilder = CsvSchema.builder();
                schemaBuilder.setUseHeader(true);
                ArrayList<String> columns = new ArrayList<String>(Arrays.asList("Name", "Dob", "Salary", "Age"));
                ArrayList<String> data = new ArrayList<String>(Arrays.asList(person.getName(), person.getDob(), String.valueOf(person.getSalary()), String.valueOf(person.getAge())));
                schemaBuilder.addColumns(columns, CsvSchema.ColumnType.STRING);
                String source = mapper.writer(schemaBuilder.build()).writeValueAsString(data);
                InputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
                client.upload(inputStream, inputStream.available());
            }
        }
        if(type.equals("XML")){
            BlobClient client = xmlContainer().getBlobClient(filename);
            if(client.exists()){
                return "Failed";
            }else {
                client.upload(content,length);
            }
        }
        return filename;
    }
    // download file
    public ByteArrayOutputStream downloadFile(String filename, String type){
        BlobContainerClient container = csvContainer();
        BlobClient blobClient = container.getBlobClient(filename);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        blobClient.download(bos);
        return bos;
    }
    // delete file
    public Boolean deleteFile(String filename, String type){
        BlobContainerClient container = csvContainer();
        BlobClient blobClient = container.getBlobClient(filename);
        blobClient.delete();
        return Boolean.TRUE;
    }
}
