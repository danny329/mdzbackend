# Usage Guide

This application has controller / routes for websocket as well as http/s network communications.

###For http/s reference, please consider the following sections:

Swagger documentation has been provided for http/s based communication.
it introduces two routes and both can be referred in detail the below link.
* [Swagger Documentation](http://localhost:8080/swagger-ui.html)

### For Websocket reference, please consider the following sections:

Since Swagger doesn't support websocket routes documentation. Here are the details on how to use webSocket routes.
There are two routes open.
####Creating Person

* [Create Route](ws://localhost:8080/app/persons) `/app/persons`\
    `* header required -> 'fileType' : values [CSV, XML]`\
    `request body should contain a base64 encrypted protobuf `\
    \
    The body definition is provided in the proto file.
    

* [Update Route](ws://localhost:8080/app/persons/<filename>) `/app/persons/<filename>`\
  `* header required -> 'fileType' : values [CSV, XML]`\
  `request body should contain a base64 encrypted protobuf `\
  


