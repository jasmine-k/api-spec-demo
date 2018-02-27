package com.example.hello.user.impl.UnitTest;

import akka.NotUsed;
import com.example.hello.user.external.api.ExternalService;
import com.example.hello.user.external.models.UserData;
import com.example.hello.user.impl.UserServiceImpl;
import com.example.hello.user.mapper.ResponseMapper;
import com.example.hello.user.models.UserResponse;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class UserServiceImplTest {

    private static final Integer ID = 1;
    private static final Integer USERID = 1;
    private static final String TITLE = "title";
    private static final String BODY = "body";
    private static UserData newUserData = new UserData() {
        {
            setId(ID);
            setUserId(USERID);
            setTitle(TITLE);
            setBody(BODY);
        }
    };

    @SuppressWarnings("unused")
    @Tested
    private UserServiceImpl userServiceImpl;

    @SuppressWarnings("unused")
    @Injectable
    private ExternalService externalService;

    @SuppressWarnings("unused")
    @Injectable
    private ResponseMapper responseMapper;

    @Test
    public void helloUserTest() throws Exception {
        new Expectations() {
            {
                externalService.getUser();

                result = new ServiceCall<NotUsed, UserData>() {
                    @Override
                    public CompletionStage<UserData> invoke(NotUsed notUsed) {
                        return CompletableFuture.completedFuture(newUserData);
                    }
                };
            }

            {
                responseMapper.getResponse(newUserData);
                result = new UserResponse() {
                    {
                        setMessage("Hello, Your UserId is " + newUserData.userId);
                    }
                };
            }
        };
        UserResponse receivedResponse = userServiceImpl
                .helloUser()
                .invoke()
                .toCompletableFuture().get(5, SECONDS);

        System.out.println(receivedResponse);
        assertEquals("helloUser method fails ", "Hello, Your UserId is " + ID, receivedResponse.getMessage());
    }

}
