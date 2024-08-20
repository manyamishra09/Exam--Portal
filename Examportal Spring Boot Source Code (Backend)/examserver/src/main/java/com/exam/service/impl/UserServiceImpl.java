package com.exam.service.impl;

import com.exam.helper.UserFoundException;
import com.exam.helper.UserNotFoundException;
import com.exam.model.Role;
import com.exam.model.User;
import com.exam.model.UserRole;
import com.exam.repo.RoleRepository;
import com.exam.repo.UserRepository;
import com.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    //creating user
    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {


        User local = this.userRepository.findByUsername(user.getUsername());
        if (local != null) {
            System.out.println("User is already there !!");
            throw new UserFoundException();
        } else {
            //user create
            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);
            local = this.userRepository.save(user);

        }

        return local;
    }

    //getting user by username
    @Override
    public User getUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }

    @Override
    public User updateUser(String username, User updatedUser) throws UserNotFoundException{
        User existingUser = userRepository.findByUsername(username);
        if(existingUser == null){
        throw new UserNotFoundException("User not found with username: " + username);}
        else{

            // Update fields only if provided in the updatedUser object
            existingUser.setUsername(updatedUser.getUsername() != null ? updatedUser.getUsername() : existingUser.getUsername());
            existingUser.setFirstName(updatedUser.getFirstName() != null ? updatedUser.getFirstName() : existingUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName() != null ? updatedUser.getLastName() : existingUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail() != null ? updatedUser.getEmail() : existingUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone() != null ? updatedUser.getPhone() : existingUser.getPhone());

            // Update password if provided
//            if (updatedUser.getPassword() != null) {
//                existingUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
//            }
        }
        return this.userRepository.save(existingUser);
    }


    //updateUser


}
