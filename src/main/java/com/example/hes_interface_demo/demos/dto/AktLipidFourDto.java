package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* <p>Title:BloodLipidFourDto </p>
* <p>Description: 血脂四项</p>
* <p>Company: Konsung</p>
* @author  HWB
* @date 2017年5月9日下午3:52:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktLipidFourDto {

    /**Total Cholesterol**/
    private String flipidsChol;

    /**Triglyceride**/
    private String flipidsTrig;

    /**High Density Lipoprotein**/
    private String flipidsHdl;

    /**Low Density Lipoprotein**/
    private String flipidsLDL;

}
