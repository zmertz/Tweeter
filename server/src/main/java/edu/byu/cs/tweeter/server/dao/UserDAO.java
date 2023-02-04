package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import edu.byu.cs.tweeter.server.dao.interfaces.IUserDAO;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public class UserDAO implements IUserDAO {

    public Table connect() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable("users");

        return table;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Table table = connect();

        Item item;

        item = table.getItem("userAlias", request.getUsername());
        if (item == null) {
            return new LoginResponse("User not found");
        }
        else if (!item.get("password").toString().equals(performHash(request.getPassword()))) {
            return new LoginResponse("Invalid password");
        }

        User user = new User(item.get("firstName").toString(), item.get("lastName").toString(),
                item.get("userAlias").toString(), item.get("password").toString(),
                item.get("imageUrl").toString(), item.get("token").toString());

        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();

        Long date = Calendar.getInstance().getTimeInMillis();
        AuthToken newToken = new AuthToken(token, String.valueOf(date));

        return new LoginResponse(user, newToken);
    }

    @Override
    public LogoutResponse logout(LogoutRequest request) {
        return new LogoutResponse(true, null);
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        Table table = connect();

        Item check = table.getItem("userAlias", request.getUsername());
        if (check != null) {
            return new RegisterResponse(false, "User already exists", null, null);
        }

        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        Long date = Calendar.getInstance().getTimeInMillis();

        Item item = new Item()
                .withPrimaryKey("userAlias", request.getUsername())
                .withString("firstName", request.getFirstName())
                .withString("lastName", request.getLastName())
                .withString("password", performHash(request.getPassword()))
                .withString("token", token)
                .withString("imageUrl", request.getImage());
        table.putItem(item);

        User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(),
                request.getPassword(), request.getImage(), token);

        return new RegisterResponse(true, null, user, new AuthToken(token, String.valueOf(date)));
    }

    @Override
    public GetUserResponse getUser(GetUserRequest request) {
        Table table = connect();

        Item item = table.getItem("userAlias", request.getUserAlias());
        if (item == null) {
            return new GetUserResponse("Unable to find user");
        }
        User user = new User(item.get("firstName").toString(), item.get("lastName").toString()
                ,item.get("userAlias").toString(), item.get("password").toString(),
                item.get("imageUrl").toString(), item.get("token").toString());

        System.out.println(user);
        return new GetUserResponse(user);
    }

    public String performHash(String password) {
        String key = "secret_key";
        String salt = "salting_the_key";
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(password.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println( e.toString());
        }
        return null;
    }
}
