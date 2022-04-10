package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.dao.FactoryManager;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAO;
import edu.byu.cs.tweeter.server.model.JobQDTO;
import edu.byu.cs.tweeter.util.JsonSerializer;

public class JobHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent input, Context context) {

        JobQDTO jobQDTO;
        StatusDAO statusDAO = FactoryManager.getDAOFactory().getStatusDAO();

        for (SQSEvent.SQSMessage msg : input.getRecords()) {
            jobQDTO = JsonSerializer.deserialize(msg.getBody(), JobQDTO.class);

            statusDAO.addStatusBatchToFeed(jobQDTO.getFollowers(), jobQDTO.getStatus());
        }
        System.out.println("Job finished");
        return null;
    }
}
