package com.technologies.iapps.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.technologies.iapps.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EpaperTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Epaper.class);
        Epaper epaper1 = new Epaper();
        epaper1.setId(1L);
        Epaper epaper2 = new Epaper();
        epaper2.setId(epaper1.getId());
        assertThat(epaper1).isEqualTo(epaper2);
        epaper2.setId(2L);
        assertThat(epaper1).isNotEqualTo(epaper2);
        epaper1.setId(null);
        assertThat(epaper1).isNotEqualTo(epaper2);
    }
}
