package edu.byu.cs.tweeter.server.lambda;


import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

class GetFollowerHandler implements RequestHandler<FollowingRequest, FollowingResponse> {
    @Override
    public FollowingResponse handleRequest(FollowingRequest input, com.amazonaws.services.lambda.runtime.Context context) {
        FollowService service = new FollowService(new DAOFactory());
        return service.getFollowers(input);
    }
}
