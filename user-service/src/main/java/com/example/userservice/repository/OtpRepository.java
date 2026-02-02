package com.example.userservice.repository;

import com.example.userservice.entity.Otp;
import com.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,Long> {

    Optional<Otp> findByUser(User user);}
