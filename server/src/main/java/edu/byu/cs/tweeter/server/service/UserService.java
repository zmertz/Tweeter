package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.IDAOFactory;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public class UserService {
    private IDAOFactory dao;

    public UserService(IDAOFactory dao) {
        this.dao = dao;
    }

    public LoginResponse login(LoginRequest request) {
        LoginResponse loginResponse = dao.getUserDAO().login(request);

        if (loginResponse.isSuccess()) {
            dao.getAuthDAO().addToken(loginResponse.getAuthToken().getToken(),
                    loginResponse.getAuthToken().getDatetime());
        }
        return loginResponse;
    }

    public LogoutResponse logout(LogoutRequest request) {
        return dao.getUserDAO().logout(request);
    }

    public RegisterResponse register(RegisterRequest request) {
        RegisterResponse registerResponse = new DAOFactory().getUserDAO().register(request);
        dao.getAuthDAO().addToken(registerResponse.getAuthToken().getToken(), registerResponse.getAuthToken().getDatetime());
        return registerResponse;
    }

    public GetUserResponse getUser(GetUserRequest request) {
        return dao.getUserDAO().getUser(request);
    }

}
