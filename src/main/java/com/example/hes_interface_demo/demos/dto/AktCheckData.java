package com.example.hes_interface_demo.demos.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktCheckData {

    /** Do not acknowledge categories this demo cannot retain. */
    @com.fasterxml.jackson.annotation.JsonAnySetter
    public void unsupportedCategory(String name, com.fasterxml.jackson.databind.JsonNode value) {
        throw new IllegalArgumentException("Unsupported checkData category: " + name);
    }

    /** Electrocardiogram **/
    private AktWaveForm heart;
    
    /** Blood sugar, total cholesterol, blood uric acid **/
    private AktBloodSugar bloodSugar;

    /** Blood oxygen **/
    private AktOxygen oxygen;

    /** Blood pressure **/
    private AktBloodPressure bloodPressure;

    /** Urine routine **/
    private AktRoutineUrine routineUrine;

    /** Body temperature **/
    private AktTemperature temperature;

    /** Blood lipid four items **/
    private AktLipidFourDto bloodLipidFour;

    /** Hemoglobin data **/
    private AktHemoglobinDto hemoglobin;

    /** Fetal heart monitoring **/
    private AktFetalHeartDto babyHeart;

    /** Biochemical data **/
    private AktBiochemistryDto bioche;

    /** Glycated hemoglobin **/
    private AktGlycatedHemoglobin gluHm;

    /** White blood cells **/
    private AktHemamebaDto hemameba;


    /** 电子听诊器 **/
    private StethoscopeDto stethoscope;

    /** 肺功能（呼吸家）**/
    private BreathingDto breathing;

    /** 免疫荧光项 **/
    private ImmuneDto immune;

    /** 超声设备数据 **/
    private UltrasoundDto ultrasound;

    /** 报告图像（可选）**/
    private ReportImagesDto reportImages;

}
