package com.example.hello.user.mapper;

import com.example.hello.user.external.models.UserData;
import com.example.hello.user.models.UserResponse;

public class ResponseMapper {

    public UserResponse getResponse(UserData userData) {
        UserResponse userResponse = new UserResponse();
        if(userData.id <= 10 ){
            userResponse.setStatus(200);

        }
        else
        {
            userResponse.setStatus(400);
        }
        return userResponse;
    }
}
