package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktPersonInfo {

    /** ID Number **/
    private String idNumber;

    /** Health card number or other **/
    private String otherNumber;

    /** Name **/
    private String name;

    /** Gender code **/
    private String sexCode;

    /** Birth date **/
    private String birthdayDate;

    /** Health record number **/
    private String healthNumber;

    /** Height **/
    private String height;

    /** Weight **/
    private String weight;

    /** Body mass index **/
    private String bmi;

    /** Waist circumference **/
    private String waist;

    /** Hip circumference **/
    private String hipline;
}
