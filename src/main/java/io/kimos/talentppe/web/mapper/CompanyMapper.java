package io.kimos.talentppe.web.mapper;

import io.kimos.talentppe.domain.Company;
import io.kimos.talentppe.web.rest.dto.CreateCompanyRequest;
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
