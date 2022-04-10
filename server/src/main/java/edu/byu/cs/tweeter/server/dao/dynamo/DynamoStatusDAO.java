package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAO;
import edu.byu.cs.tweeter.util.AlmostStatus;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.ResultsPage;

public class DynamoStatusDAO implements StatusDAO {
    private static final String feedTableName = "feed";
    private static final String storyTableName = "story";

    private static final String aliasAttribute = "alias";
    private static final String statusOwnerAttribute = "status_owner";
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
    public ResultsPage<AlmostStatus> getStory(PagedRequest request) {
        System.out.println("Getting story for " + request.getAlias());

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

        System.out.println("About to query");
        QueryResult queryResult = amazonDynamoDB.query(queryRequest);

        ResultsPage<AlmostStatus> resultsPage = new ResultsPage<AlmostStatus>();
        System.out.println("Query made");

        List<Map<String, AttributeValue>> items = queryResult.getItems();
        System.out.println("items = " + items.toString());

        for (Map<String, AttributeValue> item : items){
            String post = item.get(postAttribute).getS();
            String poster = item.get(aliasAttribute).getS();
            String dateTime = item.get(timestampAttribute).getS();
            List<String> urls = item.get(urlsAttribute).getSS(); //SS vs NS?
            List<String> mentions = item.get(mentionsAttribute).getSS();

            if (urls == null){
                urls = new ArrayList<>();
            }
            if (mentions == null){
                mentions = new ArrayList<>();
            }

            resultsPage.addValue(new AlmostStatus(post, poster, dateTime, urls, mentions));
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (resultsPage.getValues().size() <=0){
            lastKey = null;
        }
        if (lastKey != null) {
            resultsPage.setLastKey(lastKey.get(timestampAttribute).getS());
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
    public ResultsPage<AlmostStatus> getFeed(PagedRequest request) {

        System.out.println("Getting Feed for" + request.getAlias());

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
            startKey.put(timestampAttribute, new AttributeValue().withS(request.getLastItem()));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        System.out.println("About to query");
        QueryResult queryResult = amazonDynamoDB.query(queryRequest);

        ResultsPage<AlmostStatus> resultsPage = new ResultsPage<>();

        System.out.println("Query made");

        List<Map<String, AttributeValue>> items = queryResult.getItems();
        System.out.println("items = " + items.toString());

        for (Map<String, AttributeValue> item : items){
            String post = item.get(postAttribute).getS();
            String poster = item.get(statusOwnerAttribute).getS();
            String dateTime = item.get(timestampAttribute).getS();
            List<String> urls = item.get(urlsAttribute).getSS(); //SS vs NS?
            List<String> mentions = item.get(mentionsAttribute).getSS();

            if (urls == null){
                urls = new ArrayList<>();
            }
            if (mentions == null){
                mentions = new ArrayList<>();
            }
            resultsPage.addValue(new AlmostStatus(post, poster, dateTime, urls, mentions));
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (resultsPage.getValues().size() <=0){
            lastKey = null;
        }
        if (lastKey != null) {
            resultsPage.setLastKey(lastKey.get(timestampAttribute).getS());
        }


        System.out.println("Get feed returning: " + resultsPage.getValues().toString());

        return resultsPage;
    }

    @Override
    public void addStatusToFeed(String user, Status status) {
        Table table = dynamoDB.getTable(feedTableName);

        Item itemToUpload = getItemToUpload(user, status);
        itemToUpload.withString(statusOwnerAttribute, status.getUser().getAlias());

        table.putItem(itemToUpload);
    }

    @Override
    public void addStatusBatchToFeed(List<String> followers, Status status) {
        TableWriteItems items = new TableWriteItems(feedTableName);

        // Add each user into the TableWriteItems object
        for (String follower : followers) {
            Item item = new Item()
                    .withPrimaryKey(aliasAttribute, follower, timestampAttribute, status.getDate())
                    .withString(postAttribute, status.getPost())
                    .withList(mentionsAttribute, status.getMentions())
                    .withList(urlsAttribute, status.getUrls());
            items.addItemToPut(item);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being thrown
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems(feedTableName);
            }
        }

        // Write any leftover items
        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }
    }

    private void loopBatchWrite(TableWriteItems items) {

        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this example
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);
        System.out.println("Wrote feed Batch");

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
            System.out.println("Wrote extra feed items");
        }
    }

    @Override
    public void addStatusToStory(String user, Status status) {
        Table table = dynamoDB.getTable(storyTableName);

        table.putItem(getItemToUpload(user, status));
    }

    private Item getItemToUpload(String user, Status status) {
        Date date = new Date();
        Long milliseconds = date.getTime();

        Item item = new Item()
                .withPrimaryKey(aliasAttribute, user, timestampAttribute, milliseconds)
                .withString(postAttribute, status.getPost())
                .withList(mentionsAttribute, status.getMentions())
                .withList(urlsAttribute, status.getUrls());

        return item;
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
