## 🐞 Bug Report

### 📋 Summary
**[#2]** Duplicate `screenName` allowed in `GET /player/create/{editor}` request, violating uniqueness constraint.

---

### Attributes

- **Reporter:** Dastan Shokimov
- **Assigned To:** Dev
- **Priority:** High
- **Severity:** Major
- **Reproducibility:** Always
- **Status:** New
- **Resolution:** Open
- **Platform:** Test Server
- **OS:** Linux
- **OS Version:** Unknown

---

### 🧪 Description
According to the application requirements, the `screenName` field must be unique for each player. However, the API allows creation of multiple users with the same `screenName`.

---

### 🔁 Steps to Reproduce

1. Open Postman or Rest Client
2. Set the request method to **GET**
3. Use the following URL (1st request):  http://3.68.165.45/player/create/supervisor?age=25&gender=MALE&login=jackiechan1&role=USER&screenName=Jackie%20Chan&password=test123
4. Use the following URL (2nd request with different login but same screenName):  http://3.68.165.45/player/create/supervisor?age=25&gender=MALE&login=jackiechan2&role=USER&screenName=Jackie%20Chan&password=test456
5. Send both requests and observe the responses

---

### ✅ Expected Result
- Second request should fail with `400 Bad Request` or `409 Conflict`
- API must reject creation of users with duplicate `screenName`

### ❌ Actual Result
Both users were created successfully with the same `screenName`.

#### First Response:
```json
{
"id": 96537149,
"login": "jackiechan1",
"password": null,
"screenName": null,
"gender": null,
"age": null,
"role": null
}
```
#### Second Response:
```json
{
  "id": 206366012,
  "login": "jackiechan2",
"password": null,
"screenName": null,
"gender": null,
"age": null,
"role": null
}
```
