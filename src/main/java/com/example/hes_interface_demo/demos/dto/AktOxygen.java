package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
* <p>Title:AktOxygen </p>
* <p>Description: 血氧数据</p>
* <p>Company: Konsung</p>
* @author  HWB
* @date 2017年4月21日下午2:48:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktOxygen {

    /**Blood oxygen saturation**/
    private String spo2;

    /**Pulse rate**/
    private String pr;
}
