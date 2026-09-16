# KSY HES Interface — Measurements Data Outgoing Interface

**Documentation revision 2026-09-16**

| Item | Value |
| --- | --- |
| Interface | KSY platform → third-party receiver (measurement results push) |
| Transport | HTTP/HTTPS `POST`, `application/json`, UTF-8 |
| Interface `version` | `1.0.0` and later are the declared values |

This interface pushes measurement results from the **Konsung Cloud System (KSY)** to a
third-party receiving system. This document defines the wire format of that exchange.

Which categories a deployment publishes depends on the attached peripheral, the
measurement performed and the platform configuration of that deployment. The field
specifications below therefore define the format of each object, not a promise that every
listed category is enabled on every system.

Optional fields must not be interpreted as zero when they are absent or empty.

---

## About this document

### Conventions

- **`Required` = `Yes` / `No`** applies to this interface only. `No` means the receiver
  must tolerate the field being absent or empty. It does **not** mean numeric zero and does
  **not** mean that a measurement was performed.
- **`—` under `Type` or `Length`** means the specification does not define a value.
- **References** are written `§X.Y` and point to sections of this document.
- **Sample payloads in §7 are synthetic or historical examples**, not captures from a
  production device. Their provenance is given in §7.2.

### Contents

| § | Section | Covers |
| --- | --- | --- |
| [1](#h2-1-overview) | Overview | Purpose, scope, implementation and demo entry point |
| [2](#h2-2-access-amp-transport) | Access & Transport | Registered receiver URL and POST/JSON transport |
| [3](#h2-3-security-key-generation-amp-validation) | Security — Key Generation & Validation | MD5 `key` computation and the scheduled-resend variant |
| [4](#h2-4-request-schema-top-level) | Request Schema — Top Level | Envelope fields, `personInfo`, the `checkData` category index |
| [5](#h2-5-detailed-parameter-specifications) | Detailed Parameter Specifications | Per-category field tables (§5.1–§5.16) |
| [6](#h2-6-report-images-optional-) | Report Images (Optional) | The optional `checkData.reportImages` structure |
| [7](#h2-7-request-examples-amp-attachments) | Request Examples & Attachments | Sample payloads and their provenance |
| [8](#h2-8-data-restoration-multimedia-amp-waveforms-) | Data Restoration (Multimedia & Waveforms) | Base64 payload decoding rules |
| [9](#h2-9-response-format) | Response Format | `resultCode` and `resultMessage` |
| [10](#h2-10-appendix-notes-amp-remarks) | Appendix — Notes & Remarks | Result handling and the patient/order boundary |
| [11](#h2-11-change-log) | Change Log | Documentation revision history |

---

<a id="h2-1-overview"></a>

## 1. Overview

### 1.1 Purpose

This interface enables third-party platforms to receive **real-time updates** from the **Konsung Cloud System (KSY)** through HTTP POST requests with JSON payloads.

### 1.2 Scope

**Direction of delivery:** KSY platform → third-party receiver.

The standard APP → platform upload is a *different* schema and is **not** the receiving interface described here.

### 1.3 Implementation

Third parties must implement an HTTP endpoint that:

1. accepts `POST` + JSON as described in this document, and
2. validates the published checksum as described in §3.

```text
KSY platform -- HTTP/HTTPS POST results --> LIS receiver
```

### 1.4 Demo Example and Reference Implementation

To help developers quickly integrate and test this interface, a full demo project is available on GitHub:

**GitHub Repository:**
👉 [https://github.com/wangjie-git/hes_Interface_demo.git](https://github.com/wangjie-git/hes_Interface_demo.git)

This demo includes:

- Example request and response bodies
- Mock HTTP receiver for testing KSY data push
- Signature generation and verification code
- Complete environment configuration for quick deployment

The GitHub repository is the **upstream example project**. An offline runnable copy of the
same receiver example is also available, so integration work does not depend on network
access to GitHub.

The accompanying [Demo Receiver — Run & Test Guide](../README.md)
is the runnable entry point, and the sample provenance is given in §7.2.

The demo parses both standard and compatibility structures. It does **not** perform
clinical interpretation, configure KSY, or persist records.

<a id="h2-2-access-amp-transport"></a>

## 2. Access & Transport

### 2.1 Request URL format

The receiver supplies a complete URL, for example:

```text
https://receiver.example.com/ksy/results
```

Konsung registers that URL for the integration tenant (`rsId`). Neither
`/publicHealth/dataInterchange` nor the demo's `/parsingHesData` is a mandatory path.
Changing the demo's listening path does not update the platform registration.

### 2.2 Transport

| Property | Value |
| --- | --- |
| Method | `POST` |
| Content-Type | `application/json` |
| Encoding | UTF-8 |

- The platform HTTP client selects HTTP or HTTPS from the registered URL.
  HTTPS uses the runtime's TLS configuration and certificate trust store.
- An HTTPS URL requires a valid certificate chain on the receiving side; the handshake
  must be verified in the target environment. This document does not mandate a particular
  TLS version or cipher suite.
- Agree the registered URL and source IPs with the deployment owner. The MD5 value
  in §3 is **not** a substitute for transport security.

<a id="h2-3-security-key-generation-amp-validation"></a>

## 3. Security — Key Generation & Validation

### 3.1 Key generation

For real-time platform pushes:

```text
key = lowercase_hex(MD5(UTF8(rsId + "konsungyitijijsondata")))
```

| Input | Value for the published test tenant |
| --- | --- |
| `rsId` | `konsung` |
| Resulting `key` | `87a81d8edfe6b43aa8763d7147f6245a` |

Concatenate **without a separator**.

- `rsId` is the integration/routing identifier assigned by Konsung.
- `orgCode` identifies an organization *within* the integration and must **not** be
  substituted for `rsId`.
- The push body does not contain `rsId`; configure it on the receiving side out of band.
  The demo's local test value is `ksy.rsid=konsung`.

`single` is the public constant shown above. It is **shared by all integrations and is not
a customer-specific secret**: there is no separate secret-delivery or secret-rotation
mechanism. The MD5 checksum does not authenticate the message contents and does not
prevent replay. A per-integration key would be a separate protocol change.

#### 3.1.1 Scheduled resend compatibility

The platform also contains a scheduled resend path that computes
`MD5(time + single)`, using the exact `time` string in that message.

| Behavior | Detail |
| --- | --- |
| Demo default | Rejects the time-based variant |
| Enablement | Set `ksy.accept-legacy-time-key=true` **only after** the sender's resend behavior has been confirmed |
| Accepted keys once enabled | The configured `rsId` formula, or that exact `time` formula — never an arbitrary or whitelisted key |
| Cannot validate | A missing `time`, or a `time` that changed after signing |

Keys embedded in historical attachments are not test vectors for your tenant. Recompute a
local copy for replay as described in §7; do not weaken validation to accept them.

<a id="h2-4-request-schema-top-level"></a>

## 4. Request Schema — Top Level

### 4.1 Top-level fields

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `dataId` | String | 32 | Yes | Unique data ID for this record |
| `orgCode` | String | 32 | Yes | Organization code (provided by third party) |
| `deviceCode` | String | 32 | Yes | Device identifier |
| `doctorCode` | String | 32 | Yes | Operator (doctor) code |
| `checkDate` | String | — | Yes | Measurement datetime. Format: `yyyy-MM-dd HH:mm:ss` |
| `version` | String | 20 | Yes | Interface version (e.g., `1.0.0`) |
| `deviceVersion` | String | 20 | No | Legacy version metadata; the APP/firmware build is reported separately |
| `personInfo` | Object | — | Yes | Patient info object (see §4.2) |
| `checkData` | Object | — | Yes | Measurement data object (see §4.3) |
| `time` | String | — | Yes | Data transmission time |
| `key` | String | — | Yes | Published checksum (§3); not a private key |

A complete POST body that uses these fields is shown in §7.1.

### 4.2 `personInfo` — Patient information

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `idNumber` | String | 18 | No | National ID (optional per region) |
| `otherNumber` | String | 32 | No | Other certificate ID |
| `name` | String | 20 | Yes | Patient name |
| `sexCode` | String | 1 | Yes | 0=Unknown, 1=Male, 2=Female, 9=Unspecified |
| `birthdayDate` | String | — | No | Format: `yyyy-MM-dd` |
| `healthNumber` | String | 32 | No | Health file number |
| `height` | String | 10 | No | Unit: cm |
| `weight` | String | 10 | No | Unit: kg |
| `bmi` | String | 10 | No | Body mass index |
| `waist` | String | 10 | No | Unit: cm |
| `hipline` | String | 10 | No | Unit: cm |

### 4.3 `checkData` — Measurement data container

The table lists each receiver key, the APP source object that supplies it, and the scope of
that category in the current version. Whether a key carries a value depends on the attached
peripheral, the measurement performed and the deployed platform forwarding path. Empty or
null objects may be emitted by legacy serializers; **an empty object is not a measured
result**.

| Receiver key | APP source | Scope | Ref |
| --- | --- | --- | --- |
| `heart` | `uwd`, KSM5 | Standard | §5.1 |
| `bloodSugar` | `ubsud` plus top-level `chol` | Standard; populated items depend on the measurement | §5.2 |
| `oxygen` | `uod`, KSM5 | Standard | §5.3 |
| `bloodPressure` | `ubpd`, KSM5 | Standard | §5.4 |
| `temperature` | `temp` | Standard | §5.5 |
| `routineUrine` | `uud` | Standard, with the matching urine analyzer | §5.6 |
| `hemoglobin` | `uhd` | Standard, with the matching hemoglobin meter | §5.7 |
| `bloodLipidFour` | `ubsd` | Standard, with the matching lipid analyzer | §5.8 |
| `babyHeart` | `ufhd`, VCOMIN | Standard, with the matching fetal-heart monitor | §5.9 |
| `bioche` | `ubb`, KSDB | Standard, dry biochemistry; nine receiver fields | §5.10 |
| `gluHm` | `ughd`, UNT5000 | Standard, with the matching HbA1c analyzer | §5.11 |
| `hemameba` | `hemameba`, KSWbc/WBC6/HemoCue WBC | Standard for the WBC total; the differential fields depend on the peripheral and the platform build | §5.12 |
| `stethoscope` | `stethoscope`, MINTTI | Optional, with the matching electronic stethoscope | §5.13 |
| `breathing` | `breathing`, HBreath/PULMO | Optional, with the matching spirometer | §5.14 |
| `immune` | `immune`, NepQDV1/Q100/KS5902Fia/KSIF | Optional, with the matching immunofluorescence analyzer | §5.15 |
| `ultrasound` | `ultrasound`, HEALSON | Optional, with the matching ultrasound device | §5.16 |

`reportImages` is a separate optional attachment structure (§6), **not** another
laboratory category.


Dizar's actual device/peripheral list, APP build and platform deployment version remain
pending confirmation (Q15). The former 19-category union included `xcg`, `threeWay`
and `biochemicals`; these are not produced by this version and are removed from this
demo. The 16 definitions below do not mean all categories are enabled for Dizar.
The last four are custom/optional capabilities of other versions (Q19).

### 4.4 Category index at a glance

| # | Category | Device / peripheral | Status | Detail |
| --- | --- | --- | --- | --- |
| 1 | `heart` | ECG (`uwd`, KSM5) | Standard | [§5.1](#51-ecg-checkdataheart) |
| 2 | `bloodSugar` | Glucose / cholesterol (`ubsud`) | Standard | [§5.2](#52-blood-glucose-checkdatabloodsugar) |
| 3 | `oxygen` | SpO₂ (`uod`, KSM5) | Standard | [§5.3](#53-spo₂-checkdataoxygen) |
| 4 | `bloodPressure` | NIBP (`ubpd`, KSM5) | Standard | [§5.4](#54-blood-pressure-nibp-checkdatabloodpressure) |
| 5 | `temperature` | Thermometer (`temp`) | Standard | [§5.5](#55-temperature-checkdatatemperature) |
| 6 | `routineUrine` | Urine analyzer (`uud`) | Standard | [§5.6](#56-urine-routine-checkdataroutineurine) |
| 7 | `hemoglobin` | Hemoglobin meter (`uhd`) | Standard | [§5.7](#57-hemoglobin-checkdatahemoglobin) |
| 8 | `bloodLipidFour` | Lipid analyzer (`ubsd`) | Standard | [§5.8](#58-blood-lipids-checkdatabloodlipidfour) |
| 9 | `babyHeart` | Fetal heart (`ufhd`, VCOMIN) | Standard | [§5.9](#59-fetal-heart-checkdatababyheart) |
| 10 | `bioche` | KSDB dry biochemistry (`ubb`) | Standard (9 fields) | [§5.10](#510-biochemistry-checkdatabioche) |
| 11 | `gluHm` | HbA1c (`ughd`, UNT5000) | Standard | [§5.11](#511-glycated-hemoglobin-checkdatagluhm) |
| 12 | `hemameba` | WBC (`hemameba`, KSWbc/WBC6/HemoCue) | Standard for the total; differential depends on peripheral/build | [§5.12](#512-wbc--hemameba-checkdatahemameba) |
| 13 | `stethoscope` | Electronic stethoscope (MINTTI) | Optional peripheral | [§5.13](#513-electronic-stethoscope-checkdatastethoscope) |
| 14 | `breathing` | Spirometer (HBreath/PULMO) | Optional peripheral | [§5.14](#514-spirometer--pulmonary-function-checkdatabreathing) |
| 15 | `immune` | Immunofluorescence (NepQDV1/Q100/…) | Optional peripheral | [§5.15](#515-immunofluorescence-checkdataimmune) |
| 16 | `ultrasound` | Ultrasound (HEALSON) | Optional peripheral | [§5.16](#516-ultrasound-checkdataultrasound) |

<a id="h2-5-detailed-parameter-specifications"></a>

## 5. Detailed Parameter Specifications

> The sections below list fields for each data type. Optional fields are marked `No` under Required.

Each table uses the same five columns — `Field`, `Type`, `Length`, `Required`, `Remarks`.
Categories that depend on an optional peripheral are noted individually.

### 5.1 ECG (`checkData.heart`)

**Reference:** §8.1; waveform fixture in §7.4.1.

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `anal` | String | 200 | No | ECG diagnosis result (diagnostic result text) |
| `hr` | String | 3 | Yes | HR (heart rate), beats/min |
| `resp_rr` | String | 3 | Yes | Respiratory rate, rpm |
| `sample` | String | 5 | Yes | Waveform sampling rate |
| `p05` | String | 5 | Yes | Value at +0.5 mV |
| `n05` | String | 5 | Yes | Value at -0.5 mV |
| `duration` | String | 5 | Yes | Waveform duration |
| `PR` | String | 10 | Yes | PR interval, ms |
| `QRS` | String | 10 | Yes | QRS interval, ms |
| `QT` | String | 10 | Yes | QT interval, ms |
| `QTC` | String | 10 | Yes | QTC interval, ms |
| `P` | String | 10 | Yes | P axis, degrees |
| `QRSZ` | String | 10 | Yes | QRS axis, degrees |
| `T` | String | 10 | Yes | T axis, degrees |
| `RV5` | String | 10 | Yes | V5 amplitude, mV |
| `SV1` | String | 10 | Yes | V1 amplitude, mV |
| `ecg_i` | clob/base64 | — | No | ECG lead I waveform; see §8.1 |
| `ecg_ii` | clob/base64 | — | No | ECG lead II waveform; see §8.1 |
| `ecg_iii` | clob/base64 | — | No | ECG lead III waveform; see §8.1 |
| `ecg_avr` | clob/base64 | — | No | aVR waveform; see §8.1 |
| `ecg_avf` | clob/base64 | — | No | aVF waveform; see §8.1 |
| `ecg_avl` | clob/base64 | — | No | aVL waveform; see §8.1 |
| `ecg_v1` .. `ecg_v6` | clob/base64 | — | No | V1–V6 waveforms; see §8.1 |

> **Case sensitivity of the interval/axis keys**
>
> Historical attachments spell the interval/axis keys `PR`, `QRS`, `QT`, `QTC`, `P`,
> `QRSZ`, `T`, `RV5`, `SV1`; some Java serializers emit lower-case forms.
>
> - The demo accepts **both** and preserves the values.
> - A receiver must **not** silently discard these fields.
> - If both spellings occur with conflicting values, treat that message as ambiguous.
>
> Waveform bytes follow §8.1. The declared `duration` may not equal the value implied by
> `sample` × points; confirm its intended semantics before use.

### 5.2 Blood Glucose (`checkData.bloodSugar`)

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `glu` | String | — | No | Glucose, from APP `ubsud.glu`; historical unit mmol/L |
| `gluStyle` | String | — | No | 0=fasting, 1=postprandial; from `ubsud.gluStyle` |
| `uricacid` | String | — | No | Uric acid, mapped from APP `ubsud.bsPh`; do not infer its meaning from `bsPh` alone |
| `xzzdgc` | String | — | No | Total cholesterol, from APP top-level `chol` (BeneCheck PLUS JET) |

> **Note:** All four receiver fields are defined; they are **not** guaranteed to have a
> value in every message. The exact meter mode and unit for `uricacid` should be confirmed
> against the corresponding measurement report before a clinical mapping is applied. No
> value is a constant and absence must not be converted to zero.
>
> Cholesterol in a lipid panel or a biochemistry panel may be a **different** measurement.
> Do not unconditionally override it with `xzzdgc` or merge independent measurements.

### 5.3 SpO₂ (`checkData.oxygen`)

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `spo2` | String | 3 | No | Blood oxygen saturation, % |
| `pr` | String | 3 | No | Pulse rate, bpm |

### 5.4 Blood Pressure (NIBP) (`checkData.bloodPressure`)

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `sbp` | String | 3 | No | Systolic (mmHg) |
| `dbp` | String | 3 | No | Diastolic (mmHg) |
| ~~`mbp`~~ | ~~String~~ | ~~4~~ | ~~No~~ | ~~Mean BP (deprecated)~~ |
| ~~`pr`~~ | ~~String~~ | ~~8~~ | ~~No~~ | ~~Pulse rate (deprecated)~~ |
| (other left/right fields) | — | — | No | Deprecated/optional — removed from strict validation |

### 5.5 Temperature (`checkData.temperature`)

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `temp` | String | 6 | No | Body temperature, ℃ |

### 5.6 Urine Routine (`checkData.routineUrine`)

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `urinePh` | String | 6 | No | pH |
| `urineUbg` | String | 10 | No | UBG |
| `urineBld` | String | 10 | No | Occult blood |
| `urinePro` | String | 10 | No | Protein |
| `urineKet` | String | 10 | No | Ketones |
| `urineNit` | String | 10 | No | Nitrite |
| `urineGlu` | String | 10 | No | Urine glucose |
| `urineBil` | String | 10 | No | Bilirubin |
| `urineLeu` | String | 10 | No | Leukocytes |
| `urineSg` | String | 10 | No | Specific gravity |
| `urineVc` | String | 10 | No | Vitamin C |
| `urineCre` | String | 10 | No | Creatinine (mmol/L) |
| `urineCa` | String | 10 | No | Calcium (mmol/L) |
| `urineMa` | String | 10 | No | Microalbumin |

### 5.7 Hemoglobin (`checkData.hemoglobin`)

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `assxhdb` | String | 12 | No | Hemoglobin (g/L) |
| `htc` | String | 12 | No | Hematocrit (%) |

### 5.8 Blood Lipids (`checkData.bloodLipidFour`)

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `flipidsChol` | String | 12 | No | TC (mmol/L) |
| `flipidsTrig` | String | 12 | No | TG (mmol/L) |
| `flipidsHdl` | String | 12 | No | HDL-C (mmol/L) |
| `flipidsLDL` | String | 12 | No | LDL-C (mmol/L) |
| `flipidsVld` | String | 12 | No | VLDL (mmol/L) |

### 5.9 Fetal Heart (`checkData.babyHeart`)

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `fetalHeartNum` | String | 10 | No | Fetal HR (beats) |

### 5.10 Biochemistry (`checkData.bioche`)

> **Note:** The KSDB dry-biochemistry analyzer uploads the `ubb` object, and the platform
> maps **nine** of its fields to `bioche`. Additional APP-side measurements do not
> automatically become receiver fields.

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `alanine` | String | 10 | No | ALT (U/L) |
| `totalBilirubin` | String | 10 | No | TBIL (μmol/L) |
| `aspartate` | String | 10 | No | AST (U/L) |
| `directBilirubin` | String | 10 | No | DBIL (μmol/L) |
| `totalProtein` | String | 10 | No | TP (g/L) |
| `albumin` | String | 10 | No | ALB (g/L) |
| `uricAcid` | String | 10 | No | UA (μmol/L) |
| `creatinine` | String | 10 | No | CREA (μmol/L) |
| `urea` | String | 10 | No | Urea (mmol/L) |

### 5.11 Glycated Hemoglobin (`checkData.gluHm`)

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `hba1cNgsp` | String | 10 | No | HbA1c, % (NGSP) |
| `hba1cIfcc` | String | 10 | No | HbA1c, mmol/mol (IFCC) |
| `hba1cEag` | String | 10 | No | eAG (mmol/L) |

### 5.12 WBC / Hemameba (`checkData.hemameba`)

> **Naming:** The category and total-field spelling are both lower-case —
> `checkData.hemameba.hemameba`. Differential values, when produced by the peripheral, are
> uploaded in `hemameba`.
>
> **Scope:** The platform forwards the WBC **total** for this category; historical
> messages contain all eleven fields. The ten differential fields below are accepted by the
> demo and their availability depends on the peripheral and the deployed platform build.
> Do not substitute another category as a workaround, and do not assume that every peripheral
> produces all eleven values. For differential results, map these explicitly named fields
> only after the received values have been verified against the corresponding analyzer
> report.

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `hemameba` | String | 10 | No | WBC (*10^9/L) |
| `wbcLym` | String | 10 | No | Lymphocytes (*10^9/L) |
| `wbcMon` | String | 10 | No | Monocytes (*10^9/L) |
| `wbcNeu` | String | 10 | No | Neutrophils (*10^9/L) |
| `wbcEos` | String | 10 | No | Eosinophils (*10^9/L) |
| `wbcBas` | String | 10 | No | Basophils (*10^9/L) |
| `wbcLymPercent` | String | 10 | No | Lymphocytes, % |
| `wbcMonPercent` | String | 10 | No | Monocytes, % |
| `wbcNeuPercent` | String | 10 | No | Neutrophils, % |
| `wbcEosPercent` | String | 10 | No | Eosinophils, % |
| `wbcBasPercent` | String | 10 | No | Basophils, % |

### 5.13 Electronic Stethoscope (`checkData.stethoscope`)

> **Scope:** These fields are available when the matching electronic stethoscope is
> attached and the deployed platform enables this category.

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `theLength` | String | 10 | No | Duration; confirm the unit for the sending device (historical document: ms; APP: s) |
| `stethoscopeData` | String (Base64) | — | No | Base64 audio data; see §8.2 |

### 5.14 Spirometer / Pulmonary Function (`checkData.breathing`)

> **Scope:** These fields are available when the matching spirometer is attached and the
> deployed platform enables this category.

**Top-level pulmonary fields**

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `predPef` | String | 10 | Yes | Predicted PEF (L/s) |
| `pefUnit` | String | 10 | Yes | Unit (L/s) |
| `predPefr` | String | 10 | Yes | Predicted PEFr (L/min) |
| `pefrUnit` | String | 10 | Yes | Unit (L/min) |
| `predFev1` | String | 10 | Yes | Predicted FEV1 (L) |
| `fev1Unit` | String | 10 | Yes | Unit (L) |
| `predFvc` | String | 10 | Yes | Predicted FVC (L) |
| `fvcUnit` | String | 10 | Yes | Unit (L) |
| `predMef75` | String | 10 | Yes | Predicted MEF75 (L/s) |
| `mef75Unit` | String | 10 | Yes | Unit (L/s) |
| `predMef50` | String | 10 | Yes | Predicted MEF50 (L/s) |
| `mef50Unit` | String | 10 | Yes | Unit (L/s) |
| `predMef25` | String | 10 | Yes | Predicted MEF25 (L/s) |
| `mef25Unit` | String | 10 | Yes | Unit (L/s) |
| `predMmef` | String | 10 | Yes | Predicted MMEF (L/s) |
| `mmefUnit` | String | 10 | Yes | Unit (L/s) |
| `predFev1Fvc` | String | 10 | Yes | Predicted FEV1/FVC (%) |
| `fev1FveUnit` | String | 10 | Yes | Unit of FEV1/FVC, % — **the field spelling is `fve`, not `fvc`** |
| `details` | List | — | No | Array of single-test objects (see §5.14.1) |

#### 5.14.1 Single pulmonary function record (elements of `details`)

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `pef` | String | 10 | Yes | PEF (L/s) |
| `pefr` | String | 10 | Yes | PEFr (L/min) |
| `fev1` | String | 10 | Yes | FEV1 (L) |
| `fvc` | String | 10 | Yes | FVC (L) |
| `fev1Fvc` | String | 10 | Yes | FEV1/FVC (%) |
| `mef75` | String | 10 | Yes | MEF75 (L/s) |
| `mef50` | String | 10 | Yes | MEF50 (L/s) |
| `mef25` | String | 10 | Yes | MEF25 (L/s) |
| `mmef` | String | 10 | Yes | MMEF (L/s) |
| `blowgrap` | String | — | Yes | Best respiration curve; see §8.3 |
| `blowgrapType` | String | 10 | No | Curve encoding/type discriminator accompanying `blowgrap`; see §8.3 |
| `indicatorPef` | String | 10 | Yes | PEF %pred (%) |
| `indicatorPefr` | String | 10 | Yes | PEFr %pred (%) |
| `indicatorFev1` | String | 10 | Yes | FEV1 %pred (%) |
| `indicatorFvc` | String | 10 | Yes | FVC %pred (%) |
| `indicatorFev1Fvc` | String | 10 | Yes | FEV1/FVC %pred (%) |
| `indicatorMef75` | String | 10 | Yes | MEF75 %pred (%) |
| `indicatorMef50` | String | 10 | Yes | MEF50 %pred (%) |
| `indicatorMef25` | String | 10 | Yes | MEF25 %pred (%) |
| `indicatorMmef` | String | 10 | Yes | MMEF25-75 %pred (%) |

### 5.15 Immunofluorescence (`checkData.immune`)

> **Scope:** These fields are available when the matching immunofluorescence analyzer is
> attached and the deployed platform enables this category.

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `fer` | String | 10 | No | Ferritin (ng/mL) |
| `crp` | String | 10 | No | C-reactive protein (mg/L) |
| `ohvd` | String | 10 | No | 25-OH Vit D; the APP reports nmol/L after conversion |
| `hsCrp` | String | 10 | No | hs-CRP (mg/L) |
| `pct` | String | 10 | No | Procalcitonin (ng/mL) |
| `ntProBNP` | String | 10 | No | NT-proBNP (pg/mL) |
| `crpSaa` | String | 10 | No | Serum amyloid A (mg/L) |
| `cov19Nab` | String | 10 | No | COVID-19 neutralization Ab (IU/mL) |
| `dd` | String | 10 | No | D-dimer (mg/L) |

### 5.16 Ultrasound (`checkData.ultrasound`)

> **Scope:** These fields are available when the matching ultrasound device is attached and
> the deployed platform enables this category.

| Field | Type | Length | Required | Remarks |
| --- | --- | --- | --- | --- |
| `position` | String | 10 | Yes | Exam position |
| `description` | String | — | Yes | Ultrasonic description |
| `hint` | String | — | Yes | Ultrasonic prompt |
| `imageData` | List of Base64 strings | — | No | Array of Base64 images; see §8.4 |

<a id="h2-6-report-images-optional-"></a>

## 6. Report Images (Optional)

Report-image attachments are placed at `checkData.reportImages` and use the four `Bse64`
keys below (String/Base64). Report images are optional and depend on the deployed
configuration; confirm availability for the deployment before relying on them. The sample
in §7.4 is a historical reference fixture.

### 6.1 `reportImages` schema (optional fields)

Location: `checkData.reportImages`.

| Field | Type | Required | Remarks |
| --- | --- | --- | --- |
| `ecgReportBse64` | String (Base64) | No | ECG report image |
| `fetalHeartReportBse64` | String (Base64) | No | Fetal-heart report image |
| `breathReportBse64` | String (Base64) | No | Pulmonary-function report image |
| `ultrasoundReportBse64` | String (Base64) | No | Ultrasound report image |

<a id="h2-7-request-examples-amp-attachments"></a>

## 7. Request Examples & Attachments

### 7.1 Full standard exam example

A complete POST body is an envelope (§4) plus a `checkData` object (§4.3). Reduced to
three categories for readability, it looks like this:

```json
{
  "dataId": "SYNTHETIC-STANDARD",
  "orgCode": "DEMO-ORG",
  "deviceCode": "DEMO-DEVICE",
  "doctorCode": "DEMO-DOCTOR",
  "checkDate": "2026-09-16 10:00:00",
  "version": "1.0.0",
  "deviceVersion": "1.0.0",
  "time": "2026-09-16 10:00:01",
  "key": "87a81d8edfe6b43aa8763d7147f6245a",
  "personInfo": {
    "name": "SYNTHETIC TEST",
    "otherNumber": "DEMO-PERSON",
    "sexCode": "0"
  },
  "checkData": {
    "bloodPressure": {
      "sbp": "120",
      "dbp": "80"
    },
    "oxygen": {
      "spo2": "98",
      "pr": "72"
    },
    "temperature": {
      "temp": "36.5"
    }
  }
}
```

`key` above is the `rsId=konsung` test value from §3.1, not a tenant secret.

### 7.2 Bundled sample files

| File | Provenance and scope | Use | Ready for POST with local `rsId=konsung`? |
| --- | --- | --- | --- |
| [standard-checkdata.json](../demo/sample-data/standard-checkdata.json) | **Synthetic**; `rsId=konsung`; covers the standard category paths, WBC **total only** | Strict local checksum and parser testing | Yes |
| [normal-checkdata.json](../demo/sample-data/normal-checkdata.json) | Historical measurements with synthetic identifiers; removed categories omitted | Historical reference | Re-sign a local copy |
| [custom-device.json](../demo/sample-data/custom-device.json) | Historical numeric fields with synthetic identifiers and media placeholders | Reference only; not a device-enablement list | Re-sign a local copy |
| [compatibility-checkdata.json](../demo/sample-data/compatibility-checkdata.json) | **Synthetic parser fixture** covering the 16 current category definitions | Parser coverage only | Yes |
| [pulmonary-function.json](../demo/sample-data/pulmonary-function.json) | Historical category fragment — **not** a complete signed POST body | Wrap it in a signed envelope for parser tests | Wrap and re-sign a local copy |
| [ecg-waveform-example.json](../demo/sample-data/ecg-waveform-example.json) | Historical published waveform block in an example envelope | ECG waveform decoding checks | Yes |
| [report-picture.json](../demo/sample-data/report-picture.json) | Synthetic image placeholders in the historical envelope shape | Report-image shape reference (§6) | Re-sign a local copy |

These files are provided to test parsing; they are not clinical references and their values
must never be imported as patient results.

The table distinguishes adapted historical examples, synthetic fixtures and replay copies.
Historical keys are preserved for reference and are not whitelisted by the receiver.

`normal-checkdata.json` contains twelve nonempty ECG leads: each decodes to 10,000 bytes,
with a four-byte point count of 4,998 and 4,998 two-byte samples. At 500 Hz that is
9.996 seconds, while the message declares `duration=5`; the intended semantics of
`duration` should be confirmed with the sender.

Examples that contain `...` inside Base64 strings are explanatory fragments, not complete
decodable captures.

**Bundled example checksums**

These are adapted examples. The original attachments remain in the supplied local
delivery package; see [sample provenance](../demo/sample-data/README.md).

| File | SHA-256 |
| --- | --- |
| `normal-checkdata.json` | `f859aef15f99ea195b408f10024f994e38fc8ef7f12d8b1458a9b9280a68be6f` |
| `custom-device.json` | `31bf4ea168b14a19149cd6fd2fcd716e7147fb28e16377d4d4e863c21e9d02d0` |
| `pulmonary-function.json` | `8ac5f46ff27e0f6f17dd3056d6caafe7a56301e14d05468cfd5ac97625682e99` |
| `report-picture.json` | `ef0c48c39ad2b4d88662b08dfcd4b6ef266d644f278e1b15f70cd093df0a2346` |

### 7.3 Pulmonary function JSON example

See [pulmonary-function.json](../demo/sample-data/pulmonary-function.json) in the table
above (§7.2). To replay it, wrap and re-sign a local copy; the endpoint accepts only
complete signed envelopes.

### 7.4 Report images example JSON

[report-picture.json](../demo/sample-data/report-picture.json) illustrates the historical
image shape with synthetic placeholders. Its availability and field shape remain deployment-dependent (§6).

#### 7.4.1 ECG example and provenance

[ecg-waveform-example.json](../demo/sample-data/ecg-waveform-example.json) copies the
waveform block from the historical normal attachment into an example envelope with a
recomputed test key.

| Observation | Value |
| --- | --- |
| Leads that decode | All 12 |
| Points per lead | 4,998 |
| Declared `sample` | 500 |
| Implied duration from `sample` × points | 9.996 s |
| Declared `duration` | 5 |
| Provenance | Historical published waveform fixture |

The declared `duration` does not match the value implied by `sample` × points. Confirm the
intended semantics of `duration` with the sender before using the field.

<a id="h2-8-data-restoration-multimedia-amp-waveforms-"></a>

## 8. Data Restoration (Multimedia & Waveforms)

> These are the original format descriptions retained for reference. ECG byte framing is
> verified against the historical attachment; the other media parameters require the
> applicable peripheral protocol. Do not infer physical units or a device format solely
> from a historical example.

### 8.1 Waveform sampling value restoration (ECG / general waveform)

1. Decode the Base64 string to a byte stream.
2. The **first 4 bytes** are a 32-bit unsigned integer (big-endian) — this is the **number of sample points**.
3. Each **subsequent 2 bytes** is a 16-bit unsigned integer (big-endian) — each represents one sample value.

### 8.2 Stethoscope waveform (audio) restoration

1. Receive the Base64 string (often compressed or raw PCM encoded).
2. Base64-decode to a binary PCM stream.
3. Save the PCM stream to a file (raw PCM).
4. Add a WAV header (Channels, SampleRate, BitsPerSample) and convert the PCM file to `.wav`.

**Audio parameters**

| Parameter | Value |
| --- | --- |
| Channels | 1 (mono) |
| Sample rate | 8000 Hz |
| Bits per sample | 16 bits |

### 8.3 Pulmonary function curve parameter restoration

1. The historical description refers to 600 16-bit points; the APP also shows a comma-separated curve. Do not assume the two encodings are interchangeable; confirm the encoding in use.
2. Decode only after the active device curve encoding, scale and units are confirmed.
3. `blowgrapType` (§5.14.1) is the accompanying discriminator for the curve encoding where the sender emits it; confirm its allowed values per device version.

### 8.4 Ultrasound image restoration

1. Decode the Base64 string to binary image bytes.
2. Save as an image file (JPEG/PNG) according to the embedded format.

### 8.5 Fetal heart rate curve restoration

**To be determined.** Follow the same pattern:

```text
Base64 → bytes → numeric sequence → plotting
```

<a id="h2-9-response-format"></a>

## 9. Response Format

### 9.1 Standard response example

```json
{
  "resultCode": "10000",
  "resultMessage": ""
}
```

### 9.2 Return field definitions

| Field | Type | Remarks |
| --- | --- | --- |
| `resultCode` | String | Operation result: `10000` = Success, `00000` = Failure |
| `resultMessage` | String | Failure reason or extra info; text message when `resultCode` !== `10000` |

A production receiver returns `10000` **only after** it has successfully processed and
persisted the record — HTTP 200 alone is insufficient. The demo reports
"Demo validation only; no database write" and is not a persistence implementation.

<a id="h2-10-appendix-notes-amp-remarks"></a>

## 10. Appendix — Notes & Remarks

- Preserve the distinction between absent, null, empty string and numeric zero.
- `checkDate` and `time` contain no timezone offset; agree the deployment timezone.
- A record can arrive again. Use the integration context and `dataId` to recognize
  repeat deliveries and updates; do not confuse checksum validation with deduplication.
- Values from separate instruments/panels are not duplicates merely because their
  analyte names match. Agree result provenance before applying a precedence rule.

### 10.1 Patient and test-order workflow

The push documented here delivers measurement results from KSY to the receiving system.
The resident-information query documented separately reads resident information **from
KSY**; it does not receive a worklist. Basic data synchronization manages organizations
and doctors, **not** laboratory orders.

The current KSY workflow does **not** support (Q38-Q43):

- patient-list or demographic ingestion into the device,
- receipt of test orders or worklists, or
- assignment of a test order to a patient.

`dataId` identifies a **result** record, not an order; the result object has no explicit
order or specimen identifier. Returning a measurement therefore does not by itself
establish order correlation. A receiving system that needs order correlation must perform
it on its own side.

<a id="h2-11-change-log"></a>

## 11. Change Log

| Documentation revision | Change |
| --- | --- |
| 1.0.0 — 2025-11-11 | First published version of this interface document |
| 2026-09-16 | Clarified the checksum inputs (`rsId` with the public constant) and the receiver-URL registration; added the `bioche` field table; clarified the WBC/`hemameba` scope and key casing; corrected the category list and removed the structures the current version does not produce, together with their references; added request examples and sample payloads; corrected the pulmonary `fev1FveUnit` spelling and documented `blowgrapType`. |

This document describes the interface format only. It is not a service release and does
not change the on-wire `version` value.