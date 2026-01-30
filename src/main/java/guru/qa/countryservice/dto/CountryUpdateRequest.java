package guru.qa.countryservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CountryUpdateRequest {
    @NotBlank(message = "Country name is required")
    private String name;
}