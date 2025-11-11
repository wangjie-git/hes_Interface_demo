package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* <p>Title:AktWaveForm </p>
* <p>Description:数据上传--心电数据 </p>
* <p>Company: Konsung</p>
* @author  HWB
* @date 2017年8月7日下午3:46:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktWaveForm {
    private String hr; // Heart Rate
	
    private String resp_rr; // Respiration Rate

    private String sample; // Sampling frequency/second

    private String p05; // Lead I

    private String n05; // Lead II

    private String duration; // Duration

    private String ecg_i; // ECG Lead I

    private String ecg_ii; // ECG Lead II

    private String ecg_iii; // ECG Lead III

    private String ecg_avr; // ECG Lead aVR

    private String ecg_avf; // ECG Lead aVF

    private String ecg_avl; // ECG Lead aVL

    private String ecg_v1; // ECG Lead V1

    private String ecg_v2; // ECG Lead V2

    private String ecg_v3; // ECG Lead V3

    private String ecg_v4; // ECG Lead V4

    private String ecg_v5; // ECG Lead V5

    private String ecg_v6; // ECG Lead V6

    private String anal; // Analysis results

    /** PR interval **/
    private String PR;

    /** QRS interval **/
    private String QRS;

    /** QT interval **/
    private String QT;

    /** QTC interval **/
    private String QTC;

    /** P axis **/
    private String P;

    /** QRS axis **/
    private String QRSZ;

    /** T axis **/
    private String T;

    /** V5(mV) **/
    private String RV5;

    /** V1(mV) **/
    private String SV1;
}
