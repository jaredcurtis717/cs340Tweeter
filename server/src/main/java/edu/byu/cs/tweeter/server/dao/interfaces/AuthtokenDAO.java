package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.AuthTokenRequest;
import edu.byu.cs.tweeter.util.DataAccessException;

public interface AuthtokenDAO {
    AuthToken newAuthtoken();
    void validate(AuthToken token);
    void removeToken(AuthTokenRequest request) throws DataAccessException;
}
