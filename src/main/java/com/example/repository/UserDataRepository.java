package com.example.repository;

import com.example.model.UserData;

/**
 * Data access interface for the USER_DATA database object
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 23/07/16
 */
public interface UserDataRepository {

    /**
     * Finds the user data object based on the given user id
     *
     * @param id the user's id
     * @return the user's data object
     */
    UserData findById(Long id);

    /**
     * Saves the given user data object. When the given user data
     * object's id value already exists in the database, then the
     * data is updated based on the given object (the sector values
     * are firstly removed from the database and the new values are
     * inserted). Otherwise the user data object is simply saved and
     * inserted to the database.
     *
     * @param userData user data object
     * @return saved user data
     */
    UserData save(UserData userData);
}
