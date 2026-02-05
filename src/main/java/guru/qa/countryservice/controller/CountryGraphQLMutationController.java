package guru.qa.countryservice.controller;

import guru.qa.countryservice.dto.CountryRequest;
import guru.qa.countryservice.dto.CountryResponse;
import guru.qa.countryservice.dto.CountryUpdateRequest;
import guru.qa.countryservice.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CountryGraphQLMutationController {

    private final CountryService countryService;

    @MutationMapping
    public CountryResponse createCountry(@Argument CountryInput input) {
        CountryRequest request = new CountryRequest();
        request.setName(input.name());
        request.setCode(input.code());
        request.setCoordinates(input.coordinates());

        return countryService.createCountry(request);
    }

    @MutationMapping
    public CountryResponse updateCountry(@Argument String code, @Argument CountryUpdateInput input) {
        CountryUpdateRequest request = new CountryUpdateRequest();
        request.setName(input.name());

        return countryService.updateCountry(code, request);
    }

    @MutationMapping
    public Boolean deleteCountry(@Argument String code) {
        countryService.deleteCountry(code);
        return true;
    }

    public record CountryInput(String name, String code, String coordinates) {}

    public record CountryUpdateInput(String name) {}
}