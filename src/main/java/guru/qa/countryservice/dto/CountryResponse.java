package guru.qa.countryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponse {
    private String id;
    private String name;
    private String code;
    private String coordinates;
}