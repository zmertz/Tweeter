package edu.byu.cs.tweeter.client.presenter;

import android.widget.ImageView;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends Presenter {


    public RegisterPresenter(RegisterPresenter.View view) {
        super(view);
    }

    public void presenterRegister(String firstName, String lastName, String alias, String password,
                                  String image) { //method sends to service to login
        new UserService().register(firstName, lastName, alias, password, image, new RegisterPresenter.RegisterObserver());
    }

    public void validateRegistration(String firstName, String lastName, String alias, String password,
                                     ImageView image) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (image.getDrawable() == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    public class RegisterObserver extends Presenter.BaseObserver implements UserService.GetRegisterObserver {
        @Override
        public void handleSuccess(AuthToken authtoken, User user) {
            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authtoken);
            view.goToUser(user);
            view.displayInfo("Hello " + user.getName());
        }
    }
}
