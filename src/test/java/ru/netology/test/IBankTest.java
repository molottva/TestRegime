package ru.netology.test;

import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.netology.data.UserInfo;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.generator.UserGenerator.generateUser;
import static ru.netology.registration.UserRegistration.registration;

public class IBankTest {
    UserInfo userActive = generateUser(true);
    UserInfo userBlocked = generateUser(false);
    SelenideElement form = $x("//form");
    SelenideElement error = $x("//div[@data-test-id='error-notification']");

    @BeforeMethod
    public void setup() {
        open("http://localhost:9999/");
    }

    @Test
    public void shouldAuthActiveUser() {
        registration(userActive);
        form.$x(".//span[@data-test-id='login']//child::input").val(userActive.getLogin());
        form.$x(".//span[@data-test-id='password']//child::input").val(userActive.getPassword());
        form.$x(".//button").click();
        $x("//h2").should(text("Личный кабинет"));
    }
}
