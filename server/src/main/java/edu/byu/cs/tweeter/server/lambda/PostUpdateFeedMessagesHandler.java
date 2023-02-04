package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostUpdateFeedMessagesHandler implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        StatusService statusService = new StatusService(new DAOFactory());
        for(int i = 0; i < event.getRecords().size(); i++) {
            statusService.createStatusBatches(event.getRecords().get(i).getBody());
        }
        return null;
    }
}
