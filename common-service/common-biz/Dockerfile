# 基础镜像
FROM openjdk:17-jdk-alpine
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 安装该应用程序所需要的依赖库 libfreetype （需要它来支持生成图形验证码）
RUN apk add --no-cache freetype
# 复制应用程序
COPY ./target/common-biz-0.0.1-SNAPSHOT.jar /app/my-app.jar
# 设置工作目录
WORKDIR /app
# 容器启动时要执行的命令（启动应用程序）
ENTRYPOINT ["java","-Dserver.port=8081","-Dspring.profiles.active=prod","-jar","my-app.jar"]