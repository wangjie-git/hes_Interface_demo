package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktTemperature {

    /**Body temperature**/
    private String temp;
}
