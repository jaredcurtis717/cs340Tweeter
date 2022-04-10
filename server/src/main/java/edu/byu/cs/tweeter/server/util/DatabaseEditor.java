package edu.byu.cs.tweeter.server.util;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.FactoryManager;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;

public class DatabaseEditor {


    public static void main(String[] args){
        fillDatabase();
    }



    private final static String FOLLOW_TARGET = "@jc";
    private final static int NUM_USERS = 10000;

    public static void fillDatabase() {
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
}
