package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktBiochemistryDto {

    /**Alanine Aminotransferase (ALT) - Liver Function**/
    private String alanine;

    /**Total Bilirubin (TBIL) - Liver Function**/
    private String totalBilirubin;

    /**Aspartate Aminotransferase (AST) - Liver Function**/
    private String aspartate;

    /**Direct Bilirubin - Liver Function**/
    private String directBilirubin;

    /**Total Protein - Liver Function**/
    private String totalProtein;

    /**Albumin - Liver Function**/
    private String albumin;

    /**Uric Acid - Kidney Function**/
    private String uricAcid;

    /**Creatinine - Kidney Function**/
    private String creatinine;

    /**Urea - Kidney Function**/
    private String urea;
}
