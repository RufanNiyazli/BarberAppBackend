review de bir defe meslene ulduz verende veyayxudda review veribse bu adam editlemek falan eeve edek eyni adam yuz dfe review vermesin

# Barber App Backend

Bu backend həm **customer** (müştəri), həm də **barber** (bərbər) istifadəçilər üçün nəzərdə tutulmuşdur.
Burada customer-lar qeydiyyatdan keçib barber-ə rezervasiya edə, həmçinin barber-lərə ulduz və şərh yaza bilərlər.
Barber-lər isə özləri haqqında və təqdim etdikləri xidmətlər haqqında məlumat verə bilərlər.

## Rollar

İstifadəçilər **BARBER** və **CUSTOMER** rollarına ayrılır.
Bunun məqsədi təhlükəsizliyi artırmaqdır — **CUSTOMER** kimi daxil olmuş istifadəçi **BARBER**-ə aid API-lərə müdaxilə edə bilməz.

## API URL strukturu

3 növ URL var (security əsaslı):

* `/public/**` — authentication tələb etmir (misal: `/public/login`, `/public/register`)
* `/barber/**` — yalnız **BARBER** roluna icazə verilir
* `/customer/**` — yalnız **CUSTOMER** roluna icazə verilir

Public URL-lərə aşağıdakılar daxildir:

* `/public/login`
* `/public/register`
* `/public/refresh-accessToken`
* `/public/verify-user`
* `/public/resend-code`

---

## Auth Sistemi

### Register

**Endpoint:**

```
POST {base_url}/public/register
```

**Request body (JSON):**

```json
{
  "username": "exampleUser",
  "email": "user@example.com",
  "password": "securePassword123",
  "role": "ADMIN",
  "gender": "MALE",
  "phoneNumber": "+1234567890",
  "profilePicture": "https://example.com/images/profile.jpg"
}
```

> Frontend-də register zamanı ilk öncə istifadəçi `role` seçsin, sonra digər sahələri doldursun.
> `gender` seçiminin məqsədi istifadəçiyə uyğun (gender-based) barber-ləri göstərməkdir.

---

### Login

**Endpoint:**

```
POST {base_url}/public/login
```

**Request body (JSON):**

```json
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

> Login zamanı frontend `role` göndərməlidir deyə bir tələb yoxdur — backend email-ə əsaslanaraq istifadəçinin rolunu tapır və frontend bu role-a görə fərqli dizayn göstərə bilər.

**Register və Login response formatı (JSON):**

```json
{
  "accessToken": "string",
  "refreshToken": "string",
  "roleType": "BARBER","CUSTOMER"
}
```

---

### Email doğrulaması

**Endpoint:**

```
POST {base_url}/public/verify-user
```

**Request body (JSON):**

```json
{
  "email": "user@example.com",
  "verificationCode": "123456"
}
```

---

### Access Token yeniləmə

> Qeyd: **accessToken = 2 saat**, **refreshToken = 24 saat**

**Endpoint:**

```
POST {base_url}/public/refresh-accessToken
```

**Request body (JSON):**

```json
{
  "refreshToken": "your-refresh-token-string"
}
```

---

### Verification kodunu yenidən göndərmək

**Endpoint:**

```
POST {base_url}/public/resend-code
```

**Request body:**

```
"user@example.com"
```


