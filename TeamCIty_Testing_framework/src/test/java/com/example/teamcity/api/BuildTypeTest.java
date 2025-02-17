// модернизированный файлик с тестом

package com.example.teamcity.api;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import java.util.Arrays;

import static com.example.teamcity.api.enums.Endpoint.*;
import static io.qameta.allure.Allure.step;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;

public class BuildTypeTest extends BaseApiTest{
    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());
        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());
        softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(), "Build type name is not correct");
    }



    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoBuildTypesWithTheSameIdTest(){
        var buildTypeWithTheSameId = generate(Arrays.asList(testData.getProject()), BuildType.class, testData.getBuildType().getId());
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());
        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
                .create(buildTypeWithTheSameId)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("The build configuration / template ID \"%s\" is already used by another configuration or template".formatted(testData.getBuildType().getId())));
    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildTypeTest(){
        step("Create user");
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var adminRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project by user");
        adminRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        adminRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        step("Create buildType1 for project by user");

        step("Read and verify the created build type");
        var createdBuildType = adminRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());
        softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(), "Build type name is not correct");
    }

    @Test(description = "Project admin should not be able to create two build types with the same id", groups = {"Negative", "Roles"})
    public void projectAdminCannotCreateDuplicateBuildTypeTest(){
        step("Create user");
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var adminRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project by project admin");
        adminRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        step("Create build type");
        adminRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        step("Attempt to create a duplicate build type with the same id");
        var duplicateBuildType = com.example.teamcity.api.generators.TestDataGenerator.generate(
                Arrays.asList(testData.getProject()), BuildType.class, testData.getBuildType().getId());
        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
                .create(duplicateBuildType)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(
                "The build configuration / template ID \"%s\" is already used by another configuration or template"
                .formatted(testData.getBuildType().getId())));
    }


    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        step("Create sysAdmin user (user1)");
        User user1 = testData.getUser();
        user1.setRoles(new Roles(Arrays.asList(Role.systemAdmin())));
        superUserCheckRequests.getRequest(USERS).create(user1);
        var user1Requests = new CheckedRequests(Specifications.authSpec(user1));

        step("Create project1 by user1");
        Project project1 = testData.getProject();
        user1Requests.<Project>getRequest(PROJECTS).create(project1);

        step("Create build type for project1 by user1");
        BuildType buildType = testData.getBuildType();
        buildType.setProject(project1);
        user1Requests.getRequest(BUILD_TYPES).create(buildType);

        step("Create project2 for user2");
        Project project2 = com.example.teamcity.api.generators.TestDataGenerator.generate(Project.class);

        step("Use user1's credentials (superUser) to create project2");
        user1Requests.<Project>getRequest(PROJECTS).create(project2);

        step("Create user2");
        User user2 = com.example.teamcity.api.generators.TestDataGenerator.generate(User.class);

        step("Set user2 with projectAdmin role (user2)");
        user2.setRoles(new Roles(Arrays.asList(Role.projectAdmin(project2.getId()))));
        superUserCheckRequests.getRequest(USERS).create(user2);

        step("Attempt to create build type for project1 by user2 and verify that build type creation is forbidden for a projectAdmin on another user's project");
        new UncheckedBase(Specifications.authSpec(user2), Endpoint.BUILD_TYPES)
                .create(buildType)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString("Access denied")); // Adjust this substring if needed

    }
}