package edu.icet.demo.dto.leave;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeReponse {
    private Long id;
    private String name;
    private int defaultDays;
}
