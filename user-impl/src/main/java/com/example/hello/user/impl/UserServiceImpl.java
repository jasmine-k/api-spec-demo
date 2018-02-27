package com.example.hello.user.impl;

import akka.NotUsed;
import akka.japi.Pair;
import com.example.hello.user.api.UserService;
import com.example.hello.user.external.api.ExternalService;
import com.example.hello.user.external.models.UserData;
import com.example.hello.user.mapper.ResponseMapper;
import com.example.hello.user.models.UserResponse;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;

import java.util.concurrent.CompletionStage;

public class UserServiceImpl implements UserService {

    ExternalService externalService;

    ResponseMapper responseMapper;

    @Inject
    public UserServiceImpl(ExternalService externalService, ResponseMapper responseMapper) {
        this.externalService = externalService;
        this.responseMapper = responseMapper;
    }

    public CompletionStage<UserData> hitAPI() {
        CompletionStage<UserData> userData = externalService
                .getUser()
                .invoke();
        return userData;

    }

    @Override
    public HeaderServiceCall<NotUsed, UserResponse> helloUser(Integer id) {
        return (requestHeader, request) ->
        {
            CompletionStage<UserData> userData = hitAPI();
            return userData.thenApply(
                    userInfo ->
                    {
                        userInfo.id = id;
                        UserResponse userResponse = responseMapper.getResponse(userInfo);
                        return Pair.create(ResponseHeader.OK.withStatus(userResponse.getStatus()), userResponse);
                    }
            );
        };
    }

    @Override
    public HeaderServiceCall<NotUsed, UserResponse> welcomeUser(Integer id){
        return (requestHeader, request) ->
        {
            CompletionStage<UserData> userData = hitAPI();
            return userData.thenApply(
                    userInfo ->
                    {
                        userInfo.id = id;
                        UserResponse userResponse = responseMapper.getResponse(userInfo);
                        return Pair.create(ResponseHeader.OK.withStatus(userResponse.getStatus()), userResponse);
                    }
            );
        };
    }
}
