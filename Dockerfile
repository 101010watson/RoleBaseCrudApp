FROM amazoncorretto:25-jdk

ADD target/role-base.jar role-base.jar

ENTRYPOINT ["java", "-jar", "/role-base.jar"]