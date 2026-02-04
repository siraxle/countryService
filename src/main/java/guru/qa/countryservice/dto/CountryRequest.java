package guru.qa.countryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CountryRequest {
    @NotBlank(message = "Country name is required")
    private String name;

    @NotBlank(message = "Country code is required")
    @Size(min = 2, max = 3)
    private String code;

    private String coordinates;
}