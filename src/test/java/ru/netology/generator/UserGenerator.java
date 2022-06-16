package ru.netology.generator;

import com.github.javafaker.Faker;
import ru.netology.data.UserInfo;

public class UserGenerator {
    public static UserInfo generateUser(boolean status) {
        return new UserInfo(generateLogin(), generatePassword(), generateStatus(status));
    }

    public static String generateLogin() {
        Faker faker = new Faker();
        return faker.name().username();
    }

    public static String generatePassword() {
        Faker faker = new Faker();
        return faker.internet().password();
    }

    public static String generateStatus(boolean status) {
        if (status) {
            return "active";
        } else {
            return "blocked";
        }
    }


}
