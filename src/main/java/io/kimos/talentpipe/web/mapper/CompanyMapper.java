package io.kimos.talentpipe.web.mapper;

import io.kimos.talentpipe.domain.Company;
import io.kimos.talentpipe.web.rest.dto.CreateCompanyRequest;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(Company.class, CreateCompanyRequest.class)
            .field("name", "companyName")
            .field("email", "email")
            .field("contactName", "contactName")
            .field("contactLastName", "contactLastName")
            .field("phonePrefix", "phonePrefix")
            .field("phoneNumber", "phoneNumber")
            .field("taxName", "taxName")
            .field("taxId", "taxId")
            .byDefault()
            .register();
    }
}
