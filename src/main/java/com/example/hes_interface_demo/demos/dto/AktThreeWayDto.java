package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Title:AktThreeWayDto </p>
 * <p>Description: 数据上传-三分类</p>
 * <p>Company: Konsung</p>
 * @author  HWB
 * @date 2017年6月22日下午2:43:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktThreeWayDto {


    /** 小细胞群 --109/L**/
    private String smallCellGroup;

    /** 中间细胞群 --109/L**/
    private String middleCellGroup;

    /** 大细胞群 --109/L**/
    private String bigCellGroup;
}
