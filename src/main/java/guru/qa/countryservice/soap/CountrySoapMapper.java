package guru.qa.countryservice.soap;

import guru.qa.countryservice.dto.CountryResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Маппер для преобразования CountryResponse в XML-типы для SOAP ответов
 */
public final class CountrySoapMapper {

    private CountrySoapMapper() {
    }

    public static GetCountryResponse.Country toGetCountryResponseCountry(CountryResponse source) {
        if (source == null) {
            return null;
        }

        GetCountryResponse.Country target = new GetCountryResponse.Country();
        fillTarget(target, source);
        return target;
    }

    public static GetAllCountriesResponse.Countries toGetAllCountriesResponseCountry(CountryResponse source) {
        if (source == null) {
            return null;
        }

        GetAllCountriesResponse.Countries target = new GetAllCountriesResponse.Countries();
        fillTarget(target, source);
        return target;
    }

    public static CreateCountryResponse.Country toCreateCountryResponseCountry(CountryResponse source) {
        if (source == null) {
            return null;
        }

        CreateCountryResponse.Country target = new CreateCountryResponse.Country();
        fillTarget(target, source);
        return target;
    }

    public static UpdateCountryResponse.Country toUpdateCountryResponseCountry(CountryResponse source) {
        if (source == null) {
            return null;
        }

        UpdateCountryResponse.Country target = new UpdateCountryResponse.Country();
        fillTarget(target, source);
        return target;
    }

    private static void fillTarget(Object target, CountryResponse source) {
        // GetCountryResponse.Country
        if (target instanceof GetCountryResponse.Country) {
            GetCountryResponse.Country country = (GetCountryResponse.Country) target;
            country.setId(source.getId());
            country.setName(source.getName());
            country.setCode(source.getCode());
            if (source.getCoordinates() != null) {
                country.setCoordinates(source.getCoordinates());
            }
            XMLGregorianCalendar date = createCurrentXmlDate();
            country.setCreatedDate(date);
            country.setModifiedDate(date);
        }
        // GetAllCountriesResponse.Countries
        else if (target instanceof GetAllCountriesResponse.Countries) {
            GetAllCountriesResponse.Countries country = (GetAllCountriesResponse.Countries) target;
            country.setId(source.getId());
            country.setName(source.getName());
            country.setCode(source.getCode());
            if (source.getCoordinates() != null) {
                country.setCoordinates(source.getCoordinates());
            }
            XMLGregorianCalendar date = createCurrentXmlDate();
            country.setCreatedDate(date);
            country.setModifiedDate(date);
        }
        // CreateCountryResponse.Country
        else if (target instanceof CreateCountryResponse.Country) {
            CreateCountryResponse.Country country = (CreateCountryResponse.Country) target;
            country.setId(source.getId());
            country.setName(source.getName());
            country.setCode(source.getCode());
            if (source.getCoordinates() != null) {
                country.setCoordinates(source.getCoordinates());
            }
            XMLGregorianCalendar date = createCurrentXmlDate();
            country.setCreatedDate(date);
            country.setModifiedDate(date);
        }
        // UpdateCountryResponse.Country
        else if (target instanceof UpdateCountryResponse.Country) {
            UpdateCountryResponse.Country country = (UpdateCountryResponse.Country) target;
            country.setId(source.getId());
            country.setName(source.getName());
            country.setCode(source.getCode());
            if (source.getCoordinates() != null) {
                country.setCoordinates(source.getCoordinates());
            }
            XMLGregorianCalendar date = createCurrentXmlDate();
            country.setCreatedDate(date);
            country.setModifiedDate(date);
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + target.getClass().getName());
        }
    }

    private static XMLGregorianCalendar createCurrentXmlDate() {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(Date.from(LocalDateTime.now()
                    .atZone(ZoneId.systemDefault()).toInstant()));
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Failed to create XML date", e);
        }
    }
}