package com.example.userservice.repository;

import com.example.userservice.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp,Long> {

}
