package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository repository;

    @InjectMocks
    private ItemService service;

    @Test
    void findAll_shouldReturnItems() {
        when(repository.findAll()).thenReturn(List.of(new Item(1L, "Item1", "Desc")));

        List<Item> result = service.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    void findById_whenExists_shouldReturnItem() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Item(1L, "Item", "Desc")));

        Optional<Item> item = service.findById(1L);

        assertThat(item).isPresent();
    }

    @Test
    void save_shouldPersistItem() {
        Item item = new Item(null, "Item", "Desc");
        when(repository.save(item)).thenReturn(new Item(1L, "Item", "Desc"));

        Item saved = service.save(item);

        assertThat(saved.getId()).isEqualTo(1L);
    }

    @Test
    void update_whenItemExists_shouldUpdateFields() {
        Item existing = new Item(1L, "Old", "OldDesc");
        Item updated = new Item(null, "New", "NewDesc");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Optional<Item> result = service.update(1L, updated);

        assertThat(result).isPresent();
        assertThat(existing.getName()).isEqualTo("New");
    }

}
