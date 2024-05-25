package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.User;
import org.example.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user; //Storing User after login

    private List<User> userList;

    private static final String USERS_PATH = "app/src/main/java/org/example/localDb/users.json";

    //    object mapper is used to decerlize the user from json to java user
    private ObjectMapper objectMapper = new ObjectMapper();


    public UserBookingService(User user) throws IOException {
        this.user = user;
        File users = new File(USERS_PATH);
//        TypeRefernce means <User> is generic first make a type then add List because List is an generic type;
        userList = objectMapper.readValue(users, new TypeReference<List<User>>(){});
    }

    public Boolean loginUser(){
        Optional<User> founduser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(),user1.getHashedPassword());
        }).findFirst();
        return founduser.isEmpty();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile, userList);
    }
}
