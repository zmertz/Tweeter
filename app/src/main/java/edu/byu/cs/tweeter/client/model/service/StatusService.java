package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.NotifyHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedHandler;
import edu.byu.cs.tweeter.client.model.service.observer.NotifyObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedInterfaceObserver;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.story.StoryFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    public interface GetFeedObserver extends PagedInterfaceObserver { }
    public interface GetStoryObserver extends PagedInterfaceObserver { }
    public interface GetPostObserver extends NotifyObserver{}

    public void getFeed(AuthToken token, User user, int pageSize, Status lastStatus, GetFeedObserver getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(token,
                user, pageSize, lastStatus, new GetFeedHandler(getFeedObserver));
        ServiceHelper.runTask(getFeedTask);
    }

    public void getStory(AuthToken token, User user, int pageSize, Status lastStatus, GetStoryObserver getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(token,
                user, pageSize, lastStatus, new GetStoryHandler(getStoryObserver));
        ServiceHelper.runTask(getStoryTask);
    }

    public void post(AuthToken token, Status newStatus, GetPostObserver getPostObserver) {
        PostStatusTask statusTask = new PostStatusTask(token,
                newStatus, new GetPostStatusHandler(getPostObserver));
        ServiceHelper.runTask(statusTask);
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends PagedHandler {
        public GetFeedHandler(PagedInterfaceObserver observer) {
            super(observer);
        }
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends PagedHandler {
        public GetStoryHandler(PagedInterfaceObserver observer) {
            super(observer);
        }
    }

    // PostStatusHandler
    private class GetPostStatusHandler extends NotifyHandler {
        public GetPostStatusHandler(NotifyObserver observer) {
            super(observer);
        }
    }
}
