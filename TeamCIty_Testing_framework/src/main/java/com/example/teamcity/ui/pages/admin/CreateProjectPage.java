package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.example.teamcity.ui.pages.ProjectsPage;

public class CreateProjectPage extends CreateBasePage{

    public static CreateProjectPage open(String projectId){
        return Selenide.open(CREATE_URL.formatted(projectId), CreateProjectPage.class);
    }


    public ProjectsPage CreateProject(String projectName, String projectId, String projectDescription)
    {
        FillCreateProjectForm(projectName, projectId, projectDescription);
        submitButton.click();
        return Selenide.page (ProjectsPage.class);
    }
}
