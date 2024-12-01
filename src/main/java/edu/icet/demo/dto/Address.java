package edu.icet.demo.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String district;
    private Employee employee;
}
