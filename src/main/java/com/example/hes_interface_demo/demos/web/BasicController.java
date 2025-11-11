/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.hes_interface_demo.demos.web;

import com.example.hes_interface_demo.demos.dto.*;
import com.example.hes_interface_demo.demos.util.JsonUtil;
import com.example.hes_interface_demo.demos.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Slf4j
@Controller
public class BasicController {

    //To be supplemented
    private static final String KEY = ".......";

    public final static String SUCCESS = "10000";

    public final static String FAIL_RESULT = "00000";

    // http://127.0.0.1:8080/parsingHesData
    @PostMapping("/parsingHesData")
    @ResponseBody
    public ThirdResultCodeDto parsingHesData(@RequestBody AktMeasureDto aktMeasureDto) {
        try {
            log.info("Received health measurement data: " + aktMeasureDto);
            // 1. Parse and validate basic request parameters
            String dataId = aktMeasureDto.getDataId();
            String orgCode = aktMeasureDto.getOrgCode();
            String deviceCode = aktMeasureDto.getDeviceCode();
            String doctorCode = aktMeasureDto.getDoctorCode();
            String checkDate = aktMeasureDto.getCheckDate();
            String version = aktMeasureDto.getVersion();
            String deviceVersion = aktMeasureDto.getDeviceVersion();
            String requestTime = aktMeasureDto.getTime();
            String receivedKey = aktMeasureDto.getKey();


            log.info("Basic info - Org: " + orgCode + ", Device: " + deviceCode +
                    ", Doctor: " + doctorCode + ", CheckDate: " + checkDate);

            // 2. Validate security key
            String expectedKey = MD5.getMD5(orgCode + KEY);
            if (!expectedKey.equals(receivedKey)) {
                log.warn("Invalid key received. Expected: " + expectedKey + ", Received: " + receivedKey);
                return new ThirdResultCodeDto(FAIL_RESULT, "Authentication failed: Invalid key");
            }

            // 3. Parse person information
            AktPersonInfo personInfo = aktMeasureDto.getPersonInfo();
            if (personInfo != null) {
                String idNumber = personInfo.getIdNumber();
                String name = personInfo.getName();
                String sexCode = personInfo.getSexCode();
                String birthday = personInfo.getBirthdayDate();
                String height = personInfo.getHeight();
                String weight = personInfo.getWeight();
                String bmi = personInfo.getBmi();
                String waist = personInfo.getWaist();
                String hipline = personInfo.getHipline();

                log.info("Person info - Name: " + name + ", ID: " + idNumber +
                        ", Sex: " + sexCode + ", BMI: " + bmi);
            }

            // 4. Parse health check data
            AktCheckData checkData = aktMeasureDto.getCheckData();
            if (checkData != null) {
                // 4.1 Parse ECG waveform data
                AktWaveForm ecgData = checkData.getHeart();
                if (ecgData != null) {
                    String heartRate = ecgData.getHr();
                    String respRate = ecgData.getResp_rr();
                    String sample = ecgData.getSample();
                    String analysis = ecgData.getAnal();
                    String prInterval = ecgData.getPR();
                    String qrsInterval = ecgData.getQRS();
                    String qtInterval = ecgData.getQT();
                    String qtcInterval = ecgData.getQTC();

                    log.info("ECG data - HR: " + heartRate + ", RR: " + respRate +
                            ", PR: " + prInterval + ", QRS: " + qrsInterval);

                    // Parse all ECG leads
                    String ecgLeadI = ecgData.getEcg_i();
                    String ecgLeadII = ecgData.getEcg_ii();
                    String ecgLeadIII = ecgData.getEcg_iii();
                    String ecgLeadAVR = ecgData.getEcg_avr();
                    String ecgLeadAVF = ecgData.getEcg_avf();
                    String ecgLeadAVL = ecgData.getEcg_avl();
                    String ecgLeadV1 = ecgData.getEcg_v1();
                    String ecgLeadV2 = ecgData.getEcg_v2();
                    String ecgLeadV3 = ecgData.getEcg_v3();
                    String ecgLeadV4 = ecgData.getEcg_v4();
                    String ecgLeadV5 = ecgData.getEcg_v5();
                    String ecgLeadV6 = ecgData.getEcg_v6();
                }

                // 4.2 Parse blood sugar data
                AktBloodSugar bloodSugar = checkData.getBloodSugar();
                if (bloodSugar != null) {
                    String glucose = bloodSugar.getGlu();
                    String glucoseStyle = bloodSugar.getGluStyle();
                    String uricAcid = bloodSugar.getUricacid();
                    String cholesterol = bloodSugar.getXzzdgc();

                    log.info("Blood sugar - Glucose: " + glucose + ", Style: " + glucoseStyle);
                }

                // 4.3 Parse oxygen saturation data
                AktOxygen oxygen = checkData.getOxygen();
                if (oxygen != null) {
                    String spo2 = oxygen.getSpo2();
                    String oxygenPR = oxygen.getPr();

                    log.info("Oxygen - SpO2: " + spo2 + ", PR: " + oxygenPR);
                }

                // 4.4 Parse blood pressure data
                AktBloodPressure bloodPressure = checkData.getBloodPressure();
                if (bloodPressure != null) {
                    String sbp = bloodPressure.getSbp();
                    String dbp = bloodPressure.getDbp();
                    String mbp = bloodPressure.getMbp();
                    String bpPR = bloodPressure.getPr();
                    String leftSbp = bloodPressure.getLeftSbp();
                    String leftDbp = bloodPressure.getLeftDbp();
                    String rightSbp = bloodPressure.getRightSbp();
                    String rightDbp = bloodPressure.getRightDbp();

                    log.info("Blood pressure - SBP: " + sbp + ", DBP: " + dbp +
                            ", Left: " + leftSbp + "/" + leftDbp +
                            ", Right: " + rightSbp + "/" + rightDbp);
                }

                // 4.5 Parse urine test data
                AktRoutineUrine urine = checkData.getRoutineUrine();
                if (urine != null) {
                    String urinePH = urine.getUrinePh();
                    String urineProtein = urine.getUrinePro();
                    String urineGlucose = urine.getUrineGlu();
                    String urineBlood = urine.getUrineBld();
                    String urineKetone = urine.getUrineKet();

                    log.info("Urine test - pH: " + urinePH + ", Protein: " + urineProtein +
                            ", Glucose: " + urineGlucose);
                }

                // 4.6 Parse temperature data
                AktTemperature temperature = checkData.getTemperature();
                if (temperature != null) {
                    String bodyTemp = temperature.getTemp();
                    log.info("Body temperature: " + bodyTemp);
                }

                // 4.7 Parse lipid profile data
                AktLipidFourDto lipid = checkData.getBloodLipidFour();
                if (lipid != null) {
                    String cholesterol = lipid.getFlipidsChol();
                    String triglycerides = lipid.getFlipidsTrig();
                    String hdl = lipid.getFlipidsHdl();
                    String ldl = lipid.getFlipidsLDL();

                    log.info("Lipid profile - Cholesterol: " + cholesterol +
                            ", Triglycerides: " + triglycerides + ", HDL: " + hdl + ", LDL: " + ldl);
                }

                // 4.8 Parse hemoglobin data
                AktHemoglobinDto hemoglobin = checkData.getHemoglobin();
                if (hemoglobin != null) {
                    String hgb = hemoglobin.getAssxhdb();
                    String hct = hemoglobin.getHtc();

                    log.info("Hemoglobin - HGB: " + hgb + ", HCT: " + hct);
                }

                // 4.9 Parse fetal heart data
                AktFetalHeartDto fetalHeart = checkData.getBabyHeart();
                if (fetalHeart != null) {
                    String fetalHeartRate = fetalHeart.getFetalHeartNum();
                    log.info("Fetal heart rate: " + fetalHeartRate);
                }

                // 4.10 Parse blood count data (XCG)
                AktXcgDto bloodCount = checkData.getXcg();
                if (bloodCount != null) {
                    String wbc = bloodCount.getWBC();
                    String rbc = bloodCount.getRBC();
                    String hgb = bloodCount.getHGB();
                    String hct = bloodCount.getHCT();
                    String plt = bloodCount.getPLT();

                    log.info("Blood count - WBC: " + wbc + ", RBC: " + rbc +
                            ", HGB: " + hgb + ", PLT: " + plt);
                }

                // 4.11 Parse three-way cell count
                AktThreeWayDto threeWay = checkData.getThreeWay();
                if (threeWay != null) {
                    String smallCells = threeWay.getSmallCellGroup();
                    String middleCells = threeWay.getMiddleCellGroup();
                    String bigCells = threeWay.getBigCellGroup();

                    log.info("Three-way cells - Small: " + smallCells +
                            ", Middle: " + middleCells + ", Big: " + bigCells);
                }

                // 4.12 Parse biochemistry data
                AktBiochemistryDto biochemistry = checkData.getBioche();
                if (biochemistry != null) {
                    String alt = biochemistry.getAlanine();
                    String ast = biochemistry.getAspartate();
                    String totalBilirubin = biochemistry.getTotalBilirubin();
                    String totalProtein = biochemistry.getTotalProtein();
                    String albumin = biochemistry.getAlbumin();
                    String creatinine = biochemistry.getCreatinine();
                    String urea = biochemistry.getUrea();

                    log.info("Biochemistry - ALT: " + alt + ", AST: " + ast +
                            ", Total Protein: " + totalProtein + ", Creatinine: " + creatinine);
                }

                // 4.13 Parse glycated hemoglobin
                AktGlycatedHemoglobin glyHemoglobin = checkData.getGluHm();
                if (glyHemoglobin != null) {
                    String hba1cNgsp = glyHemoglobin.getHba1cNgsp();
                    String hba1cIfcc = glyHemoglobin.getHba1cIfcc();
                    String hba1cEag = glyHemoglobin.getHba1cEag();

                    log.info("Glycated hemoglobin - NGSP: " + hba1cNgsp +
                            ", IFCC: " + hba1cIfcc + ", eAG: " + hba1cEag);
                }

                // 4.14 Parse hemameba data
                AktHemamebaDto hemameba = checkData.getHemameba();
                if (hemameba != null) {
                    String hemamebaValue = hemameba.getHemameba();
                    log.info("Hemameba: " + hemamebaValue);
                }

                // 4.15 Parse comprehensive biochemicals
                AktBiochemicals biochemicals = checkData.getBiochemicals();
                if (biochemicals != null) {
                    String alt = biochemicals.getAlt();
                    String ast = biochemicals.getAst();
                    String totalBilirubin = biochemicals.getTbil();
                    String directBilirubin = biochemicals.getDbil();
                    String totalProtein = biochemicals.getTp();
                    String albumin = biochemicals.getAlb();
                    String urea = biochemicals.getUrea();
                    String creatinine = biochemicals.getCre();
                    String uricAcid = biochemicals.getUa();
                    String glucose = biochemicals.getGlu();
                    String triglycerides = biochemicals.getTg();
                    String cholesterol = biochemicals.getChol();

                    log.info("Comprehensive biochemicals - ALT: " + alt + ", AST: " + ast +
                            ", Total Protein: " + totalProtein + ", Glucose: " + glucose +
                            ", Cholesterol: " + cholesterol);
                }
            }

            // 5. Validate required device and organization codes
            if (StringUtils.isEmpty(deviceCode) || StringUtils.isEmpty(orgCode)) {
                return new ThirdResultCodeDto("400", "Missing device or organization code");
            }

            // 6. Process and save the data (implement your business logic here)
            boolean processingResult = processAndSaveHealthData(aktMeasureDto);

            if (processingResult) {
                log.info("Health data processed successfully for data ID: " + dataId);
                return new ThirdResultCodeDto(SUCCESS, "Data processed successfully");
            } else {
                log.error("Failed to process health data for data ID: " + dataId);
                return new ThirdResultCodeDto(FAIL_RESULT, "Failed to process data");
            }

        } catch (Exception e) {
            log.error("Error processing health measurement data: ", e);
            return ThirdResultCodeDto.builder()
                    .resultCode(FAIL_RESULT)
                    .resultMessage("Internal server error: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Process and save all health data to database
     */
    private boolean processAndSaveHealthData(AktMeasureDto measureDto) {
        try {
            // Implement your data processing and persistence logic here
            // This could include:
            // - Saving person information to patient table
            // - Saving vital signs to vitals table
            // - Saving lab results to laboratory table
            // - Saving ECG data to ecg_waveform table
            // - etc.

            // Example processing steps:
            // 1. Check if patient exists, if not create new patient record
            // 2. Save or update person demographic information
            // 3. Save all measurement data with timestamps
            // 4. Link all data to the patient record
            // 5. Perform data validation and quality checks

            log.info("Processing and saving health data for: " + measureDto.getDataId());

            // Return true if successful, false if failed
            return true;

        } catch (Exception e) {
            log.error("Error in processAndSaveHealthData: ", e);
            return false;
        }
    }


}
