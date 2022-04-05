package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.ResultsPage;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class DynamoFollowDAO implements FollowDAO {

    private static final String followsTableName = "follows";
    private static final String followsTableIndexName = "follows_index";

    private static final String followerAttribute = "follower_handle";
    private static final String followeeAttribute = "followee_handle";

    // DynamoDB client
    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);



    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public ResultsPage getFollowees(PagedRequest request) {

        System.out.println("In getFollowees in DAO" + "\n" +
                request.getAlias() + " = alias \n" +
                request.getLastItem() + " = last item");

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#fol", followerAttribute);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":follower", new AttributeValue().withS(request.getAlias()));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(followsTableName)
                .withKeyConditionExpression("#fol = :follower")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit());

        if (isNonEmptyString(request.getLastItem())) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(followerAttribute, new AttributeValue().withS(request.getAlias()));
            startKey.put(followeeAttribute, new AttributeValue().withS(request.getLastItem()));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);

        ResultsPage resultsPage = new ResultsPage();

        List<Map<String, AttributeValue>> items = queryResult.getItems();
        System.out.println("items = " + items.toString());

        for (Map<String, AttributeValue> item : items){
            System.out.println("Followee item: " + item.toString());
            String followeeHandle = item.get(followeeAttribute).getS();
            resultsPage.addValue(followeeHandle);
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey != null) {
            resultsPage.setLastKey(lastKey.get(followeeAttribute).getS());
        }

        System.out.println("Get followees returning: " + resultsPage.getValues().toString());

        return resultsPage;

        /*
        // old dummy data response
        assert request.getLimit() > 0;
        assert request.getAlias() != null;

        List<User> allFollowees = getDummyFollowees();
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFolloweesStartingIndex(request.getLastItem(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new FollowingResponse(responseFollowees, hasMorePages);

         */
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followers returned and to return the
     * next set of followers after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followers are to be returned and any
     *                other information required to satisfy the request.
     * @return the followers.
     */
    public ResultsPage getFollowers(PagedRequest request) {
        System.out.println("In getFollowers in DAO" + "\n" +
                request.getAlias() + " = alias \n" +
                request.getLastItem() + " = last item");

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#followeeAttribute", followeeAttribute);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":followee", new AttributeValue().withS(request.getAlias()));

        //System.out.println("followsTableName = " + followsTableName);
        //System.out.println("followsTableIndex = " + followsTableIndexName);
        QueryRequest queryRequest = new QueryRequest()
                .withTableName(followsTableName)
                .withIndexName(followsTableIndexName)
                .withKeyConditionExpression("#followeeAttribute = :followee")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit());

        if (isNonEmptyString(request.getLastItem())) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(followeeAttribute, new AttributeValue().withS(request.getAlias()));
            startKey.put(followerAttribute, new AttributeValue().withS(request.getLastItem()));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);

        ResultsPage resultsPage = new ResultsPage();

        List<Map<String, AttributeValue>> items = queryResult.getItems();
        System.out.println("items = " + items.toString());

        for (Map<String, AttributeValue> item : items){
            System.out.println("Follower item: " + item.toString());
            String followerHandle = item.get(followerAttribute).getS();
            resultsPage.addValue(followerHandle);
        }

        System.out.println("Followers: " + resultsPage.getValues());

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey != null) {
            resultsPage.setLastKey(lastKey.get(followerAttribute).getS());
        }

        System.out.println("Get followers returning: " + resultsPage.getValues().toString());

        return resultsPage;

        //old dummy data implementation
        /*
        assert request.getLimit() > 0;
        assert request.getAlias() != null;

        List<User> allFollowees = getDummyFollowees();
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFolloweesStartingIndex(request.getLastItem(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }
        return new FollowingResponse(responseFollowees, hasMorePages);
        */

    }

    @Override
    public boolean follow(String currentUser, String targetAlias) {
        Table table = dynamoDB.getTable(followsTableName);
        Item item = new Item()
                .withPrimaryKey(followerAttribute, currentUser, followeeAttribute, targetAlias);
        table.putItem(item);
        return true;
    }

    @Override
    public boolean unfollow(String currentUser, String targetAlias) {
        Table table = dynamoDB.getTable(followsTableName);
        table.deleteItem(followerAttribute, currentUser, followeeAttribute, targetAlias);
        return true;
    }

    @Override
    public boolean isFollowing(String user, String targetUser) {
        Table table = dynamoDB.getTable(followsTableName);
        try{
            Item item = table.getItem(followerAttribute, user, followeeAttribute, targetUser);
            return (item != null);
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFolloweeAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyFollowees() {
        return getFakeData().getFakeUsers();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return new FakeData();
    }
}
