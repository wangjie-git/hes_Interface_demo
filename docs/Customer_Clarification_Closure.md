# KSY HES 7 customer clarification implementation

Source: `KSY_HES_7_疑问闭环汇总.xlsx`, Chinese and English sheets, Q01–Q43,
customer delivery package dated 2026-09-16. This file traces the agreed answers to
the demo. It does not change the workbook's reply statuses or certify a deployment.

| Questions | Agreed behavior and implementation |
| --- | --- |
| Q01–Q06 | Remove `xcg` and `AktXcgDto`, including the disputed Mid/Gran/PLCR/RON definitions, from this demo and its examples. No clinical dictionary is invented for this version. |
| Q07 | Real-sample comparison against the printed analyzer report is accepted; scheduling and collection remain integration work. |
| Q08–Q13 | `MD5(UTF8(rsId + "konsungyitijijsondata"))`, lower-case hex, no separator. The receiver configures rsId out of band and never falls back to orgCode. The constant is shared/public, not a customer secret; no key rotation. Scheduled time-key resend requires an explicit disabled-by-default compatibility option. |
| Q14, Q33 | HTTPS is supported when the receiving deployment provides a trusted certificate. README covers receiver TLS configuration; an actual environment handshake is still required. |
| Q15 | **Pending confirmation.** Dizar's actual device/peripheral list, APP build and platform deployment version are not supplied. The former union of 19 categories is not an enabled-device list. This demo contains 12 standard category definitions, four optional/custom definitions and separate report images. |
| Q16–Q17 | `xcg` and `threeWay` are not produced by this version. Remove their DTOs and examples; do not describe them as ECG. |
| Q18–Q20 | KSDB biochemistry is `bioche`; ECG is `heart` / `AktWaveForm`. Stethoscope, breathing, immune and ultrasound are optional/custom capabilities of other versions, not guarantees for Dizar. |
| Q21–Q24 | WBC total and differential use lower-case `checkData.hemameba`; retain total plus all ten differential fields in `AktHemamebaDto`. Do not map the same differential again from another category. Actual values depend on the peripheral/platform build. |
| Q25 | Retain the four `bloodSugar` fields. Document glucose/mode, uric-acid source and cholesterol source; do not populate absent values with zero or assume every meter produces all four. |
| Q26–Q31 | Remove `biochemicals` and `AktBiochemicals` from this demo and examples. Use the nine-field `bioche` object for KSDB dry biochemistry. |
| Q32 | The full destination URL is configured per rsId on KSY. The demo's local path is configurable with `ksy.receiver-path`; changing it does not update the KSY registration. |
| Q34 | Add working repository-relative documentation and sample entry points, plus a replay-copy helper. |
| Q35 | **Partially answered.** A synthetic full envelope exercises all 16 current category definitions; a standard fixture covers 12 standard paths. Neither demonstrates that every category is active for Dizar. |
| Q36 | **Partially answered.** Provide the historical 12-lead waveform fixture with provenance. Each lead contains 4,998 points; 500 Hz implies 9.996 s while historical duration is 5. A new device recording, version/context and corresponding report remain outstanding. |
| Q37 | Supply the corrected interface documentation and README; remove the obsolete binary Readme to avoid contradictory guidance. |
| Q38–Q43 | Patient-list/demographic ingestion, LIS worklists/orders and order assignment are currently unsupported. The LIS implements a result receiver; Konsung configures its URL. A result dataId is not an order/specimen ID. |

The removal of three categories applies to this customer-facing demo version. It
does not delete or migrate production/private-deployment data or code.

The receiver validates the checksum, required envelope/person fields, meaningful
checkData content, supported category shapes and conflicting ECG aliases. It does
not persist results, assign orders, deduplicate, interpret clinical measurements or
validate all per-analyzer ranges. A successful demo response explicitly states
that no database write occurred.

Run `mvn clean verify` for receiver contract tests. See the [sample guide](../demo/sample-data/README.md)
for synthetic/adapted provenance and replay commands in the [README](../README.md).

Validation for this revision: Java 8 / `mvn clean verify` (19 tests); packaged JAR
tested with a custom receiver path and rsId, three accepted fixtures, wrong-tenant
and orgCode-derived keys rejected, and the old path returning 404. Both waveform
examples retain all 12 historical lead payloads and decode to 4,998 points per
lead. Replay helper checks cover signing, fragment wrapping and overwrite refusal;
repository-relative documentation links and anchors resolve.
