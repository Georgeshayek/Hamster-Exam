package services;

import connection.Database;
import model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class
AuthenticationService {
    // session will be stored in memory  used to allow access to add and delete product
    private final Map<String,String> session;
    public AuthenticationService() {
        this.session= new HashMap<>();
    }
    public boolean checkIfAccountExist(String username){
        // checking in db if username exists
        try (
                Connection conn= Database.getConnection();
                PreparedStatement stmt= conn.prepareStatement("SELECT username from users WHERE username=?")

        ){
            stmt.setString(1,username);
            ResultSet rs=stmt.executeQuery();
            // if exists it will return true
            if(rs.next()){
                return  true;
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
        // else false
        return false;
    }
    public void registerUser(String username,String password){
        // we check if user exists
        if(checkIfAccountExist(username))
            System.out.println("user already exists");
        else{
            // if user doesnt exist we'll add a new user
            try (
                    Connection conn= Database.getConnection();
                    PreparedStatement stmt= conn.prepareStatement("INSERT INTO users(username, password) VALUES(?, ?)")
            ){
                stmt.setString(1,username);
                // we hash the password using MD5
                stmt.setString(2,hashPassword(password));
                stmt.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

    }
    // to login the user where it will return a session in case of success in order to be able method that needs authentication
    public String AuthenticateUser(String username,String password){
            // a basic authentication where we check if user and password exist to continue
        try (
                Connection conn= Database.getConnection();
                PreparedStatement stmt= conn.prepareStatement("SELECT username from users WHERE username=? AND password=?")

        ){
            stmt.setString(1,username);
            stmt.setString(2,hashPassword(password));
            ResultSet rs=stmt.executeQuery();
            // if exists the session is stored in the session map where the key is the session since its unique and the value being the username
            if(rs.next()){
                // we use the generate function which exists below to create a random uuid string
                String sessionId=generateSessionId();
                session.put(sessionId,username);
                System.out.println("logged in");
                return sessionId;
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
        // will return null and inform you of wrong credential
        System.out.println("wrong credentials");
        return null;
    }

    // this function is used to check if the session id exist  thus give access to methods that needs authorization
    public boolean checkIfUserIsAuthenticated(String sessionId){
        return session.containsKey(sessionId);
    }
    // logout will remove session from the session map
    public void logout(String sessionId){
        session.remove(sessionId);
    }
    // generate a random uuid string to use as a session
    public String generateSessionId(){
        return UUID.randomUUID().toString();
    }
    // MD5 algorithm for password hashing
    public String hashPassword(String password)  {
        String generatedPassword = null;
        try
        {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(password.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
