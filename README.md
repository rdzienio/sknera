This app uses PostgreSQL database, but you can use H2 or others.
To make it work you have to add application.properties file in the resources directory.

Example "application.properties" file:

server.port=3000

spring.messages.basename=error-messages

#spring.datasource.driverClassName=org.h2.Driver
#spring.datasource.url=jdbc:h2:mem:db_name
#spring.datasource.url=jdbc:h2:file:D://db_name
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/db_name
spring.datasource.username=#your_username
spring.datasource.password=#password



#spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.properties.hibernate.dialect =org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

app.upload.dir=resources/static/img

spring.servlet.multipart.max-file-size=128KB
spring.servlet.multipart.max-request-size=128KB
spring.servlet.multipart.enabled=true

spring.task.scheduling.pool.size=5

#mail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=#your_email
spring.mail.password=#password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true


