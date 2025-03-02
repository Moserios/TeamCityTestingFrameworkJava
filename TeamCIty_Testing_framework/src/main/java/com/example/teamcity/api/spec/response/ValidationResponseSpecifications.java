package com.example.teamcity.api.spec.response;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

public class ValidationResponseSpecifications {

    public static ResponseSpecification checkBuildTypeIdAlreadyExists(String buildTypeId) {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .expectBody(Matchers.containsString(
                        "The build configuration / template ID \"%s\" is already used by another configuration or template".formatted(buildTypeId)
                ))
                .build();
    }

    public static ResponseSpecification checkProjectNameAlreadyExists(String projectName) {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .expectBody(Matchers.containsString(
                        "Project with this name already exists: %s".formatted(projectName)
                ))
                .build();
    }
}
