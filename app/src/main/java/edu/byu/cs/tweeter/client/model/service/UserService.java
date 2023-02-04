package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.NotifyHandler;
import edu.byu.cs.tweeter.client.model.service.observer.AuthObserver;
import edu.byu.cs.tweeter.client.model.service.observer.NotifyObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    public interface GetUserObserver extends ServiceObserver {
        void handleSuccess(User user);
    }

    public interface GetLoginObserver extends AuthObserver {}
    public interface GetRegisterObserver extends AuthObserver {}
    public interface GetLogoutObserver extends NotifyObserver {}

    public void login(String alias, String password, GetLoginObserver getLoginObserver) {
        LoginTask loginTask = new LoginTask(alias, password,
                new GetLoginHandler(getLoginObserver));
        ServiceHelper.runTask(loginTask);
    }

    public void logout(AuthToken token, GetLogoutObserver getLogoutObserver) {
        LogoutTask logoutTask = new LogoutTask(token, new GetLogoutHandler(getLogoutObserver));
        ServiceHelper.runTask(logoutTask);
    }

    public void register(String firstName, String lastName, String alias, String password,
                         String image, GetRegisterObserver getRegisterObserver) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, image, new GetRegisterHandler(getRegisterObserver));
        ServiceHelper.runTask(registerTask);
    }

    public void getUser(AuthToken currUserAuthToken, String alias, GetUserObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                alias, new GetUserHandler(getUserObserver));
        ServiceHelper.runTask(getUserTask);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class GetLoginHandler extends AuthHandler {
        public GetLoginHandler(AuthObserver observer) {
            super(observer);
        }

    }

    private class GetRegisterHandler extends AuthHandler {
        public GetRegisterHandler(AuthObserver observer) {
            super(observer);
        }
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandler<T extends GetUserObserver> extends BackgroundTaskHandler<T> {
        public GetUserHandler(T observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(T observer, Bundle data) {
            observer.handleSuccess((User)data.getSerializable(GetUserTask.USER_KEY));
        }

    }

    // LogoutHandler for Main
    private class GetLogoutHandler extends NotifyHandler {
        public GetLogoutHandler(NotifyObserver observer) {
            super(observer);
        }
    }
}
