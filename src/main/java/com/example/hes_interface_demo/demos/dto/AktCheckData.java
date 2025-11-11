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

    /** Blood routine **/ 
    private AktXcgDto xcg;

    /** Three classification **/
    private AktThreeWayDto threeWay;

    /** Biochemical data **/
    private AktBiochemistryDto bioche;

    /** Glycated hemoglobin **/
    private AktGlycatedHemoglobin gluHm;

    /** White blood cells **/
    private AktHemamebaDto hemameba;

    /** Thirteen items of biochemical analyzer **/
    private AktBiochemicals biochemicals;


}
