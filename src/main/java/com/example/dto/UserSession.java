package com.example.dto;

import com.example.model.UserData;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Wrapper class, which holds the user's session data.
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 23/07/16
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class UserSession implements Serializable {

    private UserData userData;


    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
