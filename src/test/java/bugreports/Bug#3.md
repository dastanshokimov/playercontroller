## 🐞 Bug Report

### 📋 Summary
**[#3]** `PATCH /player/update/{editor}/{id}` allows empty `login` and `screenName`, violating field constraints.

---

### Attributes

- **Reporter:** Dastan Shokimov
- **Assigned To:** Dev
- **Priority:** Critical 
- **Severity:** Major
- **Reproducibility:** Always
- **Status:** New
- **Resolution:** Open
- **Platform:** Test Server

---

### 🧪 Description
The `updatePlayer` endpoint accepts empty strings for `login` and `screenName`, and responds with HTTP 200 OK, even though these fields are required and must be unique according to the functional specification.

---

### 🔁 Steps to Reproduce

1. Open Postman or a REST client
2. Set the method to **PATCH**
3. Use the following URL:  http://3.68.165.45/player/update/supervisor/{id}.
Replace `{id}` with a valid existing user ID
4. Set the request body to:
```json
{
  "login": "",
  "screenName": ""
}
```
5. Send the request 

   ✅ Expected Result
   * HTTP Status: 400 Bad Request or another validation error 
   * Error message indicating that login and screenName cannot be empty 
   
   ❌ Actual Result 
   * HTTP Status: 200 OK 
   * Player is updated with empty login and screenName
```json
{
  "id": 16408080565,
  "login": "",
  "screenName": "",
  "gender": "MALE",
  "age": 25,
  "role": "USER"
}
```