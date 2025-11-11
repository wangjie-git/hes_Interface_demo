package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktRoutineUrine {

    /**Urine pH**/
    private String urinePh;

    /**Urine urobilinogen**/
    private String urineUbg;

    /**Urine occult blood**/
    private String urineBld;

    /**Urine protein**/
    private String urinePro;

    /**Urine ketone bodies**/
    private String urineKet;

    /**Urine nitrite**/
    private String urineNit;

    /**Urine glucose**/
    private String urineGlu;

    /**Urine bilirubin**/
    private String urineBil;

    /**Urine leukocytes**/
    private String urineLeu;

    /**Urine specific gravity**/
    private String urineSg;

    /**Urine vitamin C**/
    private String urineVc;

    /**Urine creatinine**/
    private String urineCre;

    /**Urine calcium**/
    private String urineCa;

    /**Microalbumin - unitless**/
    private String urineMa;

}
