package com.example.hes_interface_demo.demos.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Report images.
 *
 * Historical samples use the misspelling in the four keys: {@code Bse64}, not {@code Base64}.
 * Target-deployment support and spelling must be confirmed; this DTO is compatibility only.
 * Each value is a Base64-encoded image carried as a single string.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportImagesDto {

    /** ECG report image, Base64 (note: Bse64) **/
    private String ecgReportBse64;

    /** Ultrasound report image, Base64 (note: Bse64) **/
    private String ultrasoundReportBse64;

    /** Spirometry report image, Base64 (note: Bse64) **/
    private String breathReportBse64;

    /** Fetal-heart report image, Base64 (note: Bse64) **/
    private String fetalHeartReportBse64;
}
