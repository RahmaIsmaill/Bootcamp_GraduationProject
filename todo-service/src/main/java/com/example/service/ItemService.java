package com.example.service;

import com.example.dto.request.ItemCreateDto;
import com.example.dto.request.ItemUpdateDto;
import com.example.dto.response.ItemResponseDto;
import com.example.enums.TaskPriority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {

    ItemResponseDto createItem(ItemCreateDto itemCreateDto);

    ItemResponseDto updateItem(Long taskId, ItemUpdateDto itemUpdateDto);

    void deleteItem(Long id);

    ItemResponseDto findById(Long id);

    Page<ItemResponseDto> findAll(Pageable pageable);

    Page<ItemResponseDto> findByName(String name, Pageable pageable);

    Page<ItemResponseDto> findByPriority(TaskPriority priority, Pageable pageable);
}
