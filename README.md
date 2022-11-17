# Messenger

> Steps to Run Application :
- We need Java version 11 installed and set as default version to run this application , Otherwise we can change Java version to JAVA 8 in pom.xml
- Application will be running on port 8080
- Postman collection is present under project repo itself. It can be found under directory called postman
- Run below commands in sequence from project directory 
  * mvn clean install 
  * docker-compose build 
  * docker-compose up 


> Assessment Details

       [POST] /messages  : post a new message

       [GET]  /messages  : paginated endpoint to list messages (we should be able to filter by topic and user_id)

       [GET]  /messages/{id} : retrieve message by id

       [DELETE] /message/{id} : delete message by id

 

> Requisites:

 - User cannot perform operations unless authenticated - use Spring Security (could be using basic authentication)

 - User Id NOT to be provided in the request body, it should be infered from authentication,

 - Delete is only possible if the message belongs to the user

 - A DB of choice should be used to persist the messages

 - REST API should be coded in Java using Spring Webflux and using a reactive driver for DB

 - Use JSON as Content-Type

 

- Before persisting the message to DB on <[POST] /messages>, messages need to be validated by calling a function of blocking nature.

  * The following function could be used to emulate the method call and should be included in the code:

``` 
   private Boolean blockingValidationCall(String message) {

     Thread.sleep(1000);

     return message == null || (message.length() % 2 == 0);

   }
```

 

- Error handling is up to the developer (follow standard REST practices on error codes)

- Under no circumstance I should get a 500 status code containing stacktrace of application!!

 

> Minimum message properties
```
   {

   user_id <string>  : uniquely identifies a user

   topic   <string>  : cannot be null

   content <string>  : message content

   created <timestamp> : created timestamp

   }
```
