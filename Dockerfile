# Используем базовый образ с Java
FROM adoptopenjdk:17-jre-hotspot as builder

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем исходный код проекта в контейнер
COPY . .

# Собираем приложение
RUN ./mvnw clean install -DskipTests

# Создаем финальный образ на основе образа Java
FROM adoptopenjdk:17-jre-hotspot

# Устанавливаем MySQL
RUN apt-get update && apt-get install -y mysql-server

# Копируем JAR-файл вашего приложения в контейнер
COPY --from=builder /app/target/file-api-0.0.1-SNAPSHOT.jar .

# Определяем порт, на котором будет работать приложение
EXPOSE 8080

# Запускаем приложение при старте контейнера
CMD ["java", "-jar", "file-api-0.0.1-SNAPSHOT.jar"]
