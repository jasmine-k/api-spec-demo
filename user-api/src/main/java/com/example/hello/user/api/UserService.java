package com.example.hello.user.api;

import akka.NotUsed;
import com.example.hello.user.models.UserResponse;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.Service.named;

public interface UserService extends Service {

    ServiceCall<NotUsed, UserResponse> helloUser(Integer id);

    ServiceCall<NotUsed, UserResponse> welcomeUser(Integer id);

    @Override
    default Descriptor descriptor() {
        return named("helloUser").withCalls(
                restCall(Method.GET, "/user/:id", this::helloUser),
                restCall(Method.POST, "/welcome/:id", this::welcomeUser))
                .withAutoAcl(true);
    }

}
