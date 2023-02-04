package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.AuthenticateTask;
import edu.byu.cs.tweeter.client.model.service.observer.AuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthHandler<T extends AuthObserver> extends BackgroundTaskHandler<T> {
    public AuthHandler(T observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Bundle bundle) {
        User loggedInUser = (User) bundle.getSerializable(AuthenticateTask.USER_KEY);
        AuthToken authToken = (AuthToken) bundle.getSerializable(AuthenticateTask.AUTH_TOKEN_KEY);
        Cache.getInstance().setCurrUser(loggedInUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);
        observer.handleSuccess(authToken, loggedInUser);
    }
}
