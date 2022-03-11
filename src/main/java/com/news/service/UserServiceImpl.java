package com.news.service;

import com.news.dto.UserDTO;
import com.news.model.User;
import com.news.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO getCurrentUser(Long id) {
        User user = userRepository.getById(id);

        UserDTO userDTO = new UserDTO(user);
        return userDTO;
    }
}
