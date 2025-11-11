package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Title:UploadHemamebaDto </p>
 * <p>Description: 数据上传--白细胞数据</p>
 * <p>Company: Konsung</p>
 * @author  HWB
 * @date 2017年7月24日下午2:43:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktHemamebaDto {
    /**White blood cell**/
    private String hemameba;
}
