# План выполнения

## Требование

- [x] Поднять локальный Kubernetes-кластер
- [x] Сделать `request-service`
- [x] Сделать `notification-service`
- [x] Упаковать оба сервиса в Docker
- [ ] Создать `Deployment`, `Service`, `Ingress`
- [ ] Проверить сценарий `Ingress -> request-service -> notification-service`

## Сервисы

### `request-service`
- [x] `POST /requests`
- [x] генерирует `requestId`
- [x] вызывает `http://notification-service/notifications`
- [x] пишет лог о создании заявки

### `notification-service`
- [x] `POST /notifications`
- [x] принимает `requestId`
- [x] пишет лог о полученной нотификации

## Kubernetes

- [ ] `Deployment` для `request-service`
- [ ] `Service` для `request-service`
- [ ] `Deployment` для `notification-service`
- [ ] `Service` для `notification-service`
- [ ] `Ingress` на маршрут к `request-service`

## Проверка

1. Выполнить запрос в `request-service` через Ingress.
2. Убедиться, что API вернул `requestId`.
3. Посмотреть `kubectl logs` у `request-service`.
4. Посмотреть `kubectl logs` у `notification-service`.
