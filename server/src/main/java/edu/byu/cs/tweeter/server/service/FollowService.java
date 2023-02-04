package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.IDAOFactory;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {
    private IDAOFactory dao;

    public FollowService(DAOFactory dao) {
        this.dao = dao;
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowing(FollowingRequest request) {
        return dao.getFollowDAO().getFollowing(request);
    }

    /**
     * Returns the users that are following the specified user. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followers.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers.
     */
    public FollowingResponse getFollowers(FollowingRequest request) {
        return dao.getFollowDAO().getFollowers(request);
    }

    public FollowResponse follow(FollowRequest request) {
        return dao.getFollowDAO().follow(request);
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        return dao.getFollowDAO().unfollow(request);
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        return dao.getFollowDAO().getFollowingCount(request);
    }

    public FollowerCountResponse getFollowerCount(FollowerCountRequest request) {
        return dao.getFollowDAO().getFollowerCount(request);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        return dao.getFollowDAO().isFollower(request);
    }

}
