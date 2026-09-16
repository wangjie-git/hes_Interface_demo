package com.example.hes_interface_demo.demos.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 肺功能仪（呼吸家）数据
 *
 * 字段清单依据官方样例 02-custom-device 逐一核对。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BreathingDto {

    /** PEF 的预计值 L/s **/
    private String predPef;

    /** PEF 单位 **/
    private String pefUnit;

    /** PEF 的预计值 L/min **/
    private String predPefr;

    /** PEFr 单位 **/
    private String pefrUnit;

    /** FEV1 的预计值 **/
    private String predFev1;

    /** FEV1 单位 **/
    private String fev1Unit;

    /** FVC 的预计值 **/
    private String predFvc;

    /** FVC 单位 **/
    private String fvcUnit;

    /** MEF75 的预计值 **/
    private String predMef75;

    /** MEF75 单位 **/
    private String mef75Unit;

    /** MEF50 的预计值 **/
    private String predMef50;

    /** MEF50 单位 **/
    private String mef50Unit;

    /** MEF25 的预计值 **/
    private String predMef25;

    /** MEF25 单位 **/
    private String mef25Unit;

    /** MMEF 的预计值 **/
    private String predMmef;

    /** MMEF 单位 **/
    private String mmefUnit;

    /** FEV1/FVC 预计值 **/
    private String predFev1Fvc;

    /** FEV1/FVC 单位（注意拼写 fve） **/
    private String fev1FveUnit;


    /** 单次肺功能详情 **/
    private List<BreathingDetailsDto> details;

}