package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.interfaces.AuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;

public interface DAOFactory {
    StatusDAO getStatusDAO();
    FollowDAO getFollowDao();
    UserDAO getUserDAO();
    AuthtokenDAO getAuthtokenDAO();
}
