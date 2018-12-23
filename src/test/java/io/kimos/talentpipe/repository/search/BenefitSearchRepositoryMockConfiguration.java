package io.kimos.talentpipe.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of BenefitSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class BenefitSearchRepositoryMockConfiguration {

    @MockBean
    private BenefitSearchRepository mockBenefitSearchRepository;

}
