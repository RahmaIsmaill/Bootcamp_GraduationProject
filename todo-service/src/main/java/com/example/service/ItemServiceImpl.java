package com.example.service;

import com.example.dto.request.ItemCreateDto;
import com.example.dto.request.ItemUpdateDto;
import com.example.dto.response.ItemResponseDto;
import com.example.entity.Item;
import com.example.entity.ItemDetails;
import com.example.enums.TaskPriority;
import com.example.exception.GlobalException;
import com.example.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public ItemResponseDto createItem(ItemCreateDto itemCreateDto) {

        ItemDetails itemDetails = ItemDetails.builder()
                .createdAt(LocalDateTime.now())
                .description(itemCreateDto.getDescription())
                .taskPriority(itemCreateDto.getTaskPriority())
                .taskStatus(itemCreateDto.getTaskStatus())
                .build();

        Item item = Item.builder()
                .title(itemCreateDto.getTitle())
                .userId(itemCreateDto.getUserId())
                .itemDetails(itemDetails)
                .build();

        itemRepository.save(item);
        return mapToResponseDTO(item);
    }

    @Override
    public ItemResponseDto updateItem(Long taskId, ItemUpdateDto itemUpdateDto) {

        Item item = itemRepository.findById(taskId)
                .orElseThrow(() -> new GlobalException(Map.of("Error", "Task not found")));

        ItemDetails itemDetails = item.getItemDetails();

        if (itemUpdateDto.getTitle() != null)
            item.setTitle(itemUpdateDto.getTitle());

        if (itemUpdateDto.getDescription() != null)
            itemDetails.setDescription(itemUpdateDto.getDescription());

        if (itemUpdateDto.getTaskPriority() != null)
            itemDetails.setTaskPriority(itemUpdateDto.getTaskPriority());

        if (itemUpdateDto.getTaskStatus() != null)
            itemDetails.setTaskStatus(itemUpdateDto.getTaskStatus());

        itemRepository.save(item);
        return mapToResponseDTO(item);
    }

    @Override
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new GlobalException(Map.of("Error", "Task not found")));
        itemRepository.delete(item);
    }

    @Override
    public ItemResponseDto findById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new GlobalException(Map.of("Error", "Task not found")));
        return mapToResponseDTO(item);
    }

    @Override
    public Page<ItemResponseDto> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    public Page<ItemResponseDto> findByName(String name, Pageable pageable) {
        return itemRepository.findAllByName(name, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    public Page<ItemResponseDto> findByPriority(TaskPriority priority, Pageable pageable) {
        return itemRepository.findAllByPriority(priority, pageable)
                .map(this::mapToResponseDTO);
    }

    private ItemResponseDto mapToResponseDTO(Item item) {
        ItemDetails d = item.getItemDetails();
        return ItemResponseDto.builder()
                .itemId(item.getId())
                .title(item.getTitle())
                .description(d.getDescription())
                .taskPriority(d.getTaskPriority())
                .taskStatus(d.getTaskStatus())
                .createdAt(d.getCreatedAt())
                .build();
    }
}
