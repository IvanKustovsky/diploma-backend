package com.e2rent.user_service.service;

import com.e2rent.user_service.dto.UserDto;

public interface IUserService {

    /**
     *
     * @param userDto - UserDto Object
     */
    void registerUser(UserDto userDto);

    /**
     *
     * @param email - Input Email
     * @return User details based on a given email
     */
    UserDto fetchUser(String email);

    /**
     *
     * @param userDto - UserDto Object
     * @param authorizationToken - Authorization token
     * @return boolean indicating if the update of User is successful or not
     */
    boolean updateUser(UserDto userDto, String authorizationToken);

    /**
     *
     * @param email - Input Email
     * @return boolean indicating if the delete of User is successful or not
     */
    boolean deleteUser(String email);
}
