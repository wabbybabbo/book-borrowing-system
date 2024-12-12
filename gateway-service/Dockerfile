#基础镜像
FROM openjdk:17-jdk-alpine
#设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#拷贝jar包
COPY gateway-service-0.0.1-SNAPSHOT.jar /app.jar
#入口
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]