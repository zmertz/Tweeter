package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask {

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        String lastItemAlias;
        if (getLastItem() == null) {
            lastItemAlias = null;
        } else {
            lastItemAlias = getLastItem().getAlias();
        }
        FollowingRequest followingRequest = new FollowingRequest(getFakeData().getAuthToken(), getTargetUser().getAlias(), getLimit(), lastItemAlias);
        Pair<List<User>, Boolean> returnPair = null;
        try {
            FollowingResponse followingResponse = new ServerFacade().getFollowees(followingRequest, "/getfollowing");
            List<User> userList = followingResponse.getFollowees();
            boolean hasMorePages = followingResponse.getHasMorePages();
            returnPair = new Pair<>(userList, hasMorePages);
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (TweeterRemoteException e) {
            e.printStackTrace();
        }
        return returnPair;
        //return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
    }
}
