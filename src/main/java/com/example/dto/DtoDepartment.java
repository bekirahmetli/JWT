package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoDepartment {
    private Long id;
    private String location;
    private String name;
    private DtoDepartment department;
}
