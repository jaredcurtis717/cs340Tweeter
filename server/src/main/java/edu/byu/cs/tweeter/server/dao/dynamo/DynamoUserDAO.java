package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;

public class DynamoUserDAO implements UserDAO {

    // DynamoDB client
    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static final String tableName = "user";
    private static final String alias = "alias";
    private static final String firstName = "first_name";
    private static final String lastName = "last_name";
    private static final String image = "image_url";

    @Override
    public User getUser(String userHandle) {
        Table table = dynamoDB.getTable(tableName);

        Item item = table.getItem(alias, userHandle);

        return new User(item.getString(firstName), item.getString(lastName), userHandle, item.getString(image));
    }
}
