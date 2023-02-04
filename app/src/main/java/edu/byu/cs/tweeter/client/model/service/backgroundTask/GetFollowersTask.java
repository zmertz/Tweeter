package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        String lastItemAlias;
        if (getLastItem() == null) {
            lastItemAlias = null;
        } else {
            lastItemAlias = getLastItem().getAlias();
        }
        Pair<List<User>, Boolean> ret = null;
        FollowerRequest followerRequest = new FollowerRequest(getFakeData().getAuthToken(), getTargetUser().getAlias(),
                getLimit(), lastItemAlias);
        try {
            FollowerResponse followerResponse = new ServerFacade().getFollowers(followerRequest, "/getfollower");
            List<User> userList = followerResponse.getFollowers();
            boolean hasMorePages = followerResponse.getHasMorePages();
            ret = new Pair<>(userList, hasMorePages);
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (TweeterRemoteException e) {
            e.printStackTrace();
        }
        return ret;
        //return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
    }
}
