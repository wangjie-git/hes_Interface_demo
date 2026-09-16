package com.example.hes_interface_demo.demos.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 超声设备数据
 *
 * 字段清单依据官方样例 02-custom-device 逐一核对。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UltrasoundDto {

    /** 检查位置 **/
    private String position;

    /** 超声描述 **/
    private String description;

    /** 超声提示 **/
    private String hint;

    /** 超声图片 Base64 编码集合 **/
    private List<String> imageData;

}
