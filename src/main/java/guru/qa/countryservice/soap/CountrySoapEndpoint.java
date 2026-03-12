package guru.qa.countryservice.soap;

import guru.qa.countryservice.dto.CountryRequest;
import guru.qa.countryservice.dto.CountryResponse;
import guru.qa.countryservice.dto.CountryUpdateRequest;
import guru.qa.countryservice.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
@RequiredArgsConstructor
public class CountrySoapEndpoint {

    private final CountryService countryService;

    @PayloadRoot(namespace = "#{'${soap.namespace:http://qa.guru/country-service}'}",
            localPart = "getCountryRequest")
    @ResponsePayload
    public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
        CountryResponse country = countryService.getCountryByCode(request.getCode());

        GetCountryResponse response = new GetCountryResponse();
        response.setCountry(CountrySoapMapper.toGetCountryResponseCountry(country));

        return response;
    }

    @PayloadRoot(namespace = "#{'${soap.namespace:http://qa.guru/country-service}'}",
            localPart = "getAllCountriesRequest")
    @ResponsePayload
    public GetAllCountriesResponse getAllCountries(@RequestPayload GetAllCountriesRequest request) {
        List<CountryResponse> countries = countryService.listCountries();

        GetAllCountriesResponse response = new GetAllCountriesResponse();
        countries.forEach(country ->
                response.getCountries().add(CountrySoapMapper.toGetAllCountriesResponseCountry(country))
        );

        return response;
    }

    @PayloadRoot(namespace = "#{'${soap.namespace:http://qa.guru/country-service}'}",
            localPart = "createCountryRequest")
    @ResponsePayload
    public CreateCountryResponse createCountry(@RequestPayload CreateCountryRequest request) {
        CountryRequest dtoRequest = new CountryRequest();
        dtoRequest.setName(request.getName());
        dtoRequest.setCode(request.getCode());
        dtoRequest.setCoordinates(request.getCoordinates());

        CountryResponse country = countryService.createCountry(dtoRequest);

        CreateCountryResponse response = new CreateCountryResponse();
        response.setCountry(CountrySoapMapper.toCreateCountryResponseCountry(country));

        return response;
    }

    @PayloadRoot(namespace = "#{'${soap.namespace:http://qa.guru/country-service}'}",
            localPart = "updateCountryRequest")
    @ResponsePayload
    public UpdateCountryResponse updateCountry(@RequestPayload UpdateCountryRequest request) {
        CountryUpdateRequest dtoRequest = new CountryUpdateRequest();
        dtoRequest.setName(request.getName());

        CountryResponse country = countryService.updateCountry(request.getCode(), dtoRequest);

        UpdateCountryResponse response = new UpdateCountryResponse();
        response.setCountry(CountrySoapMapper.toUpdateCountryResponseCountry(country));

        return response;
    }

    @PayloadRoot(namespace = "#{'${soap.namespace:http://qa.guru/country-service}'}",
            localPart = "deleteCountryRequest")
    @ResponsePayload
    public DeleteCountryResponse deleteCountry(@RequestPayload DeleteCountryRequest request) {
        DeleteCountryResponse response = new DeleteCountryResponse();

        try {
            countryService.deleteCountry(request.getCode());
            response.setSuccess(true);
            response.setMessage("Country with code " + request.getCode() + " successfully deleted");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to delete country: " + e.getMessage());
        }

        return response;
    }
}