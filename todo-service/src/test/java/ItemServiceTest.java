import com.example.dto.request.ItemCreateDto;
import com.example.dto.request.ItemUpdateDto;
import com.example.dto.response.ItemResponseDto;
import com.example.entity.Item;
import com.example.entity.ItemDetails;
import com.example.enums.TaskPriority;
import com.example.enums.TaskStatus;
import com.example.repository.ItemRepository;
import com.example.service.ItemServiceImpl;
import com.example.service.UserClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private UserClientService userClientService;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void testCreateItem() {
        when(userClientService.getUserIdFromToken("FAKE-TOKEN")).thenReturn(1L);


        ItemCreateDto dto = new ItemCreateDto();
        dto.setTitle("Test");
        dto.setDescription("test create");
        dto.setTaskPriority(TaskPriority.High);
        dto.setTaskStatus(TaskStatus.Done);

        ItemDetails itemDetails = ItemDetails.builder()
                .id(1L)
                .taskPriority(TaskPriority.High)
                .taskStatus(TaskStatus.Done)
                .description("test create")
                .createdAt(LocalDateTime.now())
                .build();

        Item item = Item.builder()
                .id(1L)
                .title("Test")
                .userId(1L)
                .itemDetails(itemDetails)
                .build();

        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);

        ItemResponseDto response = itemService.createItem(dto, "FAKE-TOKEN");

        assertEquals("Test", response.getTitle());
        assertEquals("test create", response.getDescription());
        assertEquals(TaskPriority.High, response.getTaskPriority());
        assertEquals(TaskStatus.Done, response.getTaskStatus());

        verify(userClientService, times(1)).getUserIdFromToken("FAKE-TOKEN");
        verify(itemRepository, times(1)).save(any(Item.class));
    }


    @Test
    public void updateItemTest() {

        when(userClientService.getUserIdFromToken("FAKE-TOKEN")).thenReturn(Long.valueOf(1));

        ItemDetails itemDetails=ItemDetails
                .builder()
                .id(1L)
                .taskPriority(TaskPriority.High)
                .taskStatus(TaskStatus.Done)
                .description("test update")
                .createdAt(LocalDateTime.now())
                .build();

        Item item=Item
                .builder()
                .id(1L)
                .title("Test")
                .userId(1L)
                .itemDetails(itemDetails)
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setTitle("Test after update");
        itemUpdateDto.setDescription("Test updating task");

        ItemResponseDto itemResponseDto=itemService.updateItem(1L,itemUpdateDto,"FAKE-TOKEN");
        assertEquals("Test updating task",itemResponseDto.getDescription());
        assertEquals("Test after update",itemResponseDto.getTitle());

        verify(userClientService, times(1)).getUserIdFromToken("FAKE-TOKEN");
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void deleteItemTest() {
        when(userClientService.getUserIdFromToken("FAKE-TOKEN")).thenReturn(1L);

        ItemDetails itemDetails = ItemDetails.builder()
                .id(1L)
                .taskPriority(TaskPriority.High)
                .taskStatus(TaskStatus.Done)
                .description("test update")
                .createdAt(LocalDateTime.now())
                .build();

        Item item = Item.builder()
                .id(1L)
                .title("Test")
                .userId(1L)
                .itemDetails(itemDetails)
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).delete(any(Item.class));

        itemService.deleteItem(1L, "FAKE-TOKEN");

        verify(userClientService, times(1)).getUserIdFromToken("FAKE-TOKEN");
        verify(itemRepository, times(1)).delete(any(Item.class));
    }

    @Test
    public void findItemByIdTest() {
        when(userClientService.getUserIdFromToken("FAKE-TOKEN")).thenReturn(1L);

        ItemDetails itemDetails = ItemDetails.builder()
                .id(1L)
                .taskPriority(TaskPriority.High)
                .taskStatus(TaskStatus.Done)
                .description("test update")
                .createdAt(LocalDateTime.now())
                .build();

        Item item = Item.builder()
                .id(1L)
                .title("Test")
                .userId(1L)
                .itemDetails(itemDetails)
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ItemResponseDto response = itemService.findById(1L, "FAKE-TOKEN");


        assertEquals("Test", response.getTitle());
        assertEquals("test update", response.getDescription());
        assertEquals(TaskPriority.High, response.getTaskPriority());
        assertEquals(TaskStatus.Done, response.getTaskStatus());
        verify(userClientService, times(1)).getUserIdFromToken("FAKE-TOKEN");
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    public void findAllItemsByUserIdTest() {
        when(userClientService.getUserIdFromToken("FAKE-TOKEN")).thenReturn(Long.valueOf(1));
        ItemDetails itemDetails = ItemDetails.builder()
                .id(1L)
                .taskPriority(TaskPriority.High)
                .taskStatus(TaskStatus.Done)
                .description("test update")
                .build();

        Item item = Item.builder()
                .id(1L)
                .title("Test")
                .userId(1L)
                .itemDetails(itemDetails)
                .build();


        Page<Item> page = new PageImpl<>(List.of(item));
        Pageable pageable = PageRequest.of(0, 1);

        when(itemRepository.findAllByUserId(1L, pageable)).thenReturn(page);

        Page<ItemResponseDto>itemResponseDtos=itemService.findAll(pageable, "FAKE-TOKEN");
        
        assertEquals("Test", itemResponseDtos.getContent().getFirst().getTitle());
        assertEquals(TaskPriority.High, itemResponseDtos.getContent().getFirst().getTaskPriority());
        assertEquals(TaskStatus.Done, itemResponseDtos.getContent().getFirst().getTaskStatus());
        assertEquals("test update", itemResponseDtos.getContent().getFirst().getDescription());
        verify(userClientService, times(1)).getUserIdFromToken("FAKE-TOKEN");
        verify(itemRepository, times(1)).findAllByUserId(1L, pageable);


    }

    @Test
    public void findItemByNameTest() {
        when(userClientService.getUserIdFromToken("FAKE-TOKEN")).thenReturn(Long.valueOf(1));

        ItemDetails itemDetails = ItemDetails.builder()
                .id(1L)
                .taskPriority(TaskPriority.High)
                .taskStatus(TaskStatus.Done)
                .description("test update")
                .build();

        Item item = Item.builder()
                .id(1L)
                .title("Test")
                .userId(1L)
                .itemDetails(itemDetails)
                .build();

        Page<Item> page = new PageImpl<>(List.of(item));
        Pageable pageable = PageRequest.of(0, 1);

        when(itemRepository.findAllByName("test",1L,pageable)).thenReturn(page);

        Page<ItemResponseDto>items=itemService.findByName("test",pageable, "FAKE-TOKEN");
        assertEquals("Test", items.getContent().getFirst().getTitle());
        assertEquals(TaskPriority.High, items.getContent().getFirst().getTaskPriority());
        assertEquals(TaskStatus.Done, items.getContent().getFirst().getTaskStatus());
        assertEquals("test update", items.getContent().getFirst().getDescription());
        verify(userClientService, times(1)).getUserIdFromToken("FAKE-TOKEN");
        verify(itemRepository, times(1)).findAllByName("test",1L,pageable);

    }

    @Test
    public void findItemsByPriorityTest() {
        when(userClientService.getUserIdFromToken("FAKE-TOKEN")).thenReturn(1L);

        ItemDetails itemDetails = ItemDetails.builder()
                .id(1L)
                .taskPriority(TaskPriority.High)
                .taskStatus(TaskStatus.Done)
                .description("test update")
                .build();

        Item item = Item.builder()
                .id(1L)
                .title("Test")
                .userId(1L)
                .itemDetails(itemDetails)
                .build();

        Page<Item> page = new PageImpl<>(List.of(item));
        Pageable pageable = PageRequest.of(0, 1);

        when(itemRepository.findAllByPriority(TaskPriority.High, 1L, pageable)).thenReturn(page);

        Page<ItemResponseDto> items = itemService.findByPriority(TaskPriority.High, pageable, "FAKE-TOKEN");

        ItemResponseDto firstItem = items.getContent().getFirst();
        assertEquals("Test", firstItem.getTitle());
        assertEquals(TaskPriority.High, firstItem.getTaskPriority());
        assertEquals(TaskStatus.Done, firstItem.getTaskStatus());
        assertEquals("test update", firstItem.getDescription());

        verify(userClientService, times(1)).getUserIdFromToken("FAKE-TOKEN");
        verify(itemRepository, times(1)).findAllByPriority(TaskPriority.High, 1L, pageable);
    }


}
