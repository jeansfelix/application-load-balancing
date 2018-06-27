# How to execute app

## Prerequisites
You need install: [Maven](https://maven.apache.org/) - Dependency Management

A install guide can be found here [getting-started](https://maven.apache.org/guides/getting-started/index.html).

## Build and run
Copy the certificate file /alb.jks to /etc/keystore/alb.jks

To create a executable jar run: 
```
mvn clean compile assembly:single
```
To start the proxy run:
```
java -jar target/alb.jar -p 8443 -sc ${server_to_chrome} -so ${server_to_other}
```

