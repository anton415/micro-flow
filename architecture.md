# Архитектура проекта: Микросервисная система

## Обзор
Система состоит из двух микросервисов, развернутых в Kubernetes:
- **Request Service**: Принимает заявки от пользователей.
- **Notification Service**: Получает уведомления от Request Service.

Взаимодействие через HTTP. Внешний доступ через Ingress.

## API спецификация

### Request Service
- **Endpoint**: `POST /requests`
- **Тело запроса** (JSON):
  ```json
  {
    "description": "string",
    "user": "string"  // опционально
  }
  ```
- **Ответ** (JSON):
  ```json
  {
    "id": "uuid",
    "status": "registered",
    "timestamp": "ISO 8601"
  }
  ```

### Notification Service
- **Endpoint**: `POST /notifications`
- **Тело запроса** (JSON):
  ```json
  {
    "requestId": "uuid",
    "message": "string",
    "timestamp": "ISO 8601"
  }
  ```
- **Ответ**: `200 OK` с подтверждением.

## Структура данных

### Заявка (Request)
```json
{
  "id": "uuid",
  "description": "string",
  "user": "string",
  "status": "registered|processed",
  "createdAt": "ISO 8601",
  "updatedAt": "ISO 8601"
}
```

### Нотификация (Notification)
```json
{
  "id": "uuid",
  "requestId": "uuid",
  "message": "Request registered: {description}",
  "createdAt": "ISO 8601"
}
```

## Взаимодействие сервисов
1. Пользователь отправляет POST /requests в Request Service через Ingress.
2. Request Service сохраняет заявку (in-memory) и генерирует ID.
3. Request Service отправляет POST /notifications в Notification Service.
4. Notification Service логирует нотификацию.

## Технологии
- Язык: Node.js (Express.js)
- Хранение: In-memory (для демо)
- Коммуникация: HTTP (axios)
- Контейнеризация: Docker
- Оркестрация: Kubernetes (Minikube)

## Диаграмма архитектуры
```
[User] → [Ingress] → [Request Service] → [Notification Service]
                    ↓
               [Kubernetes Cluster]
```