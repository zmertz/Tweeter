package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter {

    public interface View extends Presenter.View {
        void logoutUser();
        void getFollowersCount(int count);
        void getFollowingCount(int count);
        void displayIsFollower(boolean isFollower);
        void displayFollowResult();
        void displayUnfollowResult();
        void displayPostSuccess(String message);
    }

    private MainPresenter.View view;
    private User user;
    private User currentUser;
    private AuthToken token;

    public MainPresenter(MainPresenter.View view) {
        super(view);
        this.view = view;
    }

    private class LogoutObserver extends Presenter.BaseObserver implements UserService.GetLogoutObserver {
        @Override
        public void handleSuccess() {
            Cache.getInstance().clearCache();
            view.logoutUser();
        }
    }
    public void logoutPresenter() {
        view.displayInfo("Logging Out...");
        new UserService().logout(token, new LogoutObserver());
    }

    private class FollowerCountObserver extends Presenter.BaseObserver implements FollowService.GetFollowersCountObserver {
        @Override
        public void handleSuccess(int count) {
            view.getFollowersCount(count);
        }
    }
    public void followersCount(AuthToken token, User user) {
        new FollowService().getFollowerCount(token, user, new FollowerCountObserver());
    }

    private class FollowingCountObserver extends Presenter.BaseObserver implements FollowService.GetFollowingCountObserver {
        @Override
        public void handleSuccess(int count) {
            //followingCount = count;
            view.getFollowingCount(count);
        }
    }
    public void followingCount(AuthToken token, User user) {
        new FollowService().getFollowingCount(token, user, new FollowingCountObserver());
    }

    private class IsFollowerObserver extends Presenter.BaseObserver implements FollowService.GetIsFollowerObserver {
        @Override
        public void isFollowerSuccess(boolean isFollower) {
            view.displayIsFollower(isFollower);
        }
    }
    public void isFollower() {
        new FollowService().getIsFollower(token, currentUser, user, new IsFollowerObserver());
    }


    private class FollowObserver extends Presenter.BaseObserver implements FollowService.GetFollowObserver {
        @Override
        public void handleSuccess() {
            view.displayFollowResult();
        }
    }
    public void follow(AuthToken token, User user) {
        new FollowService().follow(token, user, new FollowObserver());
        view.displayInfo("Adding " + user.getName() + "...");
    }

    private class UnfollowObserver extends Presenter.BaseObserver implements FollowService.GetUnfollowObserver {
        @Override
        public void handleSuccess() {
            view.displayUnfollowResult();
        }
    }
    public void unfollow(AuthToken token, User user) {
        new FollowService().unfollow(token, user, new UnfollowObserver());
    }


    private class PostStatusObserver extends Presenter.BaseObserver implements StatusService.GetPostObserver {
        @Override
        public void handleSuccess() {
            view.displayPostSuccess("Successfully Posted!");
        }
    }
    public void postStatus(AuthToken token, Status newStatus) {
        view.displayInfo("Posting Status...");
        getStatusService().post(token, newStatus, new PostStatusObserver());
    }


}
