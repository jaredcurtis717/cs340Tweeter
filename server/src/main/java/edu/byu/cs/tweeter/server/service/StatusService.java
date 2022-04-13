package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoStatusDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoUserDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import edu.byu.cs.tweeter.server.model.PostQDTO;
import edu.byu.cs.tweeter.util.AlmostStatus;
import edu.byu.cs.tweeter.util.DataAccessException;
import edu.byu.cs.tweeter.util.JsonSerializer;
import edu.byu.cs.tweeter.util.ResultsPage;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class StatusService {
    private static final String postQURL = "https://sqs.us-west-2.amazonaws.com/477351221852/postQ";

    /**
     * Returns the feed of the given user. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link DynamoFollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public StatusesResponse getFeed(PagedRequest request) {
        if(request.getAlias() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request needs to have a positive limit");
        }
        getAuthtokenDAO().validate(request.getAuthToken());

        ResultsPage<AlmostStatus> resultsPage = getStatusesDAO().getFeed(request);

        List<Status> feedStatuses = new ArrayList<>();

        System.out.println("Got Feed.  Almost statuses are: " + resultsPage.getValues().toString());
        for(AlmostStatus sta : resultsPage.getValues()){
            try{
                feedStatuses.add(new Status(sta.getPost(),
                        getUserDAO().getUser(sta.getUser()),
                        sta.getDate(),
                        sta.getUrls(),
                        sta.getMentions()
                ));
            } catch (DataAccessException e) {
                throw new RuntimeException("[BadRequest] couldn't get user of one of the statusus in the feed");
            }
        }
        System.out.println("Created list of statuses, about to return.  Statuses: " + feedStatuses.toString());
        System.out.println("Has last key? " + resultsPage.hasLastKey());
        return new StatusesResponse(feedStatuses, resultsPage.hasLastKey());
    }


    /**
     * Returns the story of the given user. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request. Uses the {@link DynamoFollowDAO} to
     * get the followers.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers.
     */
    public StatusesResponse getStory(PagedRequest request) {
        if(request.getAlias() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request needs to have a positive limit");
        }
        getAuthtokenDAO().validate(request.getAuthToken());

        ResultsPage<AlmostStatus> resultsPage = getStatusesDAO().getStory(request);
        List<Status> storyStatuses = new ArrayList<>();

        System.out.println("Got almostStatuses" + resultsPage.getValues().toString());

        for(AlmostStatus sta : resultsPage.getValues()){
            try{
                storyStatuses.add(new Status(sta.getPost(),
                        getUserDAO().getUser(sta.getUser()),
                        sta.getDate(),
                        sta.getUrls(),
                        sta.getMentions()
                ));
            } catch (DataAccessException e) {
                throw new RuntimeException("[BadRequest] couldn't get user of one of the statusus in the feed");
            }
        }
        System.out.println("created statuses, about to return.  Statuses: " + storyStatuses.toString());

        return new StatusesResponse(storyStatuses, resultsPage.hasLastKey());
    }

    public Response postStatus(PostStatusRequest request) {
        if(request.getAuthToken() == null) {
            throw new RuntimeException("[BadRequest] invalid authToken");
        } else if(request.getStatus() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a status to post");
        }
        String currentUser = getAuthtokenDAO().validate(request.getAuthToken());

        PostQDTO postQDTO = new PostQDTO(currentUser, request.getStatus());

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(postQURL)
                .withMessageBody(JsonSerializer.serialize(postQDTO));

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

        String msgId = send_msg_result.getMessageId();
        System.out.println("Message ID: " + msgId);

        return new Response(true);

        /*


        getStatusesDAO().addStatusToStory(currentUser, request.getStatus());

        List<String> followers = getFollowDAO()
                .getFollowers(new PagedRequest(request.getAuthToken(), currentUser, 1000000, null))
                .getValues();

        for(String follower : followers){
            getStatusesDAO().addStatusToFeed(follower, request.getStatus());
        }
        return new Response(true);
         */
    }

    /**
     * Returns an instance of {@link DynamoFollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    StatusDAO getStatusesDAO() {
        return new DynamoStatusDAO();
    }

    UserDAO getUserDAO(){return new DynamoUserDAO();}

    AuthtokenDAO getAuthtokenDAO(){return new DynamoAuthtokenDAO();}

    FollowDAO getFollowDAO(){return new DynamoFollowDAO();}
}