# Sample messages

Start with `standard-checkdata.json`. All bundled identifiers are synthetic. Values
are parser examples, not clinical reference ranges or device-enablement evidence.

| File | Provenance and scope | POST with local rsId=konsung |
| --- | --- | --- |
| standard-checkdata.json | Synthetic values for 12 standard category paths; WBC total only; ECG parameters without recorded leads | Ready |
| compatibility-checkdata.json | Synthetic values for all 16 current category definitions, including the 11 WBC fields and four optional/custom categories | Ready; not a Dizar capability list |
| ecg-waveform-example.json | Historical published 12-lead waveform in a synthetic envelope | Ready; not a new Dizar recording |
| normal-checkdata.json | Adapted historical measurements; synthetic envelope/person; removed categories omitted | Re-sign a local copy |
| custom-device.json | Adapted historical numeric fields; synthetic envelope/person; audio and images replaced by placeholders; removed categories omitted | Re-sign a local copy |
| pulmonary-function.json | Historical category fragment, not a signed request | Wrap and re-sign with `--category breathing` |
| report-picture.json | Historical report-image shape with synthetic identifiers and 1-pixel PNG placeholders | Re-sign a local copy |

`xcg`, `threeWay` and `biochemicals` are intentionally absent from every bundled
example. Q01-Q06, Q16-Q17 and Q26-Q31 exclude them from this demo version.

The normal and ECG fixtures preserve the historical waveform bytes: all 12 leads
decode to 10,000 bytes, a four-byte big-endian point count of 4,998 and 4,998
two-byte samples. At 500 Hz this implies 9.996 seconds; the original `duration=5`
remains unexplained. Q36 still requires a current device capture with its version,
acquisition context and matching report. Do not replace this evidence with `...`
fragments or claim a synthetic fixture closes that item.

The custom audio placeholder is one silent 16-bit sample, not a playable recording
with a verified duration. The image placeholders test JSON/Base64 shape only.
Synthetic `blowgrapType=EXAMPLE-CSV` is a fixture label, not a device enum value.
Clinical media interpretation requires the actual peripheral protocol.

Original attachments remain unchanged in the supplied local customer delivery
package. These repository copies intentionally differ: identifiers and media are
adapted, and unsupported categories are removed. Historical envelope keys are
retained as negative checksum examples and are never whitelisted. Use
`tools/prepare_sample.py` to create a separate signed replay copy.

## Bundled example checksums

| File | SHA-256 |
| --- | --- |
| `compatibility-checkdata.json` | `4f166544d2bf169a36b3d893fd5244a1609e77eafa02c849c28227d98e6f8a74` |
| `custom-device.json` | `31bf4ea168b14a19149cd6fd2fcd716e7147fb28e16377d4d4e863c21e9d02d0` |
| `ecg-waveform-example.json` | `e3182c5fed063c10cfda0d7399d8f7725b5df7d2d3d03977e1b786500345fe24` |
| `normal-checkdata.json` | `f859aef15f99ea195b408f10024f994e38fc8ef7f12d8b1458a9b9280a68be6f` |
| `pulmonary-function.json` | `8ac5f46ff27e0f6f17dd3056d6caafe7a56301e14d05468cfd5ac97625682e99` |
| `report-picture.json` | `ef0c48c39ad2b4d88662b08dfcd4b6ef266d644f278e1b15f70cd093df0a2346` |
| `standard-checkdata.json` | `0eaebe687e785c3312fdae9475a227a944fa83c086dff25e982dd7a7bbf0c495` |
