package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import edu.byu.cs.tweeter.util.DataAccessException;

public class DynamoUserDAO implements UserDAO {

    // DynamoDB client
    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static final String tableName = "user";
    private static final String passwordTableName = "tweeterpasswords";

    private static final String alias = "alias";
    private static final String firstName = "first_name";
    private static final String lastName = "last_name";
    private static final String image = "image_url";
    private static final String password = "password";
    private static final String salt = "salt";

    @Override
    public User getUser(String userHandle) {
        Table table = dynamoDB.getTable(tableName);

        Item item = table.getItem(alias, userHandle);

        if (item == null){
            throw new RuntimeException("[BadRequest] user doesn't exist");
        }

        System.out.println("Found user: " + item.toString());
        return new User(item.getString(firstName), item.getString(lastName), userHandle, item.getString(image));
    }

    @Override
    public User registerUser(RegisterRequest request) throws DataAccessException {
        System.out.println("Putting new user: " + request.getUsername());
        Table table = dynamoDB.getTable(tableName);

        String imageURLKey = uploadS3(request.getImage());
        String imageURL = s3Client.getUrl(bucketName, imageURLKey).toString();

        System.out.println("uploaded image");

        String salty = getSalt();
        String securePassword = getSecurePassword(request.getPassword(), salty);

        Item item = new Item()
                .withPrimaryKey(alias, request.getUsername())
                .withString(firstName, request.getFirstName())
                .withString(lastName, request.getLastName())
                .withString(salt, salty)
                .withString(image, imageURL);

        System.out.println(item.toString());
        table.putItem(item);
        System.out.println("User uploaded");

        Table passwordTable = dynamoDB.getTable(passwordTableName);
        System.out.println("Switch to password table");
        item = new Item()
                .withPrimaryKey(alias, request.getUsername(), password, securePassword);
        System.out.println("Created password item");
        passwordTable.putItem(item);

        System.out.println("Password uploaded");

        User returnUser = new User(request.getFirstName(), request.getLastName(), request.getUsername(), imageURL);

        System.out.println("registerUser returning: " + returnUser.toString());

        return returnUser;
    }

    @Override
    public User login(LoginRequest request) throws DataAccessException {
        Table table = dynamoDB.getTable(tableName);

        Item item = table.getItem(alias, request.getUsername());

        if (item == null){
            throw new RuntimeException("[BadRequest] user doesn't exist");
        }

        Table passwordTable = dynamoDB.getTable(passwordTableName);

        String userSalt = item.getString(salt);
        String passwordToCheck = getSecurePassword(request.getPassword(), userSalt);

        Item passItem = passwordTable.getItem(alias, request.getUsername(), password, passwordToCheck);
        if (passItem == null){
            throw new RuntimeException("[BadRequest] invalid username/password");
        }

        return new User(item.getString(firstName), item.getString(lastName), item.getString(alias), item.getString(image));
    }

    Regions clientRegion = Regions.US_WEST_2;
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withRegion(clientRegion)
            .build();
    private static final String bucketName = "cs340tweeterimagebucket";

    private String uploadS3(String obj) throws DataAccessException {
        String stringObjKeyName = UUID.randomUUID().toString() + ".png";
        try {
            // Upload a text string as a new object.
            byte[] bytes = Base64.getDecoder().decode(obj);
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            PutObjectRequest req = new PutObjectRequest(bucketName, stringObjKeyName, stream,
                    new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(req);

        } catch (SdkClientException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
            throw new DataAccessException("Couldn't upload image to s3");
        }
        return stringObjKeyName;
    }

    /*private String downLoadS3(String key) throws DataAccessException {
        S3Object fullObject;
        String userImage;
        try {
            // Get an object and print its contents.
            System.out.println("Downloading an object");
            fullObject = s3Client.getObject(new GetObjectRequest(bucketName, key));
            System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
            System.out.println("Content: ");
            userImage = inputStreamToString(fullObject.getObjectContent());
        } catch (SdkClientException e) {
            throw new DataAccessException("Failed to download image");
        }
        return userImage;
    }*/

    private String inputStreamToString(InputStream inputStream){
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }catch (Exception ex){
            throw new RuntimeException("Unable to read image from inputStream");
        }
        return textBuilder.toString();
    }

    private static String getSecurePassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "FAILED TO HASH PASSWORD";
    }

    private static String getSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return "FAILED TO GET SALT";
    }


}
