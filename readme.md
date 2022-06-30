[![Build status](https://ci.appveyor.com/api/projects/status/923g2h3x4jv467e5?svg=true)](https://ci.appveyor.com/project/molottva/testregime)

# Домашнее задание к занятию «2.3. Patterns»

## Задача №2 - Тестовый режим

Разработчики Интернет Банка изрядно поворчав предоставили вам тестовый режим запуска целевого сервиса, в котором открыта программная возможность создания Клиентов Банка, чтобы вы могли протестировать хотя бы функцию входа.

**Важно**: ваша задача заключается в том, чтобы протестировать функцию входа через Web интерфейс с использованием Selenide.

Для удобства вам предоставили "документацию", которая описывает возможность программного создания Клиентов Банка через API. Вот представленное ими описание (дословно):
```
Для создания клиента нужно делать запрос вида:

POST /api/system/users
Content-Type: application/json

{
    "login": "vasya",
    "password": "password",
    "status": "active" 
}

Возможные значения поля статус:
* "active" - пользователь активен
* "blocked" - пользователь заблокирован

В случае успешного создания пользователя возвращается код 200

При повторной передаче пользователя с таким же логином будет выполнена перезапись данных пользователя
```

Давайте вместе разбираться. Мы уже проходили:
* клиент-серверное взаимодействие
* HTTP-методы и коды ответов
* формат данных - JSON
* REST-assured

Мы крайне настоятельно рекомендуем ознакомиться с документацией и примерами на [Rest Assured](http://rest-assured.io).

Подключается обычным образом в Gradle:
```groovy
testImplementation 'io.rest-assured:rest-assured:4.3.0'
testImplementation 'com.google.code.gson:gson:2.8.6'
```

Библиотека [Gson](https://github.com/google/gson) нужна для того, чтобы иметь возможность сериализовать (преобразовывать) Java-объекты в JSON.

Т.е. мы не руками пишем JSON, а создаём Data-классы, объекты которых и преобразуются в JSON.

Дальнейшее использование выглядит следующим образом:
```java
// спецификация нужна для того, чтобы переиспользовать настройки в разных запросах
class AuthTest {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
        .setBaseUri("http://localhost")
        .setPort(9999)
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .log(LogDetail.ALL)
        .build();

    @BeforeAll
    static void setUpAll() {
        // сам запрос
        given() // "дано"
            .spec(requestSpec) // указываем, какую спецификацию используем 
            .body(new RegistrationDto("vasya", "password", "active")) // передаём в теле объект, который будет преобразован в JSON
        .when() // "когда" 
            .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
        .then() // "тогда ожидаем"
            .statusCode(200); // код 200 OK
    }
    ...
}
```

Это не лучший формат организации, будет лучше, если как в предыдущей задаче, вы вынесете это в класс-генератор, который по требованию вам будет создавать рандомного пользователя, сохранять его через API и возвращать вам в тест.

В логах теста вы увидите:
```
Request method:	POST
Request URI:	http://localhost:9999/api/system/users
Proxy:			<none>
Request params:	<none>
Query params:	<none>
Form params:	<none>
Path params:	<none>
Headers:		Accept=application/json, application/javascript, text/javascript, text/json
				Content-Type=application/json; charset=UTF-8
Cookies:		<none>
Multiparts:		<none>
Body:
{
    "login": "vasya",
    "password": "password",
    "status": "active" 
}
```

Для активации этого тестового режима при запуске SUT нужно указать флаг `-P:profile=test`, т.е.:
`java -jar app-ibank.jar -P:profile=test`.

**Важно:** если вы не активируете тестовый режим, любые запросы на http://localhost:9999/api/system/users будут вам возвращать 404 Not Found.

Вам нужно самостоятельно изучить реакцию приложения на различные комбинации случаев (вспомните комбинаторику):
* наличие пользователя;
* статус пользователя;
* невалидный логин;
* невалидный пароль.

Дополнительно: оцените время, которое вы затратили на автоматизацию, и время, за которое вы проверили бы те же сценарии вручную, используя для тестирования интерфейса браузер и Postman для доступа к открытому API.

Приложите к решению задачи, в формате:
* Время, затраченное на ручное тестирование (минут): x
* Время, затраченное на автоматизацию (минут): y

#### Подсказки

> Не читайте этот раздел сразу, попытайтесь сначала решить задачу самостоятельно :)

<details>

<summary>Подсказка 1. Как подключить Lombok?</summary>

Посмотрите видео "Lombok & Lambda" в уроке "Основы автоматизации"

</details>
