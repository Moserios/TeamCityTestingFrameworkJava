package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.pages.BasePage;

import static com.codeborne.selenide.Selenide.$;

public class CreateBuildTypePage extends BasePage {
    private static final String CREATE_URL = "/admin/createObjectMenu.html?projectId=%s&showMode=createBuildTypeMenu";

    private final SelenideElement manuallyTab = $("a[href='#createManually']");
    private final SelenideElement buildTypeNameInput = $("#buildTypeName");
    private final SelenideElement buildTypeIdInput = $("#buildTypeExternalId");
    private final SelenideElement submitButton = $("input[name='createBuildType']");
    private final SelenideElement nameRequiredError = $("#error_buildTypeName");
    private final SelenideElement skipVcsRootButton = $("a.cancel");

    public static CreateBuildTypePage open(String projectId) {
        var page = Selenide.open(CREATE_URL.formatted(projectId), CreateBuildTypePage.class);
        page.manuallyTab.click();
        return page;
    }

    public void createBuildType(String name, String id) {
        buildTypeNameInput.val(name);
        buildTypeIdInput.val(id);
        submitButton.click();
        skipVcsRootButton.shouldBe(Condition.visible).click();
    }

    public void submitWithoutName(String id) {
        buildTypeIdInput.val(id);
        submitButton.click();
    }

    public SelenideElement getNameRequiredError() {
        return nameRequiredError;
    }
}
