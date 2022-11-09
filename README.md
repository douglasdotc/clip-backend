# **clip-backend**

This repository containes the Spring Boot backend source code for release/springboot branch in [Clips](https://github.com/douglasdotc/Clips).

## **Table of Contents**
* [Installation guide (Windows/Mac)](#installation-guide)
    - [Installing IntelliJ](#install-intellij)
    - [Installing Docker](#install-docker)
    - [Installing MySQL](#install-mysql)
* [Running the Application](#run)

<a name="installation-guide"></a>
## **InstallationGuide (Windows/Mac)**

This project was developed with [MySQL](https://www.mysql.com/), [Docker](https://www.docker.com/) and [IntelliJ](https://www.jetbrains.com/idea/), IntelliJ IDEA is an integrated development environment written in Java for developing computer software written in Java, Kotlin, Groovy, and other JVM-based languages.

<a name="#install-intellij"></a>
### <u>Installing IntelliJ</u>

To download IntelliJ, head to [here](https://www.jetbrains.com/idea/download/#section=windows) and choose the version that is suitable for your operation system and follow the instructions to install.

<a name="#install-docker"></a>
### <u>Installing Docker</u>

To download Docker, head to [here](https://www.docker.com/) and choose the version that is suitable for your operation system and follow the instructions to install.


<a name="#install-mysql"></a>
### <u>Installing MySQL</u>
MySQL will be installed installed on the fly when [running the application](#run)



<a name="#run"></a>
## **Running the Application**

Docker is used to start the database server. With Docker Desktop running, start a terminal in `clip-backend` folder and use the command:

`docker compose up`

the terminal will look for `docker-compose.yaml` in the folder and start a MySQL server. Next, in IntelliJ IDE, we can press 'Run' or 'Debug' with 'BackendApplication.java' opened to start the backend server at `http://localhost:8080`. After that, we can use applications like Postman to send GET/POST/PUT/DELETE requests to the backend server.