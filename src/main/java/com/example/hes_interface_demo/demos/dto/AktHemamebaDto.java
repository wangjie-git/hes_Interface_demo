package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * White blood cell data (hemameba).
 *
 * The reviewed APP uploads eleven fields and historical samples contain them.
 * Their delivery by the target platform still requires confirmation.
 * The receiver accepts all eleven; this is not a sender capability guarantee.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktHemamebaDto {

    /** White blood cell total **/
    private String hemameba;

    /** Lymphocyte count **/
    private String wbcLym;

    /** Monocyte count **/
    private String wbcMon;

    /** Neutrophil count **/
    private String wbcNeu;

    /** Eosinophil count **/
    private String wbcEos;

    /** Basophil count **/
    private String wbcBas;

    /** Lymphocyte percentage **/
    private String wbcLymPercent;

    /** Monocyte percentage **/
    private String wbcMonPercent;

    /** Neutrophil percentage **/
    private String wbcNeuPercent;

    /** Eosinophil percentage **/
    private String wbcEosPercent;

    /** Basophil percentage **/
    private String wbcBasPercent;

}
