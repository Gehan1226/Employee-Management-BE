package edu.icet.demo.dto.address;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private Long id;

    @NotBlank(message = "Address street is required.")
    private String street;

    @NotBlank(message = "Address city is required.")
    private String city;

    @NotBlank(message = "Address state is required.")
    private String state;

    @NotBlank(message = "Address postalCode is required.")
    private String postalCode;

    @NotBlank(message = "Address country is required.")
    private String country;

    @NotBlank(message = "Address district is required.")
    private String district;
}
