package dtos;

import entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
private User user;

    public UserDTO(User user) {
        this.user = user;
    }

    public static List<UserDTO> getDtos(List<User> users){
        List<UserDTO> userDTOS = new ArrayList();
        users.forEach(user->userDTOS.add(new UserDTO(user)));
        return userDTOS;
    }
}
