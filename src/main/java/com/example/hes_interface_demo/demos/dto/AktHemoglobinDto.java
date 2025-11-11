package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* <p>Title:HemoglobinDto </p>
* <p>Description: 血红蛋白数据</p>
* <p>Company: Konsung</p>
* @author  HWB
* @date 2017年5月9日下午4:06:02
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktHemoglobinDto {


    /**Hemoglobin**/
    private String assxhdb;

    /**Hematocrit value**/
    private String htc;
}
