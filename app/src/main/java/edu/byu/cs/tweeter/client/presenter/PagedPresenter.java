package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {
    private static final int PAGE_SIZE = 10;
    User user;
    AuthToken token;
    T lastItem;
    boolean isLoading = false;
    boolean hasMorePages = true;
    View view;

    public PagedPresenter(View view, User user, AuthToken token) {
        super(view);
        this.view = view;
        this.user = user;
        this.token = token;
    }

    public interface View extends Presenter.View {
        void setLoading(boolean value);
    }

    public void loadMoreItems() {
        if (!isLoading && hasMorePages) {
            isLoading = true;
            view.setLoading(true);
            getItems(token, user, PAGE_SIZE, lastItem);
        }
    }

    public abstract void getItems(AuthToken authToken, User targetUser, int pageSize, T lastItem);

    public void goToUser(String alias) {
        view.displayInfo("Getting user's profile...");
        new UserService().getUser(token, alias, new UserObserver());
    }

    protected class PagedObserver extends Presenter.BaseObserver {
        @Override
        public void handleFailure(String message) {
            super.handleFailure(message);
            view.setLoading(false);
            isLoading = false;
        }

        @Override
        public void handleException(Exception ex) {
            super.handleException(ex);
            view.setLoading(false);
            isLoading = false;
        }
    }
    private class UserObserver extends Presenter.BaseObserver implements UserService.GetUserObserver {
        @Override
        public void handleSuccess(User user) {
            view.goToUser(user);
        }
    }
}
