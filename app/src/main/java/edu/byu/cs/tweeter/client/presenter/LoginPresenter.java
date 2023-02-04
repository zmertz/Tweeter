package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends Presenter {

    public LoginPresenter(View view) {
        super(view);
    }

    public void presenterLogin(String alias, String password) { //method sends to service to login
        validateLogin(alias, password);
        new UserService().login(alias, password, new LoginPresenter.LoginObserver());
    }

    public void validateLogin(String alias, String password) {
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public class LoginObserver extends Presenter.BaseObserver implements UserService.GetLoginObserver {
        @Override
        public void handleSuccess(AuthToken authtoken, User user) {
            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authtoken);
            view.goToUser(user);
            view.displayInfo("Hello " + user.getName());
        }
    }
}
