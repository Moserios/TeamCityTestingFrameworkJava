package com.example.teamcity.ui;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Role;
import com.example.teamcity.api.models.Roles;
import com.example.teamcity.api.requests.Locator;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.api.spec.response.ValidationResponseSpecifications;
import com.example.teamcity.ui.pages.BuildTypePage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import com.codeborne.selenide.Condition;
import org.testng.annotations.Test;

import java.util.Arrays;

import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class CreateBuildTypeTest extends BaseUiTest {

    @Test(description = "User should be able to create a build configuration for their project", groups = {"Positive"})
    public void userCreatesBuildType() {
        step("Create project via API", () -> {
            superUserCheckRequests.getRequest(Endpoint.PROJECTS).create(testData.getProject());
        });

        step("Grant the user Project Admin rights for the project and log in", () -> {
            testData.getUser().setRoles(new Roles(Arrays.asList(Role.projectAdmin(testData.getProject().getId()))));
            loginAs(testData.getUser());
        });

        step("Create build configuration manually via UI", () -> {
            CreateBuildTypePage.open(testData.getProject().getId())
                    .createBuildType(testData.getBuildType().getName(), testData.getBuildType().getId());
        });

        step("Check that the build configuration was created correctly on the API level", () -> {
            var createdBuildType = superUserCheckRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES)
                    .read(Locator.id(testData.getBuildType().getId()));
            softy.assertEquals(createdBuildType.getName(), testData.getBuildType().getName(),
                    "Build configuration name is not correct");
        });

        step("Check that the build configuration is visible on the UI level", () -> {
            BuildTypePage.open(testData.getProject().getId())
                    .buildTypeLink(testData.getBuildType().getName())
                    .shouldBe(Condition.visible);
        });
    }

    @Test(description = "User should not be able to create a build configuration without a name", groups = {"Negative"})
    public void userCreatesBuildTypeWithoutName() {
        step("Create project via API", () -> {
            superUserCheckRequests.getRequest(Endpoint.PROJECTS).create(testData.getProject());
        });

        step("Grant the user Project Admin rights for the project and log in", () -> {
            testData.getUser().setRoles(new Roles(Arrays.asList(Role.projectAdmin(testData.getProject().getId()))));
            loginAs(testData.getUser());
        });

        var createBuildTypePage = CreateBuildTypePage.open(testData.getProject().getId());

        step("Attempt to create build configuration without a name", () -> {
            createBuildTypePage.submitWithoutName(testData.getBuildType().getId());
        });

        step("Check that error appears `Name must not be empty`", () -> {
            createBuildTypePage.getNameRequiredError().shouldHave(Condition.exactText(CreateBuildTypePage.NAME_REQUIRED_ERROR));
        });

        step("Check that the build configuration was not created", () -> {
            new UncheckedBase(Specifications.superUserSpec(), Endpoint.BUILD_TYPES)
                    .read(Locator.id(testData.getBuildType().getId()))
                    .then().spec(ValidationResponseSpecifications.checkEntityNotFound());
        });
    }
}
