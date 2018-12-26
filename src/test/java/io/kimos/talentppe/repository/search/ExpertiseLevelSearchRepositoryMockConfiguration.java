package io.kimos.talentppe.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ExpertiseLevelSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ExpertiseLevelSearchRepositoryMockConfiguration {

    @MockBean
    private ExpertiseLevelSearchRepository mockExpertiseLevelSearchRepository;

}
