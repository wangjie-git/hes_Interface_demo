package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单次肺功能详情
 *
 * 字段清单依据官方样例 02-custom-device 逐一核对。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BreathingDetailsDto {

    /** PEF 测量值 L/s **/
    private String pef;

    /** PEF 测量值 L/min **/
    private String pefr;

    /** FEV1 测量值 **/
    private String fev1;

    /** FVC 测量值 **/
    private String fvc;

    /** FEV1/FVC 测量值 **/
    private String fev1Fvc;

    /** MEF75 值 **/
    private String mef75;

    /** MEF50 值 **/
    private String mef50;

    /** MEF25 值 **/
    private String mef25;

    /** MMEF 值 **/
    private String mmef;

    /** 最佳吹气曲线参数 **/
    private String blowgrap;

    /** 最佳吹气曲线参数类型 - 0:呼吸家 1:康泰 **/
    private String blowgrapType;

    /** PEF %pred L/s **/
    private String indicatorPef;

    /** PEFr %pred L/min **/
    private String indicatorPefr;

    /** FEV1 %pred **/
    private String indicatorFev1;

    /** FVC %pred **/
    private String indicatorFvc;

    /** FEV1/FVC %pred **/
    private String indicatorFev1Fvc;

    /** MEF75 %pred **/
    private String indicatorMef75;

    /** MEF50 %pred **/
    private String indicatorMef50;

    /** MEF25 %pred **/
    private String indicatorMef25;

    /** MMEF25%-75% %pred **/
    private String indicatorMmef;

}
