package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import io.restassured.RestAssured;
import org.testng.annotations.Test;
import com.example.teamcity.api.enums.Endpoint;


import static io.qameta.allure.Allure.step;


@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest{
    @Test(description = "User should be able to create project with proper fields values", groups = "Positive")
    public void userCreatedProject() {
        step("Login as user");
        loginAs(testData.getUser());

        // взаимодействие с UI
        step("Create project via 'New Project' dialog", () -> {
            CreateProjectPage.open("_Root")
                    .CreateProject(testData.getProject().getName(), testData.getProject().getId(), "Created by Automation");
        });

        // проверка состояния API
        // (корректность отправки данных с UI на API)
        var createdProject = step("Check that the project was created correctly on the API level", () -> {
            var project = RestAssured.given().spec(Specifications.superUserSpec())
                    .get(Endpoint.PROJECTS.getUrl() + "/name:" + testData.getProject().getName())
                    .then().statusCode(200).extract().as(Project.class);
            softy.assertEquals(project.getName(), testData.getProject().getName(), "Project name is not correct");
            return project;
        });

        step("Create build configuration for the project via UI", () -> {
            CreateBuildTypePage.open(createdProject.getId())
                    .createBuildType(testData.getBuildType().getName(), testData.getBuildType().getId());
        });

        // проверка состояния UI
        // (корректность считывания данных и отображение данных на UI)
        step("Check that project is visible on Projects Page (http://localhost:8111/favorite/projects)", () -> {
            ProjectPage.open(createdProject.getId())
                    .title.shouldHave(Condition.exactText(testData.getProject().getName()));
        });
    }

    @Test(description = "User should not be able to craete project without name", groups = {"Negative"})
    public void userCreatesProjectWithoutName() {
        // подготовка окружения
        step("Login as user");
        step("Check number of projects");

        // взаимодействие с UI
        step("Open `Create Project Page` (http://localhost:8111/admin/createObjectMenu.html)");
        step("Send all project parameters (repository URL)");
        step("Click `Proceed`");
        step("Set Project Name");
        step("Click `Proceed`");

        // проверка состояния API
        // (корректность отправки данных с UI на API)
        step("Check that number of projects did not change");

        // проверка состояния UI
        // (корректность считывания данных и отображение данных на UI)
        step("Check that error appears `Project name must not be empty`");

    }
}
