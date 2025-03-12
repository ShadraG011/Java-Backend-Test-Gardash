# Java Backend Test (Gardash)


## Описание
Веб-сервис для базового анализа текстовых документов предоставляющий основную аналитическую информацию через REST API.


## Структура проекта
```
JavaBackendTest/
├── src/main/java/ru/farpost/
│   ├── components/                      # Компоненты REST API
│   ├── controller/                      # Контроллеры REST API
│   ├── dto/                             # DTO
│   ├── exception/                       # Обработка ошибок
│   ├── model/                           # Сущности 
│   ├── repository/                      # Репозиторий для работы с базой данных
│   ├── service/                         # Бизнес-логика приложения
│   ├── utils/                           # Вспомогательные утилиты
│   ├── JavaBackendTestApplication.java  # Главный класс приложения
│
├── src/main/resources/
│   ├── application.properties           # Конфигурация Spring Boot для подключения к БД
│   ├── application.yml                  # Конфигурация Spring Boot для списка стоп-слов
│
├── site/apidocs/
│   ├──index.html                        # Документация по проекту
│                       
├── pom.xml                              # Зависимости Maven
└── README.md                            # Этот файл
```


## Требования
- Java: 21
- Maven: 3
- PostgreSQL: 17


## Запуск проекта
- ### Запуск в Docker
Проект можно запустить в Docker, так как были созданы `Dockerfile` и `docker-compose.yml` (для Spring-приложения и PostgreSQL). Используйте команды:

```sh
docker-compose build
```
```sh
docker-compose up
```
или
```sh
docker-compose up --build
```

- ### Запуск вручную
Также можно запустить приложение вручную с использованием `java -jar`:

```sh
java -jar ./target/JavaBackendTest-0.0.1-SNAPSHOT.jar
```

Но перед этим необходимо изменить `application.properties`, указав корректные настройки подключения к PostgreSQL и пересобрать проект в JAR-файл с помощью Maven:
```sh
mvn clean package 
```

## Примеры использования API 

- ### Создание документа: *POST* `/api/documents/`
**Request:** `http://localhost:8080/api/documents/`

**Response:**
```json
{
   "createStatus": "Документ успешно сохранен!"
}
```

- ### Получение документа по ID: *GET* `/api/documents/{id}`
**Request:** `http://localhost:8080/api/documents/1`

**Response:**
```json
{
   "id":1,
   "text":"Пример текста для документа."
}
```

- ### Получение нормализованного документа: *GET* `/api/documents/{id}/normalized`
**Request:** `http://localhost:8080/api/documents/1/normalized`

**Response:**
```json
{
   "id":1,
   "text":"пример текста документа"
}
```

- ### Получение статистики документа: *GET* `/api/documents/{id}/statistics`
**Request:** `http://localhost:8080/api/documents/1/statistics`

**Response:**
```json
{
   "word_count":3,
   "uniq_word_count":3,
   "avg_word_length":7,
   "sentences_count":1
}
```

- ### Получение статистики по всем документам: *GET* `/api/documents/statistics`
**Request:** `http://localhost:8080/api/documents/statistics`

**Response:**
```json
{
   "documents_count":2,
   "word_count":6,
   "uniq_word_count":4,
   "avg_word_length":7,
   "sentences_count":2
}
```

- ### Получение 10 (или меньше) часто встречающихся слов в документе: *GET* `/api/documents/{id}/top-words`
**Request:** `http://localhost:8080/api/documents/1/top-words`

**Response:**
```json
{ 
   "word1": 10, 
   "word2": 5, 
   "word3": 4,
   "word4": 3,
   "word5": 3,
   "word6": 2,
   "word7": 1,
   "word8": 1,
   "word9": 1,
   "word10": 1
}
```

- ### Поиск документа по ключевым словам: *GET* `/api/documents/search?word={word}`
**Request:** `http://localhost:8080/api/documents/search?word=пример`

**Response:**
```json
[1,2,4,5]
```

