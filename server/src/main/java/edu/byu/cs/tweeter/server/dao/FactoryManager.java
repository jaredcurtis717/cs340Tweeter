package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.dynamo.DynamoDAOFactory;

//Utility class that decides what DAO Factory is currently being used for the server
public class FactoryManager {
    private static final DAOFactory DAOFactoryImplementation = new DynamoDAOFactory();

    public static DAOFactory getDAOFactory(){
        return DAOFactoryImplementation;
    }
}
