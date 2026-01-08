package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllItems_shouldReturnListOfItems() throws Exception {
        Mockito.when(itemService.findAll())
                .thenReturn(List.of(new Item(1L, "Item1", "Desc1")));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Item1"));
    }

    @Test
    void getItemById_whenFound_shouldReturnItem() throws Exception {
        Mockito.when(itemService.findById(1L))
                .thenReturn(Optional.of(new Item(1L, "Item1", "Desc1")));

        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getItemById_whenNotFound_shouldReturn404() throws Exception {
        Mockito.when(itemService.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/items/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createItem_shouldReturn201AndLocationHeader() throws Exception {
        Item item = new Item(null, "Item1", "Desc1");
        Item saved = new Item(1L, "Item1", "Desc1");

        Mockito.when(itemService.save(Mockito.any(Item.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/items/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateItem_whenExists_shouldReturnUpdatedItem() throws Exception {
        Item updated = new Item(1L, "Updated", "UpdatedDesc");

        Mockito.when(itemService.update(Mockito.eq(1L), Mockito.any(Item.class)))
                .thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }
}

