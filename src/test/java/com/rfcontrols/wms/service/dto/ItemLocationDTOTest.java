package com.rfcontrols.wms.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.rfcontrols.wms.web.rest.TestUtil;

public class ItemLocationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemLocationDTO.class);
        ItemLocationDTO itemLocationDTO1 = new ItemLocationDTO();
        itemLocationDTO1.setId(1L);
        ItemLocationDTO itemLocationDTO2 = new ItemLocationDTO();
        assertThat(itemLocationDTO1).isNotEqualTo(itemLocationDTO2);
        itemLocationDTO2.setId(itemLocationDTO1.getId());
        assertThat(itemLocationDTO1).isEqualTo(itemLocationDTO2);
        itemLocationDTO2.setId(2L);
        assertThat(itemLocationDTO1).isNotEqualTo(itemLocationDTO2);
        itemLocationDTO1.setId(null);
        assertThat(itemLocationDTO1).isNotEqualTo(itemLocationDTO2);
    }
}
