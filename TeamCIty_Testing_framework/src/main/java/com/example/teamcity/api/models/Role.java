package com.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends BaseModel {
    @Builder.Default
    private String roleId = "SYSTEM_ADMIN";
    @Builder.Default
    private String scope = "g";

    public static Role systemAdmin() {
        return new Role("SYSTEM_ADMIN", "g");
    }

    public static Role projectAdmin(String projectId) {
        return new Role("PROJECT_ADMIN", "p:" + projectId);
    }
}