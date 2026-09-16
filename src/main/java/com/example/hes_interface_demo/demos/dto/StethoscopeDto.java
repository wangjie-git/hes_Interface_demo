package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 电子听诊器
 *
 * 字段清单依据官方样例 02-custom-device 逐一核对。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StethoscopeDto {

    /** 数据时长 (毫秒) **/
    private String theLength;

    /** 听诊器数据，含音频数据，Base64 加密字符串 **/
    private String stethoscopeData;

}
