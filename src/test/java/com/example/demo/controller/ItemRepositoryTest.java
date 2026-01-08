package com.example.demo.repository;

import com.example.demo.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository repository;

    @Test
    void saveAndFind_shouldPersistItem() {
        Item item = new Item(null, "Item", "Desc");
        Item saved = repository.save(item);

        assertThat(repository.findById(saved.getId())).isPresent();
    }
}
