// модернизированный файлик с тестом

package com.example.teamcity.api;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.Locator;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.api.spec.response.ValidationResponseSpecifications;
import com.example.teamcity.api.models.*;
import org.testng.annotations.Test;
import java.util.Arrays;

import static io.qameta.allure.Allure.step;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;

public class BuildTypeTest extends BaseApiTest {
    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        superUserCheckRequests.getRequest(Endpoint.USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckRequests.<Project>getRequest(Endpoint.PROJECTS).create(testData.getProject());
        userCheckRequests.getRequest(Endpoint.BUILD_TYPES).create(testData.getBuildType());
        var createdBuildType = userCheckRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES).read(Locator.id(testData.getBuildType().getId()));
        softy.assertEquals(createdBuildType, testData.getBuildType(), "Build type name is not correct");
    }



    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoBuildTypesWithTheSameIdTest(){
        var buildTypeWithTheSameId = generate(Arrays.asList(testData.getProject()), BuildType.class, testData.getBuildType().getId());
        superUserCheckRequests.getRequest(Endpoint.USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckRequests.<Project>getRequest(Endpoint.PROJECTS).create(testData.getProject());
        userCheckRequests.getRequest(Endpoint.BUILD_TYPES).create(testData.getBuildType());
        new UncheckedBase(Specifications.authSpec(testData.getUser()), Endpoint.BUILD_TYPES)
                .create(buildTypeWithTheSameId)
                .then().spec(ValidationResponseSpecifications.checkBuildTypeIdAlreadyExists(testData.getBuildType().getId()));
    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildTypeTest(){
        step("Create user", () -> {
                    superUserCheckRequests.getRequest(Endpoint.USERS).create(testData.getUser());
                });

        var adminRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        step("Create project by user", () -> {
            adminRequests.<Project>getRequest(Endpoint.PROJECTS).create(testData.getProject());
        });

        step("Create buildType1 for project by user", () -> {
            adminRequests.getRequest(Endpoint.BUILD_TYPES).create(testData.getBuildType());
        });

        step("Read and verify the created build type", () -> {
            var createdBuildType = adminRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES).read(Locator.id(testData.getBuildType().getId()));
            softy.assertEquals(createdBuildType, testData.getBuildType(), "Build type name is not correct");
        });
    }

    @Test(description = "Project admin should not be able to create two build types with the same id", groups = {"Negative", "Roles"})
    public void projectAdminCannotCreateDuplicateBuildTypeTest(){
        step("Create user", () -> {
            superUserCheckRequests.getRequest(Endpoint.USERS).create(testData.getUser());
        });

        var adminRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project by project admin", () -> {
            adminRequests.<Project>getRequest(Endpoint.PROJECTS).create(testData.getProject());
        });


        step("Create build type", () -> {
            adminRequests.getRequest(Endpoint.BUILD_TYPES).create(testData.getBuildType());
        });


        step("Attempt to create a duplicate build type with the same id", () -> {
            var duplicateBuildType = TestDataGenerator.generate(
                    Arrays.asList(testData.getProject()), BuildType.class, testData.getBuildType().getId());
            new UncheckedBase(Specifications.authSpec(testData.getUser()), Endpoint.BUILD_TYPES)
                    .create(duplicateBuildType)
                    .then().spec(ValidationResponseSpecifications.checkBuildTypeIdAlreadyExists(testData.getBuildType().getId()));
        });

    }


    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        User user1 = testData.getUser();
        step("Create sysAdmin user (user1)", () ->{
            user1.setRoles(new Roles(Arrays.asList(Role.systemAdmin())));
            superUserCheckRequests.getRequest(Endpoint.USERS).create(user1);
        });

        var user1Requests = new CheckedRequests(Specifications.authSpec(user1));
        Project project1 = testData.getProject();

        step("Create project1 by user1",() -> {
            user1Requests.<Project>getRequest(Endpoint.PROJECTS).create(project1);
        });

        BuildType buildType = testData.getBuildType();
        step("Create build type for project1 by user1", () ->{
            buildType.setProject(project1);
            user1Requests.getRequest(Endpoint.BUILD_TYPES).create(buildType);
        });

        Project project2 = TestDataGenerator.generate(Project.class);

        step("Use user1's credentials (superUser) to create project2", () -> {
            user1Requests.<Project>getRequest(Endpoint.PROJECTS).create(project2);
        });

        User  user2 = TestDataGenerator.generate(User.class);

        step("Set user2 with projectAdmin role (user2)",() ->{
            user2.setRoles(new Roles(Arrays.asList(Role.projectAdmin(project2.getId()))));
            superUserCheckRequests.getRequest(Endpoint.USERS).create(user2);
        });


        step("Attempt to create build type for project1 by user2 and verify that build type creation is forbidden for a projectAdmin on another user's project", () -> {
            new UncheckedBase(Specifications.authSpec(user2), Endpoint.BUILD_TYPES)
                    .create(buildType)
                    .then().spec(ValidationResponseSpecifications.checkAccessForbidden());
        });
    }
}