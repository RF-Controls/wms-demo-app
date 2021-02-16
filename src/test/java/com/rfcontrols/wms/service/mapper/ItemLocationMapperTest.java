package com.rfcontrols.wms.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemLocationMapperTest {

    private ItemLocationMapper itemLocationMapper;

    @BeforeEach
    public void setUp() {
        itemLocationMapper = new ItemLocationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(itemLocationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(itemLocationMapper.fromId(null)).isNull();
    }
}
