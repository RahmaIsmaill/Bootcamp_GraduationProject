package com.example.repository;

import com.example.dto.response.ItemResponseDto;
import com.example.entity.Item;
import com.example.enums.TaskPriority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.net.http.HttpHeaders;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("""
       SELECT i FROM Item i
       WHERE i.userId = :userId
       AND i.itemDetails.taskPriority = :priority
       """)
    Page<Item> findAllByPriority(@Param("priority") TaskPriority priority,
                                 @Param("userId") Long userId,
                                 Pageable pageable);

    @Query("""
       SELECT i FROM Item i
       WHERE i.userId = :userId
       AND LOWER(i.title) LIKE LOWER(CONCAT('%', :name, '%'))
       """)
    Page<Item> findAllByName(@Param("name") String name,
                             @Param("userId") Long userId,
                             Pageable pageable);

    Page<Item> findAllByUserId(Long userId, Pageable pageable);
}
