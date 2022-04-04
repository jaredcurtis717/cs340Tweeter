package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;

import java.util.Date;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.AuthTokenRequest;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthtokenDAO;
import edu.byu.cs.tweeter.util.DataAccessException;

public class DynamoAuthtokenDAO implements AuthtokenDAO {
    // DynamoDB client
    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static final String tableName = "authenticationTokens";
    private static final String token = "token";
    private static final String timestamp = "timestamp";

    private static final long TIMEOUT = 600000;

    @Override
    public AuthToken newAuthtoken() {
        String tok = UUID.randomUUID().toString();

        Date currentDate = new Date();
        long milliSeconds = currentDate.getTime();
        String time = String.valueOf(milliSeconds);

        Table table = dynamoDB.getTable(tableName);

        Item item = new Item()
                .withPrimaryKey(token, tok)
                .withString(timestamp,time);
        table.putItem(item);

        return new AuthToken(tok, time);
    }

    @Override
    public void validate(AuthToken authToken) {
        Table table = dynamoDB.getTable(tableName);
        Item item = table.getItem(token, authToken.getToken());
        if (item == null){
            throw new RuntimeException("[BadRequest] Invalid Authtoken");
        }

        System.out.println("got token from table");

        String itemTimeStamp = item.getString(timestamp);
        long timeToCheck = Long.parseLong(itemTimeStamp);
        Date currentDate = new Date();
        long milliSeconds = currentDate.getTime();

        if (milliSeconds - timeToCheck > TIMEOUT){
            throw new RuntimeException("[BadRequest] Authtoken timed out");
        }

        System.out.println("Checked timeout");

        String newTime = String.valueOf(milliSeconds);

        Item updateItem = new Item()
                .withPrimaryKey(token, authToken.getToken())
                .withString(timestamp, newTime);

        System.out.println("Created new item to place in table");

        table.putItem(updateItem);

        System.out.println("Placed item in table");
    }

    @Override
    public void removeToken(AuthTokenRequest request) throws DataAccessException {

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey(token, request.getAuthToken().getToken()));

        Table table = dynamoDB.getTable(tableName);
        // Conditional delete (we expect this to fail)

        try {
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded");
        }
        catch (Exception e) {
            System.err.println("Unable to delete item: " + request.getAuthToken().getToken() + " "
                    + request.getAuthToken().getDatetime());
            System.err.println(e.getMessage());
            throw new DataAccessException("Unable to delete item");
        }
    }
}
