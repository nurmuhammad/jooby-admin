package com.smartbox.admin.repo;

import com.smartbox.admin.model.User;

/**
 * User: Nurmuhammad
 * Date: 22/04/2020 13:09
 */
public class UserRepo extends EntityRepo<User> {

    public User createUserIfNotExist(String username, String password) {
        User user = findFirst("username=?1", username);
        if (user != null) return user;

        user = new User();
        user.setUsername(username);
        user.password(password);
        user.setStatus(true);
        getSession().save(user);
        return user;
    }
}
