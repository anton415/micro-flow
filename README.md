# micro-flow
Kubernetes-based Request & Notification System.
Контрольный проект по модулю микросервисы.

## Задание от ментора:
> да привет суши тестовое задание она простая то есть тебя 
> нужно поднять мини куб внутрь в рамках у микросервиса это все должен был жить в нем 
> настроить два сервиса например сервис заявки и сервис нотификации и сделать ingress 
> ты поэтому ingress у обращаешься к сервису к сервису заявок 
> заявка регистрируется а потом она через dns отправляет в сервис нотификации просто запись сделает там. 
> Все, вот такое задание должно быть. Оно достаточно... Оно, с одной стороны, простое, но с другой стороны, очень показательно от того, как должен выглядеть микросервисная архитектура. То есть, Kubernetes здесь является основными рельсами, в котором существует наш проект.

## Упрощённая трактовка задания

Если читать формулировку буквально, то обязательный минимум такой:

* поднять локальный Kubernetes-кластер
* развернуть внутри него 2 сервиса:
  * `request-service`
  * `notification-service`
* опубликовать наружу только `request-service` через Ingress
* при `POST /requests`:
  * `request-service` создаёт заявку
  * вызывает `notification-service` по DNS-имени Kubernetes
  * `notification-service` просто пишет событие в лог

Все остальные вещи вроде UI, базы данных, очередей, Helm и сложной схемы хранения для этого задания не обязательны.

## Цель
Сделать минимальный, но рабочий пример микросервисной архитектуры в Kubernetes: один внешний вход через Ingress и один внутренний межсервисный вызов по Kubernetes DNS.

## Функциональные требования

### 1. Инфраструктура

* Развернуть локальный Kubernetes-кластер (например, Minikube или Kind).
* Все компоненты системы должны работать внутри кластера.
* Использовать Kubernetes как основную платформу оркестрации.

### 2. Микросервисы

#### 2.1 Сервис заявок (Request Service)

Функции:

* Принимает HTTP-запрос `POST /requests`.
* Генерирует ID заявки.
* Хранит заявку в памяти или даже только возвращает её в ответе.
* После регистрации вызывает сервис нотификаций.

Требования:

* REST API
* логирование
* генерация ID

#### 2.2 Сервис нотификаций (Notification Service)

Функции:

* Принимает запрос от сервиса заявок.
* Ничего не хранит постоянно.
* Просто пишет в лог факт получения нотификации.

Требования:

* внутренний HTTP endpoint
* логирование событий

### 3. Взаимодействие сервисов

* Сервис заявок должен обращаться к сервису нотификаций:
    * через DNS внутри Kubernetes, например `http://notification-service`
* Без прямого IP — только через сервисы Kubernetes

### 4. Ingress

* Настроить Ingress Controller
* Дать внешний доступ только к `request-service`
* Достаточно одного маршрута, например `POST /requests`
* Входящий трафик должен идти по цепочке `Ingress -> Request Service`


### 5. Нефункциональные требования

* Контейнеризация:
    * каждый сервис должен быть упакован в Docker
* Kubernetes манифесты:
    * `Deployment`
    * `Service`
    * `Ingress`
* Логирование:
    * через `stdout`, чтобы проверять через `kubectl logs`
* Простота:
    * без Kafka / RabbitMQ
    * без базы данных
    * синхронный HTTP-запрос между сервисами достаточно


### 6. Критерии готовности

Проект считается завершённым, если:

1. локальный кластер запускается
2. оба сервиса работают в Kubernetes
3. `Ingress` пробрасывает запрос в `request-service`
4. `request-service` вызывает `notification-service` по DNS
5. в логах видно полный путь запроса

## Минимальная архитектура

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

## Самый простой сценарий проверки

1. Отправить `POST /requests` через Ingress.
2. Получить ответ с `requestId`.
3. Проверить логи `request-service`.
4. Проверить логи `notification-service`.

Если оба лога показывают одну и ту же заявку, то задание закрыто по сути.

## Как запустить

Ниже приведён минимальный сценарий запуска проекта в локальном `minikube`.

### 1. Предварительные требования

Нужно, чтобы были установлены:

* `Docker`
* `kubectl`
* `minikube`
* `Java 17`
* `Maven`

### 2. Проверить локально тесты

```bash
cd services/request-service
mvn test

cd ../notification-service
mvn test
```

### 3. Запустить `minikube`

```bash
minikube start
minikube addons enable ingress
```

Проверить текущий контекст:

```bash
kubectl config current-context
```

Ожидаемо: `minikube`.

### 4. Собрать Docker-образы

Из корня проекта:

```bash
docker build -t request-service:local services/request-service
docker build -t notification-service:local services/notification-service
```

### 5. Загрузить образы в `minikube`

```bash
minikube image load request-service:local
minikube image load notification-service:local
```

### 6. Применить Kubernetes-манифесты

```bash
kubectl apply -f k8s/
```

Проверить, что всё поднялось:

```bash
kubectl get pods
kubectl get svc
kubectl get ingress
```

### 7. Дождаться готовности сервисов

```bash
kubectl rollout status deployment/request-service
kubectl rollout status deployment/notification-service
kubectl rollout status deployment/ingress-nginx-controller -n ingress-nginx
```

## Как проверить

### Вариант 1. Через `minikube tunnel`

В отдельном терминале:

```bash
minikube tunnel
```

После этого можно отправить запрос через Ingress:

```bash
curl -i http://127.0.0.1/requests -H 'Content-Type: application/json' -d '{"text":"demo"}'
```

### Вариант 2. Через `port-forward` ingress-контроллера

Если `minikube tunnel` просит `sudo` или неудобен, можно проверить Ingress так:

```bash
kubectl port-forward -n ingress-nginx service/ingress-nginx-controller 8088:80
```

В другом терминале:

```bash
curl -i http://127.0.0.1:8088/requests \
  -H 'Content-Type: application/json' \
  -d '{"text":"demo"}'
```

Ожидаемый ответ:

```json
{"requestId":"<uuid>","status":"created"}
```

### Проверка логов

После успешного запроса нужно проверить логи обоих сервисов:

```bash
kubectl logs deployment/request-service --since=5m
kubectl logs deployment/notification-service --since=5m
```

В логах должно быть видно:

* в `request-service`:
  * создание заявки
  * отправку уведомления
* в `notification-service`:
  * получение того же `requestId`

Если один и тот же `requestId` есть в ответе API и в логах обоих сервисов, значит цепочка `Ingress -> request-service -> notification-service` работает корректно.
