# IoT Inventory - Demo RESTful HTTP API

This project shows a demo implementation of a simple IoT device and location inventory through 
an HTTP RESTful API.

The implementation is based on the Java Framework Dropwizard (https://www.dropwizard.io/en/latest/) and the available APIs 
are also exposed using swagger through the following local link: http://127.0.0.1:7070/swagger

APIs are exposed through a configurable port (7070) and accessible locally at: http://127.0.0.1:7070/api/iot/ 

## Modeled REST Resources

The IoT Inventory resources currently modeled are:

- Location (/location): Geographic location where one or multiple IoT devices can be deployed 
- Device (/device): A generic representation of an IoT device with basic information and customizable attributes. 
In the current implementation device's data are not handled and they are out of the scope of the demo inventory.