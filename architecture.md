# Минимальная архитектура проекта

## Идея

Нужна самая простая микросервисная схема, которая показывает 3 вещи:

1. всё живёт внутри Kubernetes
2. внешний трафик идёт через Ingress
3. один сервис вызывает другой по DNS-имени Kubernetes

## Состав системы

### `request-service`
- публичный сервис
- принимает `POST /requests`
- генерирует `requestId`
- вызывает `notification-service`
- возвращает клиенту успешный ответ

### `notification-service`
- внутренний сервис
- принимает `POST /notifications`
- пишет лог о полученной нотификации
- наружу через Ingress не публикуется

## Поток запроса

1. Клиент вызывает `POST /requests` через Ingress.
2. Ingress проксирует запрос в `request-service`.
3. `request-service` создаёт заявку и генерирует ID.
4. `request-service` отправляет HTTP-запрос на `http://notification-service/notifications`.
5. `notification-service` пишет событие в лог.
6. `request-service` возвращает клиенту `requestId`.

## Минимальные API

### `request-service`

`POST /requests`

Пример тела:

```json
{
  "text": "test request"
}
```

Пример ответа:

```json
{
  "requestId": "uuid",
  "status": "created"
}
```

### `notification-service`

`POST /notifications`

Пример тела:

```json
{
  "requestId": "uuid"
}
```

Ответ:

```json
{
  "status": "accepted"
}
```

## Что специально упрощаем

- без базы данных
- без очередей
- без отдельного UI
- без асинхронности
- без сложной бизнес-логики

## Диаграмма

```text
[Client]
   |
   v
[Ingress]
   |
   v
[request-service]
   |
   v  HTTP via Kubernetes DNS
[notification-service]
```
