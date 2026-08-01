package com.example.teamcity.ui.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class SetUpYourBuildPage extends BasePage{
    private final SelenideElement SkipButton = $(By.xpath("//button[contains(text(), 'Skip')]"));
    private final SelenideElement CreateButton = $(By.xpath("//button[contains(text(), 'Create')]"));
    private final SelenideElement NameField = $(By.xpath("//input[@aria-label='Name']"));
}
