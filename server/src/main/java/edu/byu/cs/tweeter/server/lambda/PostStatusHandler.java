package edu.byu.cs.tweeter.server.lambda;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.CreateStatusRequest;
import edu.byu.cs.tweeter.model.net.response.CreateStatusResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostStatusHandler implements RequestHandler<CreateStatusRequest, CreateStatusResponse> {
    @Override
    public CreateStatusResponse handleRequest(CreateStatusRequest request, Context context) {
        StatusService statusService= new StatusService(new DAOFactory());
        return statusService.postStatus(request);
    }
}
