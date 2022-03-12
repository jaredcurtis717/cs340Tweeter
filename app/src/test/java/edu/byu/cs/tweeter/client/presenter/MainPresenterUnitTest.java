package edu.byu.cs.tweeter.client.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public class MainPresenterUnitTest {
    private MainPresenter.View mockView;
    private LoginService mockLoginService;
    private StatusService mockStatusService;
    private Cache mockCache;

    private MainPresenter mainPresenterSpy;

    @Before
    public void setup(){
        mockView = Mockito.mock(MainPresenter.View.class);
        mockLoginService = Mockito.mock(LoginService.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockCache = Mockito.mock(Cache.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
        //Mockito.when(mainPresenterSpy.getLoginService()).thenReturn(mockLoginService);
        Mockito.doReturn(mockLoginService).when(mainPresenterSpy).getLoginService();
        Mockito.doReturn(mockStatusService).when(mainPresenterSpy).getStatusService();

        Cache.setInstance(mockCache);
    }

    @Test
    public void testPostStatus_success(){
        basicPostStatusTest(TestStatus.pass);
        Mockito.verify(mockView).postSuccessful();
    }

    @Test
    public void testPostStatus_fail(){
        basicPostStatusTest(TestStatus.fail);
        Mockito.verify(mockView).displayErrorMessage("Failed to get post status: Failed to post");
    }

    @Test
    public void testPostStatus_exception(){
        basicPostStatusTest(TestStatus.exception);
        Mockito.verify(mockView).displayErrorMessage("Failed to get post status because of exception: exception message");
    }

    private void basicPostStatusTest(TestStatus pass) {
        Answer<Void> answer = getPostStatusAnswer(pass);

        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any());
        mainPresenterSpy.postStatus("Hello", "12pm", null, null);

        Mockito.verify(mockView).displayInfoMessage("Posting Status");
        Mockito.verify(mockStatusService).postStatus(Mockito.any(), Mockito.any());
    }

    private Answer<Void> getPostStatusAnswer(TestStatus result) {
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Status testStatus = invocation.getArgument(0);
                Assert.assertSame(testStatus.getClass(), Status.class);
                Assert.assertNotNull(testStatus);

                SimpleNotificationObserver observer = invocation.getArgument(1, SimpleNotificationObserver.class);
                Assert.assertSame(observer.getClass(), MainPresenter.PostStatusObserver.class);
                Assert.assertNotNull(observer);

                if (result == TestStatus.pass)
                    observer.handleSuccess();
                else if(result == TestStatus.fail)
                    observer.handleFailure("Failed to post");
                else
                    observer.handleException(new Exception("exception message"));

                return null;
            }
        };
        return answer;
    }

    private enum TestStatus{
        fail,
        pass,
        exception
    }

    @Test
    public void testLogout_success(){
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                SimpleNotificationObserver observer = invocation.getArgument(1, SimpleNotificationObserver.class);
                observer.handleSuccess();
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockLoginService).logout(Mockito.any(), Mockito.any());
        mainPresenterSpy.logout();

        Mockito.verify(mockView).displayInfoMessage("Logging Out");
        Mockito.verify(mockCache).clearCache();
        Mockito.verify(mockView).clearInfoMessage();
        Mockito.verify(mockView).logoutSuccessful();
    }
    @Test
    public void testLogout_failWithMessage(){
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                SimpleNotificationObserver observer = invocation.getArgument(1, SimpleNotificationObserver.class);
                observer.handleFailure("failure");
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockLoginService).logout(Mockito.any(), Mockito.any());
        mainPresenterSpy.logout();

        Mockito.verify(mockView).displayInfoMessage("Logging Out");

        Mockito.verify(mockCache, Mockito.times(0)).clearCache();
        Mockito.verify(mockView).clearInfoMessage();
        Mockito.verify(mockView).displayErrorMessage("Failed to get logout: failure");
    }
    @Test
    public void testLogout_failWithException(){
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                SimpleNotificationObserver observer = invocation.getArgument(1, SimpleNotificationObserver.class);
                observer.handleException(new Exception("the message from the exception"));
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockLoginService).logout(Mockito.any(), Mockito.any());
        mainPresenterSpy.logout();

        Mockito.verify(mockView).displayInfoMessage("Logging Out");

        Mockito.verify(mockCache, Mockito.times(0)).clearCache();
        Mockito.verify(mockView).clearInfoMessage();
        Mockito.verify(mockView).displayErrorMessage("Failed to get logout because of exception: the message from the exception");
    }
}
