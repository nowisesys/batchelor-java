# Java&trade; library for the Batchelor Web Service API.

### INTRODUCTION:

This library makes it an easy task to write client side applications that
integrates with Batchelor (the batch job queue manager) thru its web
service interfaces (currently the SOAP and RESTful service are supported).

The purpose is to present a simple to use API that allows end users to
communicate with the web service, without having to get dirty in all
details of encoding/decoding messages passed between your application
and the server. 

The target audience are people writing client side applications or
integrating Batchelor in business-to-business systems. Those who likes
to roll their own bindings can still do so.

### VERSION:

This is version is 1.0.6 of the library, released 2018-07-25. The major and 
minor numbers are based on the remote API it implements, while the revision 
gets incremented on each release. 
   
Works together with the batchelor 0.8.x releases. 
   
### DIRECTORIES:

The client directory contains the core web service library. The explorer 
directory contains a working sample GUI application built on Swing and SPI 
using the library that can be extended by implementing custom display classes
for viewing the browse tree nodes content.

### API:

The package se.uu.bmc.it.batchelor contains common classes and interfaces.
All service specific classes (REST and SOAP) is found in their own packages.

An simple example showing how to use the REST service class with FOA as 
the response encoding method:

```java
     import java.rmi.RemoteException;
     import java.net.URL;
     
     import se.uu.bmc.it.batchelor.*;
     import se.uu.bmc.it.batchelor.rest.*;
     
     class RestServiceClient {
         
         static {
             // 
             // Content handling must be initialized once and is global for the 
             // whole application and its entire lifetime.
             // 
             ContentHandlerFactory factory = ResponseDecoderFactory.getInstance();
             HttpURLConnection.setContentHandlerFactory(factory);
         }
         
         private BatchelorRestClient client;
         
         public RestServiceClient(URL url) {
             client = new RestWebService(url, ResponseEncoder.FOA);
         }
         
         // ... methods calling client.XXX()
         
     }
```
The kind of web service interfaces that can be used dependence on how
the server (that runs batchelor) is configured.

### DEPENDENCIES:

* FOA encoding/decoding requires the foa-java library available for download from http://it.bmc.uu.se/andlov/proj/foa-java (recommended).
* Oracle Java JDK >= 1.8 or equivalent.
   
### LICENSE:

The batchelor-java code is licensed under GPL with the classpath exception.
This means that you are free to use this library even in commercial 
applications (closed source), see the files COPYING and COPYING.CLASSPATH 
for details.

The license gives you permission to (freely) link to and use this library
in unmodified form. If you use source code from this library, then the you 
have to release your work as open source as well.
   
### COPYRIGHT:

Unless otherwise stated, this library is copyright (C) 2009-2018 by Anders
Lövgren and the Computing Department at BMC, Uppsala Biomedical Centre,
Uppsala University (with equally shared rights).

### ABOUT BATCHELOR:

See the project homepage: https://nowise.se/oss/batchelor/

### DISCLAIMER:

This library/client is for an older version of batchelor (0.8.x) using package 
namespaces belonging to BMC-IT (se.uu.bmc.it) and no longer maintained versions
of other packages distributed thru https://it.bmc.uu.se. 

While the core batchelor project has been fully migrated, this packages has 
not and are distributed for backward completness only!
