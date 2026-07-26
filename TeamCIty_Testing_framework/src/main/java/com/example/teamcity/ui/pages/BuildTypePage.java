package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class BuildTypePage extends BasePage {
    private static final String PROJECT_URL = "/project/%s";

    public static BuildTypePage open(String projectId) {
        return Selenide.open(PROJECT_URL.formatted(projectId), BuildTypePage.class);
    }

    public SelenideElement buildTypeLink(String buildTypeName) {
        return $x(".//a[@title=%s and contains(@href, '/buildConfiguration/')]".formatted(quote(buildTypeName)));
    }

    public boolean hasBuildType(String buildTypeName) {
        return buildTypeLink(buildTypeName).is(Condition.visible);
    }

    private static String quote(String value) {
        return "'" + value + "'";
    }
}
