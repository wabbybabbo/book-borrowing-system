#基础镜像
FROM openjdk:17-jdk-alpine
#设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#拷贝jar包
COPY ./target/client-service-0.0.1-SNAPSHOT.jar /app.jar
#容器启动时要执行的命令
ENTRYPOINT ["java","-Dserver.port=8083","-Dspring.profiles.active=prod","-jar","/app.jar"]