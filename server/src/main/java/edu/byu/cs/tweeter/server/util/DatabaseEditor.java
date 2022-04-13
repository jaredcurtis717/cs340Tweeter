package edu.byu.cs.tweeter.server.util;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.FactoryManager;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;

public class DatabaseEditor {


    public static void main(String[] args){
        //fillDatabase();
        dropItemsInFeedTable("feed");
    }



    private final static String FOLLOW_TARGET = "@jc";
    private final static int NUM_USERS = 10000;

    private static void fillDatabase() {
        //WARNING: TO RUN, MAKE SURE YOUR TABLES ARE ADJUSTED TO 200+ WCU's

        // Get instance of DAOs by way of the Abstract Factory Pattern
        UserDAO userDAO = FactoryManager.getDAOFactory().getUserDAO();
        FollowDAO followDAO = FactoryManager.getDAOFactory().getFollowDao();

        List<String> followers = new ArrayList<>();
        List<User> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {

            String name = "Guy " + i;
            String alias = "@guy" + i;

            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            User user = new User(name, "Smith", alias,  name + ".com");
            users.add(user);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            followers.add(alias);
        }

        // Call the DAOs for the database logic
        if (users.size() > 0) {
            userDAO.addUserBatch(users);
        }
        if (followers.size() > 0) {
            followDAO.addFollowersBatch(followers, FOLLOW_TARGET);
        }
    }

    private static void dropItemsInFeedTable(String tableName){
        dropTable(tableName);
        createFeedTable(tableName);
    }

    private static void dropTable(String tableName) {
        // DynamoDB client
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion("us-west-2")
                .build();
        final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

        Table table = dynamoDB.getTable(tableName);

        try {
            System.out.println("Attempting to delete table; please wait...");
            table.delete();
            table.waitForDelete();
            System.out.print("Success.");

        }
        catch (Exception e) {
            System.err.println("Unable to delete table: ");
            System.err.println(e.getMessage());
        }
    }

    private static void createFeedTable(String tableName){
        // DynamoDB client
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion("us-west-2")
                .build();
        final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

        try {
            System.out.println("Attempting to create table; please wait...");
            Table table = dynamoDB.createTable(tableName,
                    Arrays.asList(new KeySchemaElement("alias", KeyType.HASH), // Partition
                            // key
                            new KeySchemaElement("timestamp", KeyType.RANGE)), // Sort key
                    Arrays.asList(new AttributeDefinition("alias", ScalarAttributeType.S),
                            new AttributeDefinition("timestamp", ScalarAttributeType.S)),
                    new ProvisionedThroughput(1L, 1L));
            table.waitForActive();
            System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

        }
        catch (Exception e) {
            System.err.println("Unable to create table: ");
            System.err.println(e.getMessage());
        }
    }

}
