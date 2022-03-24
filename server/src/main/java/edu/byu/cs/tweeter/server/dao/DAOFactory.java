package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAO;

public interface DAOFactory {
    StatusDAO getStatusDAO();
    FollowDAO getFollowDao();
}
