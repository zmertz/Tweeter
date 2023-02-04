package edu.byu.cs.tweeter.client;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public class M4C_Test {
    AuthToken authToken = new AuthToken("c19370a0-e5f9-4996-b413-6e1a603cdf80", "1670280350844");
    private ServerFacade serverFacade;
    private MainPresenter.View mockView;
    private StatusService mockService;
    private MainPresenter presenterSpy;
    Status status;
    AuthToken token;

    // Login thru server facade
    // PostStatus from presenter
    // GetStory thru server facade

    @Before
    public void setup() {
        serverFacade = new ServerFacade();

        mockView = Mockito.mock(MainPresenter.View.class);
        mockService = Mockito.mock(StatusService.class);

        presenterSpy = Mockito.spy(new MainPresenter(mockView));
        Mockito.doReturn(mockService).when(presenterSpy).getStatusService();
    }

    @Test
    public void testCreateStatus() throws IOException, TweeterRemoteException {
        //--------------- Login through server facade -----------------------
        String newPost = "hello world";
        LoginResponse loginResponse = serverFacade.login(new LoginRequest("@j", "j"), "/login");
        Assert.assertEquals("John", loginResponse.getUser().getFirstName());
        Assert.assertEquals("Doe", loginResponse.getUser().getLastName());

        
        //------------------ Post Status from presenter -------------------------------
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                StatusService.GetPostObserver observer = invocation.getArgument(2);
                observer.handleSuccess();
                return null;
            }
        };
        Mockito.doAnswer(answer).when(mockService).post(Mockito.any(), Mockito.any(), Mockito.any());
        presenterSpy.postStatus(token, status);
        Mockito.verify(mockView).displayInfo("Posting Status...");
        Mockito.verify(mockView).displayPostSuccess("Successfully Posted!");


        //------------Get Story from server facade ---------------------------
        StoryResponse storyResponse = serverFacade.getStory(new StoryRequest(authToken, "@j", 10, ""), "/getstory");
        int count = 0;
        for (Status s: storyResponse.getStory()) {
            if (s.getPost().equals(newPost)) { count++; }
        }
        Assert.assertEquals(1, count);
    }


}
