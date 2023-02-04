package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask {

    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        String lastItemPost;
        if (getLastItem() == null) {
            lastItemPost = null;
        } else {
            lastItemPost = getLastItem().post;
        }
        Pair<List<Status>, Boolean> ret = null;
        FeedRequest feedRequest = new FeedRequest(getFakeData().getAuthToken(),
                getTargetUser().getAlias(), getLimit(), lastItemPost);
        try {
            FeedResponse feedResponse = new ServerFacade().getFeed(feedRequest, "/getfeed");
            List<Status> statusList = feedResponse.getStatusList();
            boolean hasMorePages = feedResponse.getHasMorePages();
            ret = new Pair<>(statusList, hasMorePages);
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (TweeterRemoteException e) {
            e.printStackTrace();
        }
        return ret;
        //return getFakeData().getPageOfStatus(getLastItem(), getLimit());
    }
}
