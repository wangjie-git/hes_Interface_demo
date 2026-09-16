# KSY HES interface demo

This is a receiver example accompanying the corrected existing interface documentation
(2026-09-16). The production service is not changed by this demo revision.

Read [the interface documentation](docs/KSY_Data_Interchange_Interface.md) and
[the original interface directory](docs/00-Preface.md). The receiver supports optional
and historical structures for compatibility; that does not mean every structure is
enabled in the standard APP or in Dizar's deployment.

## Run and test

Requires Java 8 and Maven. From this directory:

```bash
mvn clean verify
java -jar target/hes_Interface_demo.jar
```

Default local configuration: port 8080, `ksy.rsid=konsung`. For a real integration,
configure the tenant assigned by Konsung, for example:

```bash
java -jar target/hes_Interface_demo.jar --ksy.rsid=YOUR_ASSIGNED_RSID
```

The configured tenant is not inferred from the message's `orgCode`.
POST a signed synthetic local example (on Windows use `curl.exe`):

```bash
curl -X POST http://localhost:8080/parsingHesData -H "Content-Type: application/json" --data-binary @demo/sample-data/standard-checkdata.json
```

Success response:

```json
{"resultCode":"10000","resultMessage":"Demo validation only; no database write"}
```

Invalid checksums, missing required envelope/person fields, empty measurements,
unsupported categories and conflicting ECG aliases produce `resultCode="00000"`.
Malformed JSON can produce HTTP 400. Category-specific clinical validation remains
the responsibility of the receiving integration. This demo does not persist data or provide LIS
mapping. Replace the processing point with durable storage and idempotent handling
before using a receiver in production; acknowledge only after successful processing.

## Checksum

`key = MD5(rsId + "konsungyitijijsondata")`, UTF-8, lower-case hex, no separator.
For `rsId=konsung`, the test result is `87a81d8edfe6b43aa8763d7147f6245a`.
The constant is public, not a per-customer secret. The demo never accepts a key merely
because it appears in a historical sample and has no permissive validation mode.

The reviewed platform has a legacy scheduled resend path signing over the exact
message `time` instead of `rsId`. Only after confirming that path in the target
deployment, enable `--ksy.accept-legacy-time-key=true`. This adds exact time-key
validation, not a bypass. Neither checksum mode prevents replay or proves body integrity.

## Samples

See [sample provenance and scope](demo/sample-data/README.md).
The standard example is synthetic. The compatibility example is a parser fixture.
The ECG fixture contains a historical published waveform, not a new Dizar recording.

Historical keys are preserved in adapted examples. To replay one, make a separate copy:

```bash
python tools/prepare_sample.py demo/sample-data/normal-checkdata.json normal-replay.json --rsid konsung
curl -X POST http://localhost:8080/parsingHesData -H "Content-Type: application/json" --data-binary @normal-replay.json
```

For the pulmonary fragment, add `--category breathing` when preparing it. Only complete
signed envelopes are accepted.

## Category and mapping corrections

- `heart` / `AktWaveForm` carries ECG. Both cases of the nine historical ECG interval/
  axis keys are accepted. Send one spelling per field; conflicting aliases are ambiguous.
- `xcg` / `AktXcgDto` is blood routine; `threeWay` is a three-part blood count, not ECG.
  Their upstream containers are not populated by the reviewed standard APP. These categories and DTOs
  are removed from this version of the demo (Q01-Q06, Q16-Q17).
- The APP uses `ubb` for KSDB biochemistry; the standard receiver path is `bioche`.
  `biochemicals` is removed from this demo and its examples (Q26-Q31); use `bioche`.
- The demo retains all eleven `hemameba` fields, but target-platform delivery of the ten
  differential fields remains to be confirmed. APP upload alone does not prove delivery.
- Optional stethoscope, pulmonary, immunofluorescence and ultrasound structures are
  parseable. Target deployment enablement/forwarding remains to be confirmed.

The contradictory old binary `Readme.docx` has been retired from the active package;
the Markdown documentation above is the maintained reference. Original interface
sections 1-11 are preserved to match the customer letter.

## Endpoint and transport

`/parsingHesData` is this demo's chosen path. The KSY destination URL is configurable
per integration. To choose a local receiving path:

```bash
java -jar target/hes_Interface_demo.jar --ksy.receiver-path=/lis/ksy/results
```

Register that complete destination URL with Konsung for the assigned rsId; changing
this property alone does not update KSY. HTTPS is selected by the registered URL;
a successful target TLS handshake still needs deployment verification. This local
demo starts with HTTP. Terminate TLS at a reverse proxy, or use Spring
Boot's `server.ssl.enabled`, `server.ssl.key-store`, `server.ssl.key-store-type` and
`server.ssl.key-store-password` configuration with a certificate trusted by the
sender. Keep certificate passwords outside source control. Confirm the TLS
handshake in the actual deployment (Q14/Q33).

## Patient and order workflow

The demo receives results only. Patient-list and demographics ingestion, test
orders/worklists and order assignment are currently unsupported (Q38-Q43).
Results are pushed to the LIS receiver configured by Konsung. The receiver must
handle any order correlation itself. See interface section 10.1.

## Customer clarification coverage

See [Q01-Q43 implementation notes](docs/Customer_Clarification_Closure.md).
Q15 remains pending deployment confirmation; Q35 and Q36 remain partially answered.
The 16 category definitions and optional report images are parser coverage, not a
claim that Dizar enables every device.
