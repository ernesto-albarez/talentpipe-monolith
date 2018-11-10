package io.kimos.talentppe.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of AreaSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AreaSearchRepositoryMockConfiguration {

    @MockBean
    private AreaSearchRepository mockAreaSearchRepository;

}
