package com.fitlife.service;

import com.fitlife.dto.UserCreationRequest;
import com.fitlife.dto.UserResponse;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);
}
