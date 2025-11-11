package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktBloodPressure {

    /**Systolic Blood Pressure**/
    private String sbp;

    /**Diastolic Blood Pressure**/
    private String dbp;

    /**Mean Blood Pressure**/
    private String mbp;

    /**Pulse Rate**/
    private String pr;

    /**Left Systolic Blood Pressure**/
    private String leftSbp;

    /**Left Diastolic Blood Pressure**/
    private String leftDbp;

    /**Left Mean Blood Pressure**/
    private String leftMbp;

    /**Right Systolic Blood Pressure**/
    private String rightSbp;

    /**Right Diastolic Blood Pressure**/
    private String rightDbp;

    /**Right Mean Blood Pressure**/
    private String rightMbp;
}
