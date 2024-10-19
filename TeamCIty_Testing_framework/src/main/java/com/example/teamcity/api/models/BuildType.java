package com.example.teamcity.api.models;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildType extends BaseModel{
    private String id;
    private String name;
    private Project project;
    private Steps steps;
}
