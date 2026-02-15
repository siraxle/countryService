package guru.qa.countryservice.controller;

import guru.qa.countryservice.dto.CountryResponse;
import guru.qa.countryservice.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CountryGraphQLQueryController {

    private final CountryService countryService;

    @QueryMapping
    public List<CountryResponse> listCountries() {
        return countryService.listCountries();
    }

    @QueryMapping
    public CountryResponse getCountryByCode(@Argument String code) {
        return countryService.getCountryByCode(code);
    }
}