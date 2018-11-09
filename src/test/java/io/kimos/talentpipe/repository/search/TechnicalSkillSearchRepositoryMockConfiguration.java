package io.kimos.talentpipe.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of TechnicalSkillSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TechnicalSkillSearchRepositoryMockConfiguration {

    @MockBean
    private TechnicalSkillSearchRepository mockTechnicalSkillSearchRepository;

}
