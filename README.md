# application-load-balancing

To create a executable jar run: 
  mvn clean compile assembly:single

To start the proxy run:
  java -jar target/alb.jar -p 8443 -sc ${server_to_chrome} -so ${server_to_other}

