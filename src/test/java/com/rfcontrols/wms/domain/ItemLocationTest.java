package com.rfcontrols.wms.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.rfcontrols.wms.web.rest.TestUtil;

public class ItemLocationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemLocation.class);
        ItemLocation itemLocation1 = new ItemLocation();
        itemLocation1.setId(1L);
        ItemLocation itemLocation2 = new ItemLocation();
        itemLocation2.setId(itemLocation1.getId());
        assertThat(itemLocation1).isEqualTo(itemLocation2);
        itemLocation2.setId(2L);
        assertThat(itemLocation1).isNotEqualTo(itemLocation2);
        itemLocation1.setId(null);
        assertThat(itemLocation1).isNotEqualTo(itemLocation2);
    }
}
