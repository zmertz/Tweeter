package edu.byu.cs.tweeter.client;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusTest {

    private MainPresenter.View mockView;
    private StatusService mockService;
    private MainPresenter presenterSpy;

    AuthToken token;
    Status status;

    @Before
    public void setup()  {
        mockView = Mockito.mock(MainPresenter.View.class);
        mockService = Mockito.mock(StatusService.class);

        token = new AuthToken();

        presenterSpy = Mockito.spy(new MainPresenter(mockView));
        Mockito.doReturn(mockService).when(presenterSpy).getStatusService();
    }

    @Test
    public void testPostSuccess() {
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                StatusService.GetPostObserver observer = invocation.getArgument(2);
                observer.handleSuccess();
                //token, status, GetPostObserver
                assertEquals(invocation.getArgument(0), token);
                assertEquals(invocation.getArgument(1), status);
                assertEquals(invocation.getArgument(2), observer);
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockService).post(Mockito.any(), Mockito.any(), Mockito.any());

        presenterSpy.postStatus(token, status);
        Mockito.verify(mockView).displayInfo("Posting Status...");
        Mockito.verify(mockView).displayPostSuccess("Successfully Posted!");
    }

    @Test
    public void testPostFail() {
        Answer<Void> failAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                StatusService.GetPostObserver observer = invocation.getArgument(2);
                observer.handleFailure("Failed to post");
                assertEquals(invocation.getArgument(0), token);
                assertEquals(invocation.getArgument(1), status);
                assertEquals(invocation.getArgument(2), observer);
                return null;
            }
        };

        Mockito.doAnswer(failAnswer).when(mockService).post(Mockito.any(), Mockito.any(), Mockito.any());

        presenterSpy.postStatus(token, status);
        Mockito.verify(mockView).displayInfo("Posting Status...");
        Mockito.verify(mockView).displayInfo("Failed to post");
    }

    @Test
    public void testPostException() {
        Answer<Void> excAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation)  {
                StatusService.GetPostObserver observer = invocation.getArgument(2);
                Exception exception = new Exception("Failed due to exception");
                observer.handleException(exception);
                assertEquals(invocation.getArgument(0), token);
                assertEquals(invocation.getArgument(1), status);
                assertEquals(invocation.getArgument(2), observer);
                return null;
            }
        };

        Mockito.doAnswer(excAnswer).when(mockService).post(Mockito.any(), Mockito.any(),Mockito.any());

        presenterSpy.postStatus(token, status);
        Mockito.verify(mockView).displayInfo("Posting Status...");
        Mockito.verify(mockView).displayInfo("Failed due to exception");
    }
}
