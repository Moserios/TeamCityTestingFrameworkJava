package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Steps {
    private Integer count;
    private List<Step> step;
}
