CountryService - Мультипротокольный микросервис для управления странами
📋 Описание
Микросервис для управления информацией о странах с поддержкой четырёх протоколов взаимодействия:

REST API - традиционный HTTP API

GraphQL - гибкий API с возможностью выбирать поля

gRPC - высокопроизводительный RPC с поддержкой streaming

SOAP - XML веб-сервисы

🛠 Технологический стек
Java 21

Spring Boot 3.4.1

PostgreSQL 15

Gradle

gRPC, GraphQL, SOAP

🚀 Запуск проекта
Предварительные требования
Java 21 (установка: https://adoptium.net/)

Docker и Docker Compose (https://www.docker.com/products/docker-desktop)

Gradle

# Запустить PostgreSQL контейнер
docker rm postgres-country

docker run --name postgres-country \
-e POSTGRES_DB=countrydb \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=secret \
-p 5432:5432 \
-d postgres:15

Запустить существующий остановленный контейнер:
bash
docker start postgres-country
3. Остановить работающий контейнер:
   bash
   docker stop postgres-country
4. Перезапустить контейнер:
   bash
   docker restart postgres-country
5. Посмотреть логи контейнера:
   bash
   docker logs postgres-country
6. Подключиться к контейнеру для проверки:
   bash
# Подключиться к PostgreSQL внутри контейнера
docker exec -it postgres-country psql -U postgres -d countrydb

# Выполнить SQL запрос прямо из командной строки
docker exec -it postgres-country psql -U postgres -d countrydb -c "SELECT * FROM country;"

# Проверить, что контейнер запущен
docker ps

# Проверить подключение к БД
docker exec -it postgres-country psql -U postgres -d countrydb -c "\l"
3. Сборка проекта
   bash
# Очистка и сборка
./gradlew clean build

# Если нужно пропустить тесты
./gradlew clean build -x test
4. Запуск приложения
   bash
# Запуск через Gradle
./gradlew bootRun

# Или запуск собранного JAR файла
java -jar build/libs/countryService-0.0.1-SNAPSHOT.jar
Приложение запустится на:

REST/GraphQL/SOAP: http://localhost:8383

gRPC: localhost:9090

5. Остановка
   bash
# Остановка приложения: Ctrl+C в терминале

# Остановка контейнера PostgreSQL
docker stop postgres-country

# Полное удаление контейнера (если нужно)
docker rm postgres-country
📡 Тестирование API
REST API
bash
# Получить все страны
curl http://localhost:8383/api/countries/all

# Получить страну по коду
curl http://localhost:8383/api/countries/RU

# Создать новую страну
curl -X POST http://localhost:8383/api/countries \
-H "Content-Type: application/json" \
-d '{"name":"Россия","code":"RU","coordinates":"60.0° N, 90.0° E"}'

# Обновить страну
curl -X PATCH http://localhost:8383/api/countries/RU \
-H "Content-Type: application/json" \
-d '{"name":"Российская Федерация"}'

# Удалить страну
curl -X DELETE http://localhost:8383/api/countries/RU
GraphQL API
bash
# GraphiQL интерфейс: http://localhost:8383/graphiql

# Запрос через curl
curl -X POST http://localhost:8383/graphql \
-H "Content-Type: application/json" \
-d '{"query": "{ listCountries { name code coordinates } }"}'

# Мутация для создания страны
curl -X POST http://localhost:8383/graphql \
-H "Content-Type: application/json" \
-d '{"query": "mutation { createCountry(input: {name: \"Германия\", code: \"DE\", coordinates: \"51.1657° N, 10.4515° E\"}) { id name code } }"}'
SOAP API
bash
# Получить WSDL
curl http://localhost:8383/ws/countries.wsdl

# SOAP запрос для получения всех стран
curl -X POST http://localhost:8383/ws \
-H "Content-Type: text/xml" \
-d '<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
xmlns:gs="http://qa.guru/country-service">
<soapenv:Header/>
<soapenv:Body>
<gs:getAllCountriesRequest/>
</soapenv:Body>
</soapenv:Envelope>'
gRPC API
Для тестирования gRPC используйте grpcurl:

bash
# Установка grpcurl (https://github.com/fullstorydev/grpcurl)
# Скачайте бинарник или установите через包 менеджер

# Получить список методов
grpcurl -plaintext localhost:9090 list

# Получить все страны (server-streaming)
grpcurl -plaintext localhost:9090 guru.qa.grpc.countrycatalog.CountrycatalogService/ListCountries

# Создать страну
grpcurl -plaintext -d '{"name":"Франция","code":"FR","coordinates":"46.2276° N, 2.2137° E"}' \
localhost:9090 guru.qa.grpc.countrycatalog.CountrycatalogService/AddCountry

# Client-streaming (через файл с несколькими записями)
# Создайте файл countries.json:
# {"name": "Италия", "code": "IT", "coordinates": "41.8719° N, 12.5674° E"}
# {"name": "Испания", "code": "ES", "coordinates": "40.4637° N, 3.7492° W"}

grpcurl -plaintext -d @ localhost:9090 \
guru.qa.grpc.countrycatalog.CountrycatalogService/AddCountriesStream < countries.json
📁 Структура проекта
text
countryService/
├── src/
│   ├── main/
│   │   ├── java/guru/qa/countryservice/
│   │   │   ├── controller/        # REST и GraphQL контроллеры
│   │   │   ├── service/           # Бизнес-логика и gRPC сервис
│   │   │   ├── repository/        # JPA репозитории
│   │   │   ├── entity/            # JPA сущности
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   ├── config/            # Конфигурации
│   │   │   └── soap/              # SOAP endpoint
│   │   └── resources/
│   │       ├── application.yaml    # Конфигурация приложения
│   │       ├── db/migration/       # Flyway миграции
│   │       ├── graphql/            # GraphQL схема
│   │       ├── proto/              # gRPC proto файлы
│   │       └── xsd/                # SOAP XSD схема
│   └── test/                       # Тесты
├── build.gradle                     # Сборочный файл
├── settings.gradle                  # Настройки Gradle
└── README.md                        # Этот файл