package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 免疫荧光项
 *
 * 字段清单依据官方样例 02-custom-device 逐一核对。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImmuneDto {

    /** FER 铁蛋白 **/
    private String fer;

    /** CRP C反应蛋白 **/
    private String crp;

    /** 25-OH-VD **/
    private String ohvd;

    /** 超敏C反应蛋白 **/
    private String hsCrp;

    /** 降钙素原 **/
    private String pct;

    /** B型钠尿肽前体 **/
    private String ntProBNP;

    /** 血清淀粉样蛋白A **/
    private String crpSaa;

    /** 中和抗体 **/
    private String cov19Nab;

    /** D-二聚体 **/
    private String dd;

}
