package com.example.teamcity.ui;

import com.example.teamcity.api.models.User;
import com.example.teamcity.ui.pages.LoginPage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
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
        step("Open `Create Project Page` (http://localhost:8111/admin/createObjectMenu.html)");
        step("Send all project parameters (repository URL)");
        step("Click `Proceed`");
        step("Fix Project Name and Build Type name values");
        step("Click `Proceed`");
        CreateProjectPage.open("_Root").CreateProject("Project 09.02.2026", "09022026", "Created by Automation");

        // проверка состояния API
        // (корректность отправки данных с UI на API)
        step("Check that all entities (project, build type) was successfully created with correct data on API level");

        // проверка состояния UI
        // (корректность считывания данных и отображение данных на UI)
        step("Check that project is visible on Projects Page (http://localhost:8111/favorite/projects)");
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
