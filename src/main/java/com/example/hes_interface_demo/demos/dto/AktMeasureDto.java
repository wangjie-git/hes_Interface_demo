package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktMeasureDto {

    /** Data ID **/
    private String dataId;

    /** Organization Code **/
    private String orgCode;

    /** Device Code **/
    private String deviceCode;

    /** Doctor Code **/
    private String doctorCode;

    /** Check Date **/
    private String checkDate;

    /** Version **/
    private String version;

    /** Software Version **/
    private String deviceVersion;

    /** Data Transmission Time **/
    private String time;

    /** Private Key **/
    private String key;

    /** Personal Basic Information **/
    private AktPersonInfo personInfo;

    /** Check Data **/
    private AktCheckData checkData;

}
