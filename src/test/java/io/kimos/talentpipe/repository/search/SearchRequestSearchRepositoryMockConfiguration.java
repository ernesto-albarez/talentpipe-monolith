package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.repository.search.SearchRequestSearchRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of SearchRequestSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SearchRequestSearchRepositoryMockConfiguration {

    @MockBean
    private SearchRequestSearchRepository mockSearchRequestSearchRepository;

}
