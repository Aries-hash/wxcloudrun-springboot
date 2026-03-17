# ===== 构建阶段 =====
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

# 【优化关键】先只拷贝 pom.xml 和 settings.xml，单独下载依赖
# 这样只要依赖不变，Docker 会缓存这一层，后续构建直接跳过依赖下载
COPY settings.xml pom.xml /app/
RUN mvn -s /app/settings.xml -f /app/pom.xml dependency:go-offline -B

# 再拷贝源代码（这一层经常变化，但依赖层已缓存）
COPY src /app/src

# 编译打包（跳过测试 + 离线模式，因为依赖已下载完毕）
RUN mvn -s /app/settings.xml -f /app/pom.xml clean package -DskipTests -o

# ===== 运行阶段 =====
FROM eclipse-temurin:25-jre-alpine

# 设置上海时区
ENV TZ=Asia/Shanghai

# 安装证书 + 时区数据（合并为一条 RUN 减少层数）
RUN apk add --no-cache ca-certificates tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata

WORKDIR /app

# 只拷贝最终 jar 包
COPY --from=build /app/target/*.jar app.jar

EXPOSE 80

# 优化 JVM 启动参数
CMD ["java", \
     "-XX:+UseContainerSupport", \
     "-XX:MaxRAMPercentage=75.0", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-jar", "/app/app.jar"]
