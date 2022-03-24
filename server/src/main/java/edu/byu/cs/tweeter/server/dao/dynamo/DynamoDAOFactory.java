package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAO;

public class DynamoDAOFactory implements DAOFactory {
    @Override
    public StatusDAO getStatusDAO() {
        return new DynamoStatusDAO();
    }

    @Override
    public FollowDAO getFollowDao() {
        return new DynamoFollowDAO();
    }
}
