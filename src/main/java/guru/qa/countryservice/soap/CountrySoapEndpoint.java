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

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.List;

@Endpoint
@RequiredArgsConstructor
public class CountrySoapEndpoint {

    private static final String NAMESPACE_URI = "http://qa.guru/country-service";
    private final CountryService countryService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
        CountryResponse country = countryService.getCountryByCode(request.getCode());

        GetCountryResponse response = new GetCountryResponse();
        response.setCountry(mapToGetCountryResponseCountry(country));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllCountriesRequest")
    @ResponsePayload
    public GetAllCountriesResponse getAllCountries(@RequestPayload GetAllCountriesRequest request) {
        List<CountryResponse> countries = countryService.listCountries();

        GetAllCountriesResponse response = new GetAllCountriesResponse();
        countries.forEach(country ->
                response.getCountries().add(mapToGetAllCountriesResponseCountry(country))
        );

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createCountryRequest")
    @ResponsePayload
    public CreateCountryResponse createCountry(@RequestPayload CreateCountryRequest request) {
        CountryRequest dtoRequest = new CountryRequest();
        dtoRequest.setName(request.getName());
        dtoRequest.setCode(request.getCode());
        dtoRequest.setCoordinates(request.getCoordinates());

        CountryResponse country = countryService.createCountry(dtoRequest);

        CreateCountryResponse response = new CreateCountryResponse();
        response.setCountry(mapToCreateCountryResponseCountry(country));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateCountryRequest")
    @ResponsePayload
    public UpdateCountryResponse updateCountry(@RequestPayload UpdateCountryRequest request) {
        CountryUpdateRequest dtoRequest = new CountryUpdateRequest();
        dtoRequest.setName(request.getName());

        CountryResponse country = countryService.updateCountry(request.getCode(), dtoRequest);

        UpdateCountryResponse response = new UpdateCountryResponse();
        response.setCountry(mapToUpdateCountryResponseCountry(country));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteCountryRequest")
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


    private GetCountryResponse.Country mapToGetCountryResponseCountry(CountryResponse source) {
        GetCountryResponse.Country target = new GetCountryResponse.Country();

        target.setId(source.getId());
        target.setName(source.getName());
        target.setCode(source.getCode());

        if (source.getCoordinates() != null) {
            target.setCoordinates(source.getCoordinates());
        }

        // Добавляем даты
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(java.util.Date.from(LocalDateTime.now()
                    .atZone(ZoneId.systemDefault()).toInstant()));
            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(calendar);
            target.setCreatedDate(xmlCalendar);
            target.setModifiedDate(xmlCalendar);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return target;
    }

    private GetAllCountriesResponse.Countries mapToGetAllCountriesResponseCountry(CountryResponse source) {
        GetAllCountriesResponse.Countries target = new GetAllCountriesResponse.Countries();

        target.setId(source.getId());
        target.setName(source.getName());
        target.setCode(source.getCode());

        if (source.getCoordinates() != null) {
            target.setCoordinates(source.getCoordinates());
        }

        // Добавляем даты
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(java.util.Date.from(LocalDateTime.now()
                    .atZone(ZoneId.systemDefault()).toInstant()));
            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(calendar);
            target.setCreatedDate(xmlCalendar);
            target.setModifiedDate(xmlCalendar);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return target;
    }


    private CreateCountryResponse.Country mapToCreateCountryResponseCountry(CountryResponse source) {
        CreateCountryResponse.Country target = new CreateCountryResponse.Country();

        target.setId(source.getId());
        target.setName(source.getName());
        target.setCode(source.getCode());

        if (source.getCoordinates() != null) {
            target.setCoordinates(source.getCoordinates());
        }

        // Добавляем даты
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(java.util.Date.from(LocalDateTime.now()
                    .atZone(ZoneId.systemDefault()).toInstant()));
            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(calendar);
            target.setCreatedDate(xmlCalendar);
            target.setModifiedDate(xmlCalendar);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return target;
    }


    private UpdateCountryResponse.Country mapToUpdateCountryResponseCountry(CountryResponse source) {
        UpdateCountryResponse.Country target = new UpdateCountryResponse.Country();

        target.setId(source.getId());
        target.setName(source.getName());
        target.setCode(source.getCode());

        if (source.getCoordinates() != null) {
            target.setCoordinates(source.getCoordinates());
        }

        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(java.util.Date.from(LocalDateTime.now()
                    .atZone(ZoneId.systemDefault()).toInstant()));
            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(calendar);
            target.setCreatedDate(xmlCalendar);
            target.setModifiedDate(xmlCalendar);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return target;
    }
}