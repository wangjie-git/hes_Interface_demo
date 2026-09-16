> Original interface retained for reference; this correction does not certify a new
> deployment of this endpoint. Obtain the target base URL and enabled model from the
> deployment owner. This is not an order/worklist API. See the measurement document,
> section 10.1, for the patient/order capability clarification.

### 3.1.1 Interface solution

1. ** `HES management system provides interface`**
2. Calling by the third party company to transfer the basic data to the third party

### 3.1.2 Data transmission mode

 `http+json+post`

### 3.1.3 Interface address example

http://localhost:8080/imms-web/thirdData/queryPersonIndex

### 3.1.4 Description of calling interface parameters

| Data Item | Field Name | Type | Required | Remarks |
| -------- | ------- | ------- | ---- | -------------------------------------------------------- |
| Check code | vercode | String | Yes | The unique authentication string agreed by both parties; MD5(rsId+konsungyitijijsondata) |
| Region Code | rsId | String | Yes | Specified by the HES management system |
| Organization Code | orgCode | String | Yes | Organization Code |
| Staff Code | empCode | String | No | Staff Code |
| Name | name | String | No | Name |
| ID Card | idNo | String | No | ID card number |

### 3.1.5 Calling interface returned value table

| Data Item | Field Name | Type | Byte | Required | Remarks |
| ---------------- | ------------- | ------ | ---- | ---- | ---------------------------- |
| Calling status | resultCode | String | 5 | Yes | ` 10000  `: Succeed<br/>` 00000 `: Failed |
| Prompt | resultMessage | String | | Yes | Prompt returned after calling interface |
| Physical examination information set | persons | set | Yes | List<AktPersonInfo> | | |

**AktPersonInfo Object Properties**

| Data Item | Field Name | Type | Byte | Required | Remarks |
| -------------- | ------------ | ------ | ---- | ---- | ------------------------------ |
| ID card | idNumber | String | 18 | No | It depends on the application scenarios and requirements of countries or regions. |
| Other certificate | otherNumber | String | 32 | No | It depends on the application scenarios and requirements of countries or regions. |
| Name | name | String | 20 | Yes | |
| Gender | sexCode | String | 1 | Yes | 0 is Unknown; 1 is Male; 2 is Female; 9 is Unspecified |
| Date of birth | birthdayDate | String | | No | Format: 2015-12-11 |
| Health file number | healthNumber | String | 32 | No | It depends on the business scenario of HES. |
| Height | height | String | 10 | No | Unit: cm |
| Weight | weight | String | 10 | No | Unit: kg |
| Body Mass Index | bmi | String | 10 | No | |
| Waist | waist | String | 10 | No | Unit: cm |
| Hipline | hipline | String | 10 | No | Unit: cm |

**Json example (for reference only)**

```json
{
  "resultCode": "10000",
  "resultMessage": "",
  "persons": [
    {
      "idNumber": "",
      "otherNumber": "DEMO-PERSON",
      "sexCode": "1",
      "birthdayDate": "1979-12-25",
      "healthNumber": "",
      "height": "177",
      "weight": "65",
      "bmi": "20.75",
      "waist": "",
      "hipline": "",
      "name": "Test"
    }
  ]
}
```
