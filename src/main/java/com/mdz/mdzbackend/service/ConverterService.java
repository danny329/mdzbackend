package com.mdz.mdzbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.mdz.mdzbackend.model.Mdz;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Double.parseDouble;

public class ConverterService {
    public InputStream ProtoToCsv(Mdz.Person person) throws JsonProcessingException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema.Builder schemaBuilder = CsvSchema.builder();
        schemaBuilder.setUseHeader(true);
        ArrayList<String> columns = new ArrayList<String>(Arrays.asList("Name", "Dob", "Salary", "Age"));
        ArrayList<String> data = new ArrayList<String>(Arrays.asList(person.getName(), person.getDob(), String.valueOf(person.getSalary()), String.valueOf(person.getAge())));
        schemaBuilder.addColumns(columns, CsvSchema.ColumnType.STRING);
        String source = mapper.writer(schemaBuilder.build()).writeValueAsString(data);
        InputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        return inputStream;
    }
    public Mdz.Person CsvToProto(InputStream inputStream) throws JDOMException, IOException {
        Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();
        String[] stringcontent = csvReader.readNext();
        System.out.println(stringcontent);
        Mdz.Person person = Mdz.Person.newBuilder().setName(stringcontent[0]).setDob(stringcontent[1]).setSalary(Double.parseDouble(stringcontent[2])).setAge(Integer.parseInt(stringcontent[3])).build();
        return person;
    }
    public InputStream ProtoToXml(Mdz.Person person) throws JDOMException, IOException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<Person><Name>");
        stringBuffer.append(person.getName());
        stringBuffer.append("</Name><Dob>");
        stringBuffer.append(person.getDob());
        stringBuffer.append("</Dob><Salary>");
        stringBuffer.append(String.valueOf(person.getSalary()));
        stringBuffer.append("</Salary><Age>");
        stringBuffer.append(String.valueOf(person.getAge()));
        stringBuffer.append("</Age></Person>");
        String xmldata = stringBuffer.toString();
        System.out.println(xmldata);
        InputStream is = new ByteArrayInputStream(xmldata.getBytes(StandardCharsets.UTF_8));
        SAXBuilder builder = new SAXBuilder();
        org.jdom2.Document document = builder.build(is);
        XMLOutputter outputter = new XMLOutputter();
        InputStream inputStream = new ByteArrayInputStream(outputter.outputString(document).getBytes(StandardCharsets.UTF_8));
        return inputStream;

    }
    public Mdz.Person XmlToProto(InputStream inputStream) throws JDOMException, IOException {
        SAXBuilder parser = new SAXBuilder();
        org.jdom2.Document document =  parser.build(inputStream);
        Element rootElement = document.getRootElement();
        String Name = rootElement.getChildText("Name");
        String Dob = rootElement.getChildText("Dob");
        double Salary = parseDouble(rootElement.getChildText("Salary"));
        int Age = Integer.parseInt(rootElement.getChildText("Age"));
        Mdz.Person person = Mdz.Person.newBuilder().setName(Name).setDob(Dob).setSalary(Salary).setAge(Age).build();
        return person;
    }
}
