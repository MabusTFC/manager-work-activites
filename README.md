# Manager Work Activities

Веб-приложение для управления интервалами рабочих активностей в течение суток.
Каждая активность — временной интервал `[startTime, endTime]` в секундах
(`0..86400`) с типом `WORK` (Работа) или `BREAK` (Перерыв).
Пересекающиеся интервалы сохранять нельзя.

## Стек

- **Бэкенд:** Java 21, Spring Boot 3.2, Spring Data JPA, Hibernate 6, Lombok, SQLite, springdoc-openapi (Swagger)
- **Фронтенд:** Vue 3 (Composition API, `<script setup>`), Vite
- **Сборка:** Maven (через `mvnw`) и npm

## Структура

```
manager-work-activites/
├── pom.xml, mvnw, mvnw.cmd      Maven wrapper
├── src/main/java/...            Исходники бэкенда
├── src/main/resources/
│   └── application.properties   Конфиг (SQLite, JPA)
├── data/                        Папка для файла БД (создаётся вручную)
│   └── intervals.db             SQLite-файл (создаёт приложение)
└── frontend/                    Клиентская часть (Vue 3 + Vite)
    ├── package.json
    └── src/
```

---

## 1. Запуск бэкенда

### Требования

- Java 21 (или JDK 17+)
- Maven можно не ставить — используется `mvnw`

### Шаги

1. Перейти в корень проекта:

   ```bash
   cd manager-work-activites
   ```

2. Создать папку для файла SQLite (драйвер сам её не создаёт):

   - Windows (PowerShell):
     ```powershell
     New-Item -ItemType Directory -Path data -Force
     ```
   - macOS / Linux:
     ```bash
     mkdir -p data
     ```

3. Запустить приложение:

   - Windows:
     ```powershell
     .\mvnw.cmd spring-boot:run
     ```
   - macOS / Linux:
     ```bash
     ./mvnw spring-boot:run
     ```

4. Проверить:

   - API: <http://localhost:8080/api/intervals> → `[]`
   - Swagger: <http://localhost:8080/swagger-ui.html>

После первого запуска появится файл `data/intervals.db`.
Данные сохраняются между перезапусками.

### Сборка JAR

```bash
./mvnw clean package
java -jar target/manager-work-activites-0.0.1-SNAPSHOT.jar
```

---

## 2. Запуск фронтенда

### Требования

- Node.js 18+ и npm

### Шаги

```bash
cd frontend
npm install
npm run dev
```

Фронт стартует на <http://localhost:5173>.

Vite настроен с прокси: запросы `/api/**` с фронта уходят
на `http://localhost:8080`. **Бэкенд должен быть запущен**.


### Модель запроса

```json
{
  "type": "WORK",
  "startTime": 0,
  "endTime": 3600,
  "description": "Утренний блок"
}
```

| Поле          | Тип               | Ограничения                                  |
|---------------|-------------------|----------------------------------------------|
| `type`        | `WORK` или `BREAK`| Обязательно                                  |
| `startTime`   | integer           | `0..86400`, обязательно                      |
| `endTime`     | integer           | `0..86400`, обязательно, `> startTime`       |
| `description` | string            | Непустая строка, обязательно                 |

### Модель ответа

```json
{
  "id": 1,
  "type": "WORK",
  "startTime": 0,
  "endTime": 3600,
  "description": "Утренний блок",
  "createdAt": "2026-05-26T18:30:00"
}
```

### Модель ошибки

```json
{
  "status": 409,
  "message": "Интервал пересекается с существующим(и): [0 - 3600 сек (ID: 1)] ",
  "path": "/api/intervals",
  "timestamp": "2026-05-26T18:31:00"
}
```

### Эндпоинты

| Метод  | URL                   | Назначение                |
|--------|-----------------------|---------------------------|
| GET    | `/api/intervals`      | Получить все интервалы    |
| GET    | `/api/intervals/{id}` | Получить один по `id`     |
| POST   | `/api/intervals`      | Создать интервал          |
| PUT    | `/api/intervals/{id}` | Обновить интервал         |
| DELETE | `/api/intervals/{id}` | Удалить интервал          |

### Коды ответов

| Код | Когда                                                       |
|-----|-------------------------------------------------------------|
| 200 | Успех (GET / PUT)                                           |
| 201 | Создано (POST)                                              |
| 204 | Удалено (DELETE)                                            |
| 400 | Невалидные данные (диапазон, `start >= end`, пустые поля)   |
| 404 | Интервал с заданным `id` не найден                          |
| 409 | Интервал пересекается с уже существующим                    |

---

## 3. Примеры cURL

### Получить все интервалы

```bash
curl http://localhost:8080/api/intervals
```

### Получить один интервал

```bash
curl http://localhost:8080/api/intervals/1
```

### Создать интервал

```bash
curl -X POST http://localhost:8080/api/intervals \
  -H "Content-Type: application/json" \
  -d '{
    "type": "WORK",
    "startTime": 0,
    "endTime": 3600,
    "description": "Первый блок"
  }'
```

### Обновить интервал

```bash
curl -X PUT http://localhost:8080/api/intervals/1 \
  -H "Content-Type: application/json" \
  -d '{
    "type": "BREAK",
    "startTime": 0,
    "endTime": 1800,
    "description": "Завтрак"
  }'
```

### Удалить интервал

```bash
curl -X DELETE http://localhost:8080/api/intervals/1
```

### Пример пересечения

Сначала добавляем `[0, 3600]`:

```bash
curl -X POST http://localhost:8080/api/intervals \
  -H "Content-Type: application/json" \
  -d '{"type":"WORK","startTime":0,"endTime":3600,"description":"A"}'
```

Затем `[1800, 5400]` — пересекается:

```bash
curl -X POST http://localhost:8080/api/intervals \
  -H "Content-Type: application/json" \
  -d '{"type":"BREAK","startTime":1800,"endTime":5400,"description":"B"}'
```

Ответ:

```json
{
  "status": 409,
  "message": "Интервал пересекается с существующим(и): [0 - 3600 сек (ID: 1)] ",
  "path": "/api/intervals",
  "timestamp": "2026-05-26T18:31:00"
}
```

---

## 4. Валидация и бизнес-логика

- `startTime`, `endTime` — целые числа в диапазоне `0..86400`
- `startTime < endTime`
- `type` обязателен (`WORK` или `BREAK`)
- `description` обязателен и непустой
- Перед сохранением проверяется отсутствие пересечений по правилу
  `a.startTime < b.endTime AND a.endTime > b.startTime`.
  При пересечении возвращается `409 Conflict`, запись **не** сохраняется.

---

## 5. Конфигурация

`src/main/resources/application.properties`:

```properties
spring.application.name=manager-work-activites

spring.datasource.url=jdbc:sqlite:./data/intervals.db
spring.datasource.driver-class-name=org.sqlite.JDBC

spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Таблица `actions` создаётся автоматически при первом старте.

---
