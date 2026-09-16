> Original interface retained for reference; this correction does not certify a new
> deployment of this endpoint. Obtain the target base URL and enabled model from the
> deployment owner. This is not an order/worklist API. See the measurement document,
> section 10.1, for the patient/order capability clarification.

## 2.2 Third-party provides basic data query interface

### 2.2.1 Interface solution

1. **`The interface is provided by the third-party platform`**
2. **The HES management system calls and saves the queried data to the local database**

### 2.2.2 Data transmission mode

 `http+json+post`

### 2.2.3 Interface address example

	http://localhost:8080/ehr-app/queryOrgs

### 2.2.4 Description of interface calling parameters

| Data Item | Field Name | Type | Required | Remarks |
| ------ | ------- | ------ | ---- | ------------------------ |
| Check code | vercode | String | Yes | The unique authentication string agreed by both parties. |

### 2.2.5 Calling interface returned value table

| Data Item | Field Name | Type | Byte | Required | Remarks |
| ---------------- | ------------- | ------ | ---- | ---- | ------------------------------------ |
| Calling status | resultCode | String | 5 | Yes | ` 10000  `: Succeed<br/>` 00000 `: Failed |
| Prompt | resultMessage | String | | | The prompt returned after calling the interface |
| Organization object group | orgList | collection | | | Organization object group, it is empty if no organization or failure. |

**Organization (ThirdOrgDto)**

| Data Item | Field Name | Type | Byte | Required | Remarks |
| ------------ | ------------ | ------ | ---- | ---- | ----------------------------------------------- |
| Organization code | orgCode | String | 64 | Yes | It must be unique in the original system. |
| Organization name | orgName | String | 80 | Yes | |
| Third-party code | rsId | String | 32 | Yes | Assigned by HES management system |
| Parent organization code | parentOrgId | String | 64 | | Top level organization is empty, and other level organizations are required, otherwise it will cause data confusion. |
| Organization level code | orgClass | String | 10 | Yes | |
| Organization address | orgAddr | String | 100 | | |
| Available | available | String | 1 | Yes | Y is available, N is unavailable. |
| Organization Contact | orgContacts | String | 40 | | |
| Organization phone | orgTelephone | String | 40 | | |
| Organization Description | orgDesc | String | 256 | | |
| Doctor group | doctors | | | | The group of doctor objects in the organization. It can be empty. |

**Doctor (DoctorDto)**

| Data Item | Field Name | Type | Byte | Required | Remarks |
| ------------ | --------- | ------ | ---- | ---- | ---------------------------------------- |
| Account | username | string | 64 | Yes | It must be unique in the original system. |
| Displayed name | nickName | String | 30 | Yes | |
| Organization Code | orgCode | String | 64 | Yes | |
| Available | available | String | 1 | Yes | Y is available, N is unavailable. |
| ID card | idNo | string | 18 | | |
| Date of birth | birthdate | String | 10 | | Format: 1990-01-01 |
| Gender id | sexId | String | 1 | | 0 is Unknown; 1 is Male; 2 is Female; 9 is Unspecified |
| Mobile phone | telphone | String | 11 | | |

Json example:

```json
{
  "resultCode": "10000",
  "resultMessage": "",
  "orgList": [
    {
      "orgCode": "DEMO-ORG",
      "orgName": "Example organization",
      "rsId": "konsung",
      "parentOrgId": "",
      "orgClass": "2",
      "orgAddr": "Example address",
      "available": "Y",
      "orgContacts": "Example contact",
      "orgTelephone": "",
      "orgDesc": "Synthetic reference example",
      "doctors": [
        {
          "username": "DEMO-DOCTOR",
          "nickName": "Example doctor",
          "orgCode": "DEMO-ORG",
          "available": "Y",
          "idNo": "",
          "birthdate": "1985-05-05",
          "sexId": "0",
          "telphone": ""
        }
      ]
    }
  ]
}
```
