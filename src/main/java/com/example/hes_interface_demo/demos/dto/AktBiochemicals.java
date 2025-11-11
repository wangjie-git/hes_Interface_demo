package com.example.hes_interface_demo.demos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 生化仪十三项指标参数
 * @author luzhichao
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AktBiochemicals {
	// Alanine Aminotransferase
	private String alt;

	// Aspartate Aminotransferase
	private String ast;

	// Total Bilirubin (TBIL)
	private String tbil;

	// Direct Bilirubin (DBIL)
	private String dbil;

	// Indirect Bilirubin (IBIL)
	private String ibil;

	// Total Protein (TP)
	private String tp;

	// Albumin (ALB)
	private String alb;

	// Globulin (GLO)
	private String glo;

	// Albumin/Globulin Ratio (A/G)
	private String ag;

	// Urea (UREA)
	private String urea;

	// Creatinine (CRE)
	private String cre;

	// Uric Acid (UA)
	private String ua;

	// Glucose (GLU)
	private String glu;

	// Triglyceride (TG)
	private String tg;

	// Cholesterol (CHOL)
	private String chol;

	// High Density Lipoprotein Cholesterol (HDL-C)
	private String hdlc;

	// Low Density Lipoprotein Cholesterol (LDL-C)
	private String ldlc;
}
