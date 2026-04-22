# Короткий план выполнения

## Что реально нужно сделать

- [x] Поднять локальный Kubernetes-кластер
- [ ] Сделать `request-service`
- [ ] Сделать `notification-service`
- [ ] Упаковать оба сервиса в Docker
- [ ] Создать `Deployment`, `Service`, `Ingress`
- [ ] Проверить сценарий `Ingress -> request-service -> notification-service`

## Минимальный scope по сервисам

### `request-service`
- [ ] `POST /requests`
- [ ] генерирует `requestId`
- [ ] вызывает `http://notification-service/notifications`
- [ ] пишет лог о создании заявки

### `notification-service`
- [ ] `POST /notifications`
- [ ] принимает `requestId`
- [ ] пишет лог о полученной нотификации

## Минимальный scope по Kubernetes

- [ ] `Deployment` для `request-service`
- [ ] `Service` для `request-service`
- [ ] `Deployment` для `notification-service`
- [ ] `Service` для `notification-service`
- [ ] `Ingress` на маршрут к `request-service`

## Что не делаем

- [ ] UI
- [ ] база данных
- [ ] брокер сообщений
- [ ] gRPC
- [ ] Helm
- [ ] сложная доменная модель

## Финальная проверка

1. Выполнить запрос в `request-service` через Ingress.
2. Убедиться, что API вернул `requestId`.
3. Посмотреть `kubectl logs` у `request-service`.
4. Посмотреть `kubectl logs` у `notification-service`.

Если запрос дошёл до обоих сервисов, то минимальное задание выполнено.
