package edu.byu.cs.server.lambda;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.service.UserService;

public class RegisterHandler implements RequestHandler<RegisterRequest, RegisterResponse> {
    @Override
    public RegisterResponse handleRequest(RegisterRequest registerRequest, Context context) {
        UserService userService = null;
        try {
            if (registerRequest.getUsername().equals("")
                    || registerRequest.getPassword().equals("")) {
                throw new IOException("Bad Request");
            }
        } catch (IOException exception) {
            exception.getStackTrace();
        }
        userService = new UserService(new DAOFactory());
        return userService.register(registerRequest);
    }
}
