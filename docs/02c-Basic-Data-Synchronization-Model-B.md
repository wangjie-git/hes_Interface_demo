> Original interface retained for reference; this correction does not certify a new
> deployment of this endpoint. Obtain the target base URL and enabled model from the
> deployment owner. This is not an order/worklist API. See the measurement document,
> section 10.1, for the patient/order capability clarification.

## 2.3 Konsung provides the calling interface to third-party

### 2.3.1 Interface solution

1. ** `HES management system provides one calling interface to the third party`**
2. Transfer the basic data into the HES management system to synchronize the data to the local database

### 2.3.2 Data transmission mode

 `http+json+post`

### 2.3.3 Interface address example

http://localhost:8080/ehr-app/queryOrgs

### 2.3.4 Description of interface calling parameters

| Data Item | Field Name | Type | Required | Remarks |
| ------------ | ------- | ------ | ---- | ---------------------------------------------------------- |
| Check code | vercode | String | Yes | The only authentication string agreed by both parties; MD5(rsCode+konsungyitijijsondata) |
| Area Code | rsCode | String | Yes | Specified by the all-in-one machine management system |
| Organization information collection | orgs | collection | Yes | List<ThirdOrgDto>is not empty |

**ThirdOrgDto object properties**

| Data Item | Field Name | Type | Required | Length | Remarks |
| ------------ | ------------ | ------ | ---- | ---- | ---------------------------- |
| Organization Code | orgCode | String | Yes | 64 | Ensure unique in the original system |
| Organization name | orgName | String | Yes | 80 | |
| Third-party code | rsId | String | Yes | 32 | Assigned by all-in-one machine management system |
| Parent organization code | parentOrgId | String | Yes | 64 | Top level organization is blank, other level organizations must fill in |
| Organization Level | orgClass | String | Yes | 1 | |
| Institution address | orgAddr | String | No | 100 | |
| Available | available | String | Yes | 1 | Y - available, N - unavailable |
| Organization Contact | orgContacts | String | No | 40 | |
| Organization phone | orgTelephone | String | No | 40 | |
| Organization Description | orgDesc | String | No | 256 | |
| Employee collection | doctors | collection | No | 80 | List<DoctorDto> |

**DoctorDto object properties**

| Data Item | Field Name | Type | Required | Length | Remarks |
| ------------ | --------- | ------ | ---- | ---- | ---------------------------------------- |
| Account (employee number) | username | String | Yes | 64 | and ensure that it is unique in the original system |
| Display name | nickName | String | Yes | 30 | |
| Organization Code - | orgCode | String | Yes | 64 | |
| ID number | idNo | string | No | 18 | |
| Gender id | sexId | String | No | 1 | 0-Unknown gender, 1-male, 2-female, 9-Unspecified gender |
| Date of birth | birthdate | String | No | 10 | Format: 1990-12-01 |
| Available | available | String | Yes | 1 | Y is available, N is unavailable. |
| Mobile phone | telphone | String | No | 11 | |

### 2.3.5 Interface call return value table

| Data Item | Field Name | Type | Byte | Required | Remarks |
| ---------------- | ------------- | ------ | ---- | ---- | ---------------------------- |
| Call interface status code | resultCode | string | 5 | Yes | 10000 - call succeeded 00000 - call failed |
| Call interface prompt | resultMessage | string | | | The prompt returned after calling the interface |

## 2.4 Basic data download interface

### 2.4.1 Interface scheme

The all-in-one machine management system provides an interface, which is called by a third party company to transfer the basic data to the third party

### 2.4.2 Data transmission mode

 http+json+post

### 2.4.3 Interface address example

http://localhost:8080/imms-web/thirdData/queryBaseData

### 2.4.4 Description of interface call parameters

| Data Item | Field Name | Type | Required | Remarks |
| ---------- | ----------- | ------ | ---- | -------------------------------------------------------- |
| Check code | vercode | string | Yes | The only authentication string agreed by both parties; MD5(rsId+konsungyitijijsondata) |
| Region Code | rsId | String | Yes | Specified by the all-in-one machine management system |
| Organization Code | orgCode | Set | No | Organization Code |
| Organization name | orgName | string | No | Organization name |
| Parent organization ID | parentOrgId | String | No | Parent organization ID |

### 2.4.5 Interface call return value table

| Data Item | Field Name | Type | Byte | Required | Remarks |
| ---------------- | ------------- | ------ | ---- | ---- | ----------------------------- |
| Call interface status code | resultCode | string | 5 | Yes | 10000 - call succeeded 00000 - call failed |
| Call interface prompt | resultMessage | string | | Yes | Prompt returned after calling interface |
| Organization information collection | orgs | collection | | No | List<ThirdOrgDto>is not empty |

**ThirdOrgDto object properties**

| Data Item | Field Name | Type | Required | Length | Remarks |
| ------------ | ------------ | ------ | ---- | ---- | ---------------------------- |
| Organization Code | orgCode | String | Yes | 64 | Ensure unique in the original system |
| Organization name | orgName | string | Yes | 80 | |
| Third-party code | rsId | string | Yes | 32 | Assigned by all-in-one machine management system |
| Parent organization code | parentOrgId | string | Yes | 64 | Top level organization is blank, other level organizations must fill in |
| Organization Level | orgClass | String | Yes | 1 | |
| Institution address | orgAddr | String | No | 100 | |
| Available | available | String | Yes | 1 | Y - available, N - unavailable |
| Organization Contact | orgContacts | String | No | 40 | |
| Organization phone | orgTelephone | String | No | 40 | |
