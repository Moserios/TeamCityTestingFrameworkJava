package com.example.teamcity.ui.pages;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.models.User;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage {
    private static final String LOGIN_URL = "/login.html";
    private final SelenideElement inputUsername = $(By.cssSelector("#username"));
    private final SelenideElement inputLogin = $(By.cssSelector("#password"));
    private final SelenideElement inputSubmitButton = $(By.cssSelector(".loginButton"));

    public static LoginPage open() {
        return Selenide.open(LOGIN_URL, LoginPage.class);
    }

    public ProjectsPage login(User user) {
        inputUsername.val(user.getUsername());
        inputLogin.val(user.getPassword());
        inputSubmitButton.click();

        return Selenide.page(ProjectsPage.class);
    }
}
