package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import java.security.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthtokenDAO;

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
}
