package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.CountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.NotifyHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedHandler;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.NotifyObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedInterfaceObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public interface GetFollowingObserver extends  PagedInterfaceObserver { }
    public interface GetFollowersObserver extends PagedInterfaceObserver { }
    public interface GetFollowersCountObserver extends CountObserver { }
    public interface GetFollowingCountObserver extends CountObserver { }
    public interface GetIsFollowerObserver extends ServiceObserver {
        void isFollowerSuccess(boolean isFollower);
    }
    public interface GetFollowObserver extends NotifyObserver { }
    public interface GetUnfollowObserver extends NotifyObserver { }

    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, GetFollowingObserver getFollowObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetFollowingHandler(getFollowObserver));
        ServiceHelper.runTask(getFollowingTask);
    }

    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, GetFollowersObserver getFollowObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new GetFollowersHandler(getFollowObserver));
        ServiceHelper.runTask(getFollowersTask);
    }

    public void getFollowerCount(AuthToken token, User selectedUser, GetFollowersCountObserver getFollowersObserver) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(token,
                selectedUser, new GetFollowersCountHandler(getFollowersObserver));
        ServiceHelper.runTask(followersCountTask);
    }

    public void getFollowingCount(AuthToken token, User selectedUser, GetFollowingCountObserver getFollowingCountObserver) {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(token,
                selectedUser, new GetFollowingCountHandler(getFollowingCountObserver));
        ServiceHelper.runTask(followingCountTask);
    }

    public void getIsFollower(AuthToken token, User currUser, User selectedUser, GetIsFollowerObserver getIsFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(token,
                currUser, selectedUser, new GetIsFollowerHandler(getIsFollowerObserver));
        ServiceHelper.runTask(isFollowerTask);
    }

    public void follow(AuthToken token, User selectedUser, GetFollowObserver getFollowObserver) {
        FollowTask followTask = new FollowTask(token,
                selectedUser, new GetFollowHandler(getFollowObserver));
        ServiceHelper.runTask(followTask);
    }

    public void unfollow(AuthToken token, User selectedUser, GetUnfollowObserver getUnfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(token,
                selectedUser, new GetUnfollowHandler(getUnfollowObserver));
        ServiceHelper.runTask(unfollowTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends PagedHandler {
        public GetFollowingHandler(PagedInterfaceObserver observer) {
            super(observer);
        }

    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends PagedHandler {
        public GetFollowersHandler(PagedInterfaceObserver observer) {
            super(observer);
        }

    }

    // GetFollowersCountHandler

    private class GetFollowersCountHandler extends CountHandler {
        public GetFollowersCountHandler(CountObserver observer) {
            super(observer);
        }
    }

    private class GetFollowingCountHandler extends CountHandler {
        public GetFollowingCountHandler(CountObserver observer) {
            super(observer);
        }
    }

    private class GetIsFollowerHandler<T extends GetIsFollowerObserver> extends BackgroundTaskHandler<T> {
        public GetIsFollowerHandler(T observer) {
            super(observer);
        }
        @Override
        protected void handleSuccessMessage(T observer, Bundle data) {
            observer.isFollowerSuccess(data.getBoolean(IsFollowerTask.SUCCESS_KEY));
        }
    }

    private class GetFollowHandler extends NotifyHandler {
        public GetFollowHandler(NotifyObserver observer) {
            super(observer);
        }
    }

    private class GetUnfollowHandler extends NotifyHandler {
        public GetUnfollowHandler(NotifyObserver observer) {
            super(observer);
        }
    }
}
