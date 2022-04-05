package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.ResultsPage;

public class DynamoStatusDAO implements StatusDAO {
    private static final String feedTableName = "feed";
    private static final String storyTableName = "story";

    private static final String aliasAttribute = "alias";
    private static final String timestampAttribute = "timestamp";
    private static final String postAttribute = "post";
    private static final String urlsAttribute = "urls";
    private  static final String mentionsAttribute = "mentions";

    // DynamoDB client
    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    /**
     * Gets the statuses in a users story. Uses
     * information in the request object to limit the number of statuses returned and to return the
     * next set of statuses after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose statuses are to be returned and any
     *                other information required to satisfy the request.
     * @return the statuses.
     */
    public ResultsPage getStory(PagedRequest request) {
        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#alias", aliasAttribute);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":alias", new AttributeValue().withS(request.getAlias()));


        QueryRequest queryRequest = new QueryRequest()
                .withTableName(storyTableName)
                .withKeyConditionExpression("#alias = :alias")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit());

        if (isNonEmptyString(request.getLastItem())) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(aliasAttribute, new AttributeValue().withS(request.getAlias()));
            startKey.put(timestampAttribute, new AttributeValue().withS(request.getLastItem()));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);

        ResultsPage resultsPage = new ResultsPage();

        List<Map<String, AttributeValue>> items = queryResult.getItems();
        System.out.println("items = " + items.toString());

        for (Map<String, AttributeValue> item : items){
            String statusGot = item.get(statusAttribute).getS();
            resultsPage.addValue(statusGot);
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey != null) {
            resultsPage.setLastKey(lastKey.get(statusAttribute).getS());
        }

        System.out.println("Get story returning: " + resultsPage.getValues().toString());

        return resultsPage;
    }

    /**
     * Gets the statuses in a users feed. Uses
     * information in the request object to limit the number of statuses returned and to return the
     * next set of statuses after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose statuses are to be returned and any
     *                other information required to satisfy the request.
     * @return the statuses.
     */
    public ResultsPage getFeed(PagedRequest request) {

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#alias", aliasAttribute);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":alias", new AttributeValue().withS(request.getAlias()));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(feedTableName)
                .withKeyConditionExpression("#alias = :alias")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit());

        if (isNonEmptyString(request.getLastItem())) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(aliasAttribute, new AttributeValue().withS(request.getAlias()));
            startKey.put(statusAttribute, new AttributeValue().withS(request.getLastItem()));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);

        ResultsPage resultsPage = new ResultsPage();

        List<Map<String, AttributeValue>> items = queryResult.getItems();
        System.out.println("items = " + items.toString());

        for (Map<String, AttributeValue> item : items){
            String statusGot = item.get(statusAttribute).getS();
            resultsPage.addValue(statusGot);
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey != null) {
            resultsPage.setLastKey(lastKey.get(statusAttribute).getS());
        }

        System.out.println("Get feed returning: " + resultsPage.getValues().toString());

        return resultsPage;
    }

    @Override
    public void addStatusToFeed(String user, Status status) {
        Table table = dynamoDB.getTable(feedTableName);

        uploadStatus(user, status, table);
    }

    @Override
    public void addStatusToStory(String user, Status status) {
        Table table = dynamoDB.getTable(storyTableName);

        uploadStatus(user, status, table);
    }

    private void uploadStatus(String user, Status status, Table table) {
        Date currentDate = new Date();
        String currentTime = String.valueOf(currentDate.getTime());

        Item item = new Item()
                .withPrimaryKey(aliasAttribute, user, timestampAttribute, currentTime)
                .withString(postAttribute, status.getPost())
                .withList(mentionsAttribute, status.getMentions())
                .withList(urlsAttribute, status.getUrls());

        table.putItem(item);
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }


    private int getStatusesStartingIndex(String lastStatus, List<Status> allStatuses) {

        int followeesIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i).getPost())) {
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
    private List<Status> getDummyStatuses() {
        return getFakeData().getFakeStatuses();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy data.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    private FakeData getFakeData() {
        return new FakeData();
    }
}
