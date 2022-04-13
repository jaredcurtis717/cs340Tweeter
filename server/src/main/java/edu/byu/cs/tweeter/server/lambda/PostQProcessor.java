package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import java.util.List;

import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.server.dao.FactoryManager;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.model.JobQDTO;
import edu.byu.cs.tweeter.server.model.PostQDTO;
import edu.byu.cs.tweeter.util.JsonSerializer;
import edu.byu.cs.tweeter.util.ResultsPage;

public class PostQProcessor implements RequestHandler<SQSEvent, Void> {
    private static final String jobsQURL = "https://sqs.us-west-2.amazonaws.com/477351221852/jobsQ";

    @Override
    public Void handleRequest(SQSEvent input, Context context) {
        System.out.println("Welcome to post Q handler");
        FollowDAO followDAO = FactoryManager.getDAOFactory().getFollowDao();
        PostQDTO post;
        for (SQSEvent.SQSMessage msg : input.getRecords()) {
            post = JsonSerializer.deserialize(msg.getBody(), PostQDTO.class);

            FactoryManager.getDAOFactory().getStatusDAO().addStatusToStory(post.getStatus().getUser().getAlias(), post.getStatus());

            /*
            System.out.println("Received: ");
            System.out.print("sender: " + post.getUserAlias());
            System.out.println("Post: " + post.getStatus());
             */

            ResultsPage<String> followersPage;
            String lastKey = null;

            do{
                followersPage = followDAO.getFollowers(post.getUserAlias(), lastKey, 25);
                lastKey = followersPage.getLastKey();

                JobQDTO jobQDTO = new JobQDTO(post.getStatus(), followersPage.getValues());

                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(jobsQURL)
                        .withMessageBody(JsonSerializer.serialize(jobQDTO));

                AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
                SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

                String msgId = send_msg_result.getMessageId();
                System.out.println("Message ID: " + msgId);

                System.out.println("Got batch: " + followersPage.getValues() + "\n"+
                        "With last key: " + lastKey);

            } while (lastKey != null);

            System.out.println("Finished Adding jobs");
        }
        return null;
    }
}
