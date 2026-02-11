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
    private final UserClientService userClientService;

    @Override
    public ItemResponseDto createItem(ItemCreateDto itemCreateDto, String token) {
        Long userId = getUserIdFromToken(token);

        ItemDetails itemDetails = ItemDetails.builder()
                .createdAt(LocalDateTime.now())
                .description(itemCreateDto.getDescription())
                .taskPriority(itemCreateDto.getTaskPriority())
                .taskStatus(itemCreateDto.getTaskStatus())
                .build();

        Item item = Item.builder()
                .title(itemCreateDto.getTitle())
                .userId(userId)
                .itemDetails(itemDetails)
                .build();

        itemRepository.save(item);
        return mapToResponseDTO(item);
    }

    @Override
    public ItemResponseDto updateItem(Long taskId, ItemUpdateDto itemUpdateDto, String token) {
        Long userId = getUserIdFromToken(token);

        Item item = itemRepository.findById(taskId)
                .orElseThrow(() -> new GlobalException(Map.of("Error", "Task not found")));

        if (!item.getUserId().equals(userId)) {
            throw new GlobalException(Map.of("Error", "You are not allowed to update this task"));
        }

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
    public void deleteItem(Long id, String token) {
        Long userId = getUserIdFromToken(token);

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new GlobalException(Map.of("Error", "Task not found")));

        if (!item.getUserId().equals(userId)) {
            throw new GlobalException(Map.of("Error", "You are not allowed to delete this task"));
        }

        itemRepository.delete(item);
    }

    @Override
    public ItemResponseDto findById(Long id, String token) {
        Long userId = getUserIdFromToken(token);

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new GlobalException(Map.of("Error", "Task not found")));

        if (!item.getUserId().equals(userId)) {
            throw new GlobalException(Map.of("Error", "You are not allowed to view this task"));
        }

        return mapToResponseDTO(item);
    }

    @Override
    public Page<ItemResponseDto> findAll(Pageable pageable, String token) {
        Long userId = getUserIdFromToken(token);
        return itemRepository.findAllByUserId(userId, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    public Page<ItemResponseDto> findByName(String name, Pageable pageable, String token) {
        Long userId = getUserIdFromToken(token);
        return itemRepository.findAllByName(name, userId, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    public Page<ItemResponseDto> findByPriority(TaskPriority priority, Pageable pageable, String token) {
        Long userId = getUserIdFromToken(token);
        return itemRepository.findAllByPriority(priority, userId, pageable)
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

    private Long getUserIdFromToken(String token) {
        return userClientService.getUserIdFromToken(token);
    }
}
