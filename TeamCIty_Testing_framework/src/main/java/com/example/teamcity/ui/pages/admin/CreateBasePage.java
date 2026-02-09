package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.pages.BasePage;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.$;


public abstract class CreateBasePage extends BasePage {
    protected static final String CREATE_URL = "/projects/create?projectId=%s"; // _Root
    protected SelenideElement inputProjectName = $(By.xpath("//label/following-sibling::div/input[contains(@data-test, 'project-name-input')]"));
    protected SelenideElement inputProjectId = $(By.xpath("//label/following-sibling::div/input[contains(@data-test, 'project-id-input')]"));
    protected SelenideElement inputProjectDescription = $(By.xpath("//label/following-sibling::div/textarea"));
    protected SelenideElement submitButton = $(By.xpath(".//button[contains(@class, 'ring-button-button') and contains (text(), 'Create')]"));

    protected void FillCreateProjectForm(String projectName, String projectId, String projectDescription) {
        inputProjectName.val(projectName);
        inputProjectId.val(projectId);
        inputProjectDescription.val(projectDescription);
    }

}
