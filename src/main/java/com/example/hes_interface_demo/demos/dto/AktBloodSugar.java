package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
 public class AktBloodSugar {

    /** Blood glucose **/
    private String glu;

    /** Blood uric acid **/
    private String uricacid;

    /** Blood glucose measurement method **/
    private String gluStyle;

    /** Total cholesterol **/
    private String xzzdgc;
}
