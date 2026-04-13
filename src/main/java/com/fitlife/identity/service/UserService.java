package com.fitlife.identity;

import com.fitlife.identity.controller.UserCreationRequest;
import com.fitlife.identity.dto.UserResponse;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);
}
