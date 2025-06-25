## VLC End points for the api

### Axios is best for handling api status codes

Use the steps to install axios

```bash
    npm install --save axios
```

Import and use axios

```javascript
//add this dependency for api fetch
import axios from "axios";

await axios.fetch();
```

## About the Endpoints nad Data To Pass

---

- ### _To register a new user_

```json
{
  "phoneNumber": "+23312345678", //should match a format (+233) and number without zero (123456789)
  "password": "qwerty" // should be 8 charcters long
}
```

send a **POST** to the end point

```http
    http://_____/register
```

- ### _To reset a user's password_

```json
{
  "phoneNumber": "+23312345678", //should match a format (+233) and number without zero (123456789)
  "password": "new password" // should be 8 charcters long
}
```

send a **POST** to the end point

```http
    http://_____/reset-password
```

- ### _Logining in_

```json
{
  "phoneNumber": "+23312345678", //should match a format (+233) and number without zero (123456789)
  "password": "new password" // should be 8 charcters long
}

//returns a set of json data in the form
//status code of 200
{
    "status":true,
    "message":"User authenticated successfully",
    "data":{
        "jwt":"a set of strings to be stored as jwt"
    }
}

// returns an error incase the user password was wrong
//status code (401,500)
{
    "status":false,
    "message":"the error message",
    "data":null
}

```

send a **POST** to the end point

```http
    http://_____/login
```

- ### To log a user out

```http
    http://_____/logout
```
