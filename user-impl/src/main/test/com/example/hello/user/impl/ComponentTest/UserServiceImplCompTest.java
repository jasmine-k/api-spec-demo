package com.example.hello.user.impl.ComponentTest;

import akka.NotUsed;
import com.example.hello.user.api.UserService;
import com.example.hello.user.external.api.ExternalService;
import com.example.hello.user.external.models.UserData;
import com.example.hello.user.models.UserResponse;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.CompletableFuture;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class UserServiceImplCompTest extends Mockito {

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
    private static UserService service;
    private static ServiceTest.TestServer server;
    private static ServiceTest.Setup setup = defaultSetup()
            .withCassandra(false)
            .withCluster(false)
            .withConfigureBuilder(b -> b.overrides
                    (bind(ExternalService.class).to(ExternalStub.class)));

    @BeforeClass
    public static void setup() {

        server = startServer(setup);
        service = server.client(UserService.class);

    }

    @AfterClass
    public static void tearDown() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    @Test
    public void shouldGetUserData() throws Exception {
        UserResponse receivedResponse = service
                .helloUser()
                .invoke()
                .toCompletableFuture().get(100, SECONDS);

        assertEquals("helloUser method fails ", "Hello, Your UserId is " + ID, receivedResponse.getMessage());

    }

    static class ExternalStub implements ExternalService {

        @Override
        public ServiceCall<NotUsed, UserData> getUser() {
            return request -> CompletableFuture.completedFuture(newUserData);
        }
    }

}
