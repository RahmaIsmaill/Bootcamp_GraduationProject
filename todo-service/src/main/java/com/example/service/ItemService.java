package com.example.service;

import com.example.dto.request.ItemCreateDto;
import com.example.dto.request.ItemUpdateDto;
import com.example.dto.response.ItemResponseDto;
import com.example.enums.TaskPriority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {

    ItemResponseDto createItem(ItemCreateDto itemCreateDto,String token);

    ItemResponseDto updateItem(Long taskId, ItemUpdateDto itemUpdateDto,String token);

    void deleteItem(Long id,String token);

    ItemResponseDto findById(Long id,String token);

    Page<ItemResponseDto> findAll(Pageable pageable,String token);

    Page<ItemResponseDto> findByName(String name, Pageable pageable,String token);

    Page<ItemResponseDto> findByPriority(TaskPriority priority, Pageable pageable,String token);
}
