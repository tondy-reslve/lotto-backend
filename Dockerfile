# 1. 使用與你本地環境完全一致嘅 Amazon Corretto 17 (Alpine 版本，極度輕量！)
FROM amazoncorretto:17-alpine

# 2. 設定容器內的工作目錄
WORKDIR /app

# 3. (安全最佳實踐) 建立一個非 root 用戶，防止 K8s 權限過大
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# 4. 將剛才 Maven build 出嚟嘅 jar 檔複製入容器，並改名做 app.jar
COPY target/*.jar app.jar

# 5. 聲明對外暴露嘅 Port (對應你 application.yml 嘅 1688)
EXPOSE 1688

# 6. 啟動指令，並且加入常用嘅 JVM 參數優化容器記憶體
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]