1. Create a new user - POST(`/api/auth`)

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "username@mail.cz",
  "password": "123456a",
  "birthDate": "2023-11-20T04:49:14.674Z"
}

2. Login, get new tokens and put in "Authorize" (any token) - POST(`/api/auth/login`)

{
  "email": "username@mail.cz",
  "password": "123456a"
}

Access token - 1 d.
Refresh - 10 d.
Refresh token - eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzAwNTExMDIzLCJleHAiOjE3MDkxNTEwMjN9.jNd3icNlSNpiOTtkKoix0mv6WcilPnfoZwJG9RQp8Ns

3. Get user - GET(`/api/user`)


4. Create 2 new accounts - POST(`/api/accounts`)

{
  "name": "Account for Czechia and CZK payments",
  "currency": "CZK",
  "iban": "CZ123456"
}

{
  "name": "Account for EU",
  "currency": "CZK",
  "iban": "EU123456"
}

6. Get list of all accounts - GET(`/api/accounts`)

id = 1

7. Create 3 new transactions - POST(`/api/accounts/{id}/transactions`)

id = 1

{
  "amount": 1500.27,
  "debtor": "EU123456"
}

{
  "amount": 1000,
  "debtor": "Another bank 1"
}

{
  "amount": 1200,
  "debtor": "Another bank 2"
}

{
  "amount": -1200,
  "debtor": "Another bank 3"
}
(Error case)

8. Get all transactions of accounts 1 and 2 - GET(`/api/accounts/{id}/transactions`)

id = 1

9. Get account by id GET (`/api/accounts/{id}`)

id = 1 (- balance)
id = 2 (+ balance)

10. Create 2 new cards for account '1' - POST(`/api/accounts/{id}/cards`)

id = 1

{
  "name": "Travel card"
}

{
  "name": "Working card"
}

11. Get all cards of account '1' - GET(`/api/accounts/{id}/cards`)

id = 1

12. Unlock (activate) card '2' for account '1' - PUT(`/api/accounts/{accountId}/cards/{id}`)

accountId = 1
id = 2

{
  "blocked": false
}

13. Get all cards of account '1' - GET(`/api/accounts/{id}/cards`)

id = 1

14. Get card '1' - GET(`/api/accounts/{accountId}/cards/{id}`)

accountId = 1
id = 2

15. Block card '2' for account '1' - PUT(`/api/accounts/{accountId}/cards/{id}`)

accountId = 1
id = 2

{
  "blocked": true
}

16. Get card '1' - GET(`/api/accounts/{accountId}/cards/{id}`)

accountId = 1
id = 2

Finish