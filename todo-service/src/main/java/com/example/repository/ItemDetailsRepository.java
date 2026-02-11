package com.example.repository;

import com.example.entity.ItemDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDetailsRepository extends JpaRepository<ItemDetails, Long> {}
