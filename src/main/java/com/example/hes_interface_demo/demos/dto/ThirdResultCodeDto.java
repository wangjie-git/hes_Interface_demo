package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThirdResultCodeDto {

    /**
     * Result code returned by third-party interface
     */
    private String resultCode;

    /**
     * Result message returned by third-party interface
     */
    private String resultMessage;

}
