package ru.netology.test;

import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.netology.data.UserInfo;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.generator.UserGenerator.*;
import static ru.netology.registration.UserRegistration.registration;

public class IBankTest {
    UserInfo userActive = generateUser(true);
    UserInfo userBlocked = generateUser(false);
    UserInfo userNotRegistration = generateUser(true);
    SelenideElement form = $x("//form");
    SelenideElement error = $x("//div[@data-test-id='error-notification']");

    @BeforeClass
    public void registrationUsers() {
        registration(userActive);
        registration(userBlocked);
    }

    @BeforeMethod
    public void setup() {
        open("http://localhost:9999/");
    }

    @Test
    public void shouldAuthActiveUser() {
        form.$x(".//span[@data-test-id='login']//child::input").val(userActive.getLogin());
        form.$x(".//span[@data-test-id='password']//child::input").val(userActive.getPassword());
        form.$x(".//button").click();
        $x("//h2").should(text("Личный кабинет"));
    }

    @Test
    public void shouldNoAuthBlockedUser() {
        form.$x(".//span[@data-test-id='login']//child::input").val(userBlocked.getLogin());
        form.$x(".//span[@data-test-id='password']//child::input").val(userBlocked.getPassword());
        form.$x(".//button").click();
        error.should(visible);
        error.$x(".//div[@class='notification__content']").should(text("Пользователь заблокирован"));
        error.$x(".//button").click();
        error.should(hidden);
    }

    @Test
    public void shouldNoAuthNotRegistrationUser() {
        form.$x(".//span[@data-test-id='login']//child::input").val(userNotRegistration.getLogin());
        form.$x(".//span[@data-test-id='password']//child::input").val(userNotRegistration.getPassword());
        form.$x(".//button").click();
        error.should(visible);
        error.$x(".//div[@class='notification__content']").should(text("Неверно указан логин или пароль"));
        error.$x(".//button").click();
        error.should(hidden);
    }

    @Test
    public void shouldNoAuthInvalidLogin() {
        form.$x(".//span[@data-test-id='login']//child::input").val(generateLogin());
        form.$x(".//span[@data-test-id='password']//child::input").val(userActive.getPassword());
        form.$x(".//button").click();
        error.should(visible);
        error.$x(".//div[@class='notification__content']").should(text("Неверно указан логин или пароль"));
        error.$x(".//button").click();
        error.should(hidden);
    }

    @Test
    public void shouldNoAuthInvalidPassword() {
        form.$x(".//span[@data-test-id='login']//child::input").val(userActive.getLogin());
        form.$x(".//span[@data-test-id='password']//child::input").val(generatePassword());
        form.$x(".//button").click();
        error.should(visible);
        error.$x(".//div[@class='notification__content']").should(text("Неверно указан логин или пароль"));
        error.$x(".//button").click();
        error.should(hidden);
    }
}
