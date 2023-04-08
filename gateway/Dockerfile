FROM amazoncorretto:11-alpine-jdk
RUN apk add tzdata && cp /usr/share/zoneinfo/Europe/Moscow /etc/localtime && echo "Europe/Moscow" >  /etc/timezone && date && apk del tzdata
COPY target/*.jar gateway.jar
ENTRYPOINT ["java","-jar","/gateway.jar"]