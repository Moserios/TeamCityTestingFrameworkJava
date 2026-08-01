package com.example.teamcity.ui.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class RepositoryPage extends BasePage{
    private final SelenideElement ProceedButton = $(By.xpath("//a[contains(text(), 'Proceed without repository')]"));
}
