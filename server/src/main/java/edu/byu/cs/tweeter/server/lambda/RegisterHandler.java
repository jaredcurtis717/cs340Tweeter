package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that registers a user and returns the user object and an auth code for
 * a successful login.
 */
public class RegisterHandler implements RequestHandler<RegisterRequest, LoginResponse> {
    @Override
    public LoginResponse handleRequest(RegisterRequest registerRequest, Context context) {
        UserService userService = new UserService(new DynamoDAOFactory());
        return userService.register(registerRequest);
    }
}
