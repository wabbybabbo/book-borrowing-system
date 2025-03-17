# 基础镜像
FROM openjdk:17-jdk-alpine
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 安装依赖库和字体
RUN apk add --no-cache freetype fontconfig ttf-dejavu
# 设置字体目录
ENV JAVA_FONTS=/usr/share/fonts/truetype/dejavu
# 复制应用程序
COPY ./target/admin-service-0.0.1-SNAPSHOT.jar /app/my-app.jar
# 设置工作目录
WORKDIR /app
# 容器启动时要执行的命令（启动应用程序）
ENTRYPOINT ["java","-Dserver.port=8082","-Dspring.profiles.active=prod","-jar","my-app.jar"]