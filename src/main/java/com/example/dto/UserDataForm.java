package com.example.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Represents the user data form JSON object
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 22/07/16
 */
public class UserDataForm implements Serializable {

    @NotEmpty
    @Size(max = 255)
    private String userFullName;

    @NotEmpty
    private Long[] userSectors;

    @AssertTrue
    private boolean agreeTerms;


    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Long[] getUserSectors() {
        return userSectors;
    }

    public void setUserSectors(Long[] userSectors) {
        this.userSectors = userSectors;
    }

    public boolean isAgreeTerms() {
        return agreeTerms;
    }

    public void setAgreeTerms(boolean agreeTerms) {
        this.agreeTerms = agreeTerms;
    }
}
