package io.kimos.talentppe.web.mapper;

import io.kimos.talentppe.domain.City;
import io.kimos.talentppe.web.rest.dto.CreateCityDTO;
import io.kimos.talentppe.web.rest.dto.UpdateCityDTO;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CityMapper implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(City.class, CreateCityDTO.class)
            .field("name", "name")
            .field("postalCode", "postalCode")
            .field("country.id", "countryId")
            .byDefault()
            .register();

        orikaMapperFactory.classMap(City.class, UpdateCityDTO.class)
            .field("name", "name")
            .field("postalCode", "postalCode")
            .field("country.id", "countryId")
            .field("id", "id")
            .byDefault()
            .register();
    }
}
