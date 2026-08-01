package com.example.teamcity.api.requests;

import com.example.teamcity.api.models.BaseModel;

public interface CrudInterface {
    Object create(BaseModel model);
    Object read(Locator locator);
    Object update(Locator locator, BaseModel model);
    Object delete(Locator locator);
}
