# Frontend — Интервалы активности

Минималистичный SPA на Vue 3 (Composition API, `<script setup>`) + Vite.
Бэкенд ожидается по адресу `http://localhost:8080/api/intervals`.

## Запуск

```bash
npm install
npm run dev
```

> Подробную инструкцию (порядок старта бэка/фронта, переменные окружения, прод-сборка) допишите здесь.

## Структура

```
src/
  App.vue                  — корневой компонент, связывает форму и таблицу
  main.js                  — точка входа Vite
  style.css                — глобальные стили
  components/
    ActionForm.vue         — форма добавления интервала
    ActionTable.vue        — таблица всех интервалов
  services/
    actionService.js       — обёртка над REST API бэкенда
```

## API, который дёргает фронт

- `GET  /api/intervals` — список интервалов
- `POST /api/intervals` — создать интервал
  - тело: `{ type: 'WORK' | 'BREAK', startTime: number, endTime: number, description: string }`
  - `409 Conflict` — интервал пересекается с существующим (текст в поле `message`)
