# minesweeper-backend
Тестовое задание Studio TG: бэкенд API для игры «Сапёр».
Фронтенд и спецификация: https://minesweeper-test.studiotg.ru/

## Технологический стек
Java 17, Spring Boot 3.x, Maven, Spring Data JPA/Hibernate, PostgreSQL 13 + JSONB, Docker + Docker Compose, JUnit 5 + Testcontainers

## Быстрый старт
```bash
docker compose up --build
```
После запуска приложение будет принимать запросы по адресу: **http://localhost:8080/api**

### Подключение фронтенда
1. Открыть фронтенд: https://minesweeper-test.studiotg.ru/
2. В поле «Путь к API» указать: `http://localhost:8080/api`
3. Начать новую игру — запросы пойдут на локальный бэкенд

## Требования
- Docker и Docker Compose
- Java 17 (для локальной разработки)

## Лицензия
Apache License 2.0 — см. файл LICENSE