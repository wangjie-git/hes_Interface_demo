package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktGlycatedHemoglobin {

    /**Glycated Hemoglobin - Ngsp --%**/
    private String hba1cNgsp;

    /**Glycated Hemoglobin - Nfcc --%**/
    private String hba1cIfcc;

    /**Glycated Hemoglobin - eag --%**/
    private String hba1cEag;
}
