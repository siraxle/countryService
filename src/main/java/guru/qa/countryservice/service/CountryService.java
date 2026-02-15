package guru.qa.countryservice.service;

import guru.qa.countryservice.dto.CountryRequest;
import guru.qa.countryservice.dto.CountryResponse;
import guru.qa.countryservice.dto.CountryUpdateRequest;
import guru.qa.countryservice.entity.Country;
import guru.qa.countryservice.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public List<CountryResponse> listCountries() {
        return countryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CountryResponse getCountryByCode(String code) {
        Country country = countryRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Country not found with code: " + code));
        return mapToResponse(country);
    }

    @Transactional
    public CountryResponse createCountry(CountryRequest request) {
        if (countryRepository.existsByCode(request.getCode().toUpperCase())) {
            throw new IllegalArgumentException("Country with code " + request.getCode() + " already exists");
        }

        Country country = new Country();
        country.setName(request.getName());
        country.setCode(request.getCode().toUpperCase());
        country.setCoordinates(request.getCoordinates());

        Country savedCountry = countryRepository.save(country);
        return mapToResponse(savedCountry);
    }

    @Transactional
    public CountryResponse updateCountry(String code, CountryUpdateRequest request) {
        Country country = countryRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Country not found with code: " + code));

        country.setName(request.getName());
        Country updatedCountry = countryRepository.save(country);

        return mapToResponse(updatedCountry);
    }

    @Transactional
    public void deleteCountry(String code) {
        Country country = countryRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Country not found with code: " + code));
        countryRepository.delete(country);
    }

    private CountryResponse mapToResponse(Country country) {
        return new CountryResponse(
                country.getId().toString(),
                country.getName(),
                country.getCode(),
                country.getCoordinates()
        );
    }
}