package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktXcgDto {

    /**白细胞数目**/
    private String WBC;

    /**淋巴细胞数目**/
    private String Lymph1;

    /**中性粒细胞计数**/
    private String Mid1;

    /**单核细胞计数**/
    private String Gran1;

    /**淋巴细胞比值**/
    private String Lymph2;

    /**中性粒细胞比例**/
    private String Mid2;

    /**单核细胞比例**/
    private String Gran2;

    /**红细胞计数**/
    private String RBC;

    /**血红蛋白浓度**/
    private String HGB;

    /**红细胞压积**/
    private String HCT;

    /**平均红细胞体积**/
    private String MCV;

    /**平均红细胞血红蛋白含量**/
    private String MCH;

    /**平均红细胞血红蛋白浓度**/
    private String MCHC;

    /**红细胞分布宽度-CV值**/
    private String RDW_CV;

    /**红细胞分布宽度-SD值 **/
    private String RDW_SD;

    /**血小板计数**/
    private String PLT;

    /**平均血小板体积**/
    private String MPV;

    /**血小板分布宽度**/
    private String PDW;

    /**血小板压积**/
    private String PCT;

    /**嗜酸性细胞比例**/
    private String ROEC;

    /**嗜碱性细胞比例**/
    private String POBC;

    /**嗜酸性细胞数**/
    private String EL;

    /**嗜碱性细胞数**/
    private String BL;

    /**有核红细胞比例**/
    private String PLCR;

    /**有核红细胞比例**/
    private String RON;

    /**有核红细胞数10*9/L**/
    private String NRBC;
}
