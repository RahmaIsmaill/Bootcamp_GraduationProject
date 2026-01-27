package com.example.repository;

import com.example.entity.Item;
import com.example.enums.TaskPriority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE LOWER(i.title) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Item> findAllByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.itemDetails.taskPriority = :priority")
    Page<Item> findAllByPriority(@Param("priority") TaskPriority priority, Pageable pageable);
}
