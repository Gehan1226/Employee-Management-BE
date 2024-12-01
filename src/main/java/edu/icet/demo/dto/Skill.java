package edu.icet.demo.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    private Long id;
    private String name;
    private String category;
}
