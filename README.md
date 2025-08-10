
# Barber App Backend

Bu backend həm **customer** (müştəri), həm də **barber** (bərbər) istifadəçilər üçün nəzərdə tutulmuşdur.
Burada customer-lar qeydiyyatdan keçib barber-ə rezervasiya edə, həmçinin barber-lərə ulduz və şərh yaza bilərlər.
Barber-lər isə özləri haqqında və təqdim etdikləri xidmətlər haqqında məlumat verə bilərlər.


---


## 📌 Entity-lər və Tipləri

### 1. **User**

**Cədvəl:** `user`

| Sahə                        | Tip                   | İzah                                                  |
| --------------------------- | --------------------- | ----------------------------------------------------- |
| `id`                        | `Long`                | Unikal identifikator                                  |
| `username`                  | `String`              | İstifadəçi adı                                        |
| `email`                     | `String`              | Email ünvanı                                          |
| `password`                  | `String`              | Şifrələnmiş parol                                     |
| `role`                      | `RoleType` *(enum)*   | İstifadəçi rolu (**ADMIN**, **CUSTOMER**, **BARBER**) |
| `gender`                    | `GenderType` *(enum)* | Cinsi (**MALE**, **WOMAN**)                           |
| `phoneNumber`               | `String`              | Telefon nömrəsi                                       |
| `profilePicture`            | `String`              | Profil şəkli URL-i                                    |
| `createdAt`                 | `LocalDateTime`       | Yaradılma tarixi                                      |
| `updatedAt`                 | `LocalDateTime`       | Yenilənmə tarixi                                      |
| `verificationCodeExpiresAt` | `LocalDateTime`       | Doğrulama kodunun bitmə tarixi                        |
| `verificationCode`          | `String`              | Email doğrulama kodu                                  |
| `enabled`                   | `boolean`             | Hesab aktiv olub-olmaması                             |

Əlaqələr:

* `List<Appointment>` — **OneToMany** (customer rezervasiyaları)
* `List<RefreshToken>` — **OneToMany** (refresh tokenlər)

---

### 2. **Barber**

**Cədvəl:** `barber`

| Sahə           | Tip                   | İzah                          |
| -------------- | --------------------- | ----------------------------- |
| `id`           | `Long`                | Unikal identifikator          |
| `user`         | `User`                | Əlaqəli istifadəçi (OneToOne) |
| `name`         | `String`              | Bərbərin adı                  |
| `photoUrl`     | `String`              | Şəkil URL-i                   |
| `location`     | `String`              | Ünvan                         |
| `rating`       | `Double`              | Ümumi reytinq                 |
| `targetGender` | `GenderType` *(enum)* | Xidmət etdiyi cins            |
| `createdAt`    | `LocalDateTime`       | Yaradılma tarixi              |
| `updatedAt`    | `LocalDateTime`       | Yenilənmə tarixi              |

Əlaqələr:

* `List<Service>` — **OneToMany** (təklif olunan xidmətlər)
* `List<Appointment>` — **OneToMany** (qəbul edilən rezervasiyalar)
* `List<Review>` — **OneToMany** (müştəri rəyləri)
* `List<Schedule>` — **OneToMany** (iş qrafiki)

---

### 3. **Appointment**

**Cədvəl:** `appointment`

| Sahə              | Tip                          | İzah                        |
| ----------------- | ---------------------------- | --------------------------- |
| `id`              | `Long`                       | Unikal identifikator        |
| `customer`        | `User`                       | Rezervasiya edən istifadəçi |
| `barber`          | `Barber`                     | Rezervasiya olunan bərbər   |
| `services`        | `List<Service>`              | Seçilmiş xidmətlər          |
| `appointmentDate` | `LocalDate`                  | Tarix                       |
| `appointmentTime` | `LocalTime`                  | Saat                        |
| `status`          | `ReservationStatus` *(enum)* | Rezervasiya statusu         |
| `createdAt`       | `LocalDateTime`              | Yaradılma tarixi            |
| `updatedAt`       | `LocalDateTime`              | Yenilənmə tarixi            |

---

### 4. **Service**

**Cədvəl:** `service`

| Sahə              | Tip                    | İzah                       |
| ----------------- | ---------------------- | -------------------------- |
| `id`              | `Long`                 | Unikal identifikator       |
| `barber`          | `Barber`               | Xidmət sahibi bərbər       |
| `serviceType`     | `ServiceType` *(enum)* | Xidmət növü                |
| `description`     | `String`               | Təsviri                    |
| `durationMinutes` | `Integer`              | Davametmə müddəti (dəqiqə) |
| `price`           | `Double`               | Qiymət                     |
| `createdAt`       | `LocalDateTime`        | Yaradılma tarixi           |
| `updatedAt`       | `LocalDateTime`        | Yenilənmə tarixi           |

---

### 5. **Review**

**Cədvəl:** `reviews`

| Sahə        | Tip             | İzah                 |
| ----------- | --------------- | -------------------- |
| `id`        | `Long`          | Unikal identifikator |
| `customer`  | `User`          | Rəyi yazan müştəri   |
| `barber`    | `Barber`        | Rəy yazılan bərbər   |
| `rating`    | `Integer`       | Ulduz sayı           |
| `comment`   | `String`        | Rəy mətni            |
| `createdAt` | `LocalDateTime` | Tarix                |

---

### 6. **Schedule**

**Cədvəl:** `schedule`

| Sahə        | Tip             | İzah                 |
| ----------- | --------------- | -------------------- |
| `id`        | `Long`          | Unikal identifikator |
| `barber`    | `Barber`        | Əlaqəli bərbər       |
| `dayOfWeek` | `DayOfWeek`     | Həftənin günü        |
| `startTime` | `LocalTime`     | İşin başlama vaxtı   |
| `endTime`   | `LocalTime`     | İşin bitmə vaxtı     |
| `createdAt` | `LocalDateTime` | Yaradılma tarixi     |
| `updatedAt` | `LocalDateTime` | Yenilənmə tarixi     |

---

### 7. **RefreshToken**

**Cədvəl:** `refresh_token`

| Sahə        | Tip       | İzah                    |
| ----------- | --------- | ----------------------- |
| `id`        | `Long`    | Unikal identifikator    |
| `token`     | `String`  | Refresh token string    |
| `createdAt` | `Date`    | Yaradılma tarixi        |
| `expiredAt` | `Date`    | Bitmə tarixi            |
| `revoked`   | `boolean` | Ləğv edilib/edilməyib   |
| `user`      | `User`    | Token sahibi istifadəçi |

---

## 📌 Enum-lar və Dəyərlər

### **GenderType**

```java
MALE,
WOMAN
```

### **ReservationStatus**

```java
PENDING,    // Gözləmədə
CONFIRMED,  // Təsdiqlənib
CANCELED    // Ləğv edilib
```

### **RoleType**

```java
ADMIN,
CUSTOMER,
BARBER
```

### **ServiceType**

```java
FADE,
COLORING,
BEARD_TRIM,
KERATIN,
HAIRCUT,
SHAVE,
WASH_AND_STYLE,
SCALP_TREATMENT,
MASK,
BLACK_MASK,
HOT_TOWEL,
EYEBROW_TRIM,
HAIR_TATTOO,
CHILD_HAIRCUT,
HEAD_MASSAGE,
SHAMPOO_ONLY,
BLOW_DRY,
HAIR_STRAIGHTENING
```

---




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

Aşağıda verdiyin məlumatları nəzərə alaraq **Appointment API** üçün səliqəli `README.md` formatını hazırladım:

---

# Appointment API Documentation


## **1. Appointment Yaratmaq**

Hər iki rol üçün mövcuddur:

* **Customer:** `{base_url}/customer/create-appointment`
* **Barber:** `{base_url}/barber/create-appointment`

**Method:** `POST`

### **Request Body**

```json
{
  "barberId": 1,
  "serviceIds": [67890, 54321],
  "appointmentDate": "2025-08-15",
  "appointmentTime": "14:30:00"
}
```

### **Response**

```json
{
  "id": 56789,
  "barberName": "John Smith",
  "customerName": "Jane Doe",
  "services": [
    {
      "serviceId": 101,
      "serviceName": "KERATIN",
      "price": 30.00
    },
    {
      "serviceId": 102,
      "serviceName": "HAIRCUT",
      "price": 15.00
    }
  ],
  "appointmentDate": "2025-08-15",
  "appointmentTime": "10:00:00",
  "status": "CONFIRMED"
}
```

---

## **2. Appointmentləri Oxumaq**

**Endpoint:** `{base_url}/customer/read-appointments`
**Method:** `GET`
**Request:** Tələb olunmur. Login olmuş istifadəçiyə aid bütün görüşlər (ən son ediləndən ən əvvələ doğru sıralanmış) qaytarılır.

### **Response**

```json
[
  {
    "id": 56789,
    "barberName": "John Smith",
    "customerName": "Jane Doe",
    "services": [
      {
        "serviceId": 101,
        "serviceName": "Haircut",
        "price": 30.00
      },
      {
        "serviceId": 102,
        "serviceName": "Beard Trim",
        "price": 15.00
      }
    ],
    "appointmentDate": "2025-08-15",
    "appointmentTime": "10:00:00",
    "status": "CONFIRMED"
  },
  {
    "id": 56790,
    "barberName": "John Smith",
    "customerName": "Peter Jones",
    "services": [
      {
        "serviceId": 103,
        "serviceName": "Shave",
        "price": 20.00
      }
    ],
    "appointmentDate": "2025-08-15",
    "appointmentTime": "11:30:00",
    "status": "PENDING"
  },
  {
    "id": 56791,
    "barberName": "Emily White",
    "customerName": "Sarah Brown",
    "services": [
      {
        "serviceId": 101,
        "serviceName": "Haircut",
        "price": 30.00
      },
      {
        "serviceId": 104,
        "serviceName": "Coloring",
        "price": 80.00
      }
    ],
    "appointmentDate": "2025-08-16",
    "appointmentTime": "09:00:00",
    "status": "COMPLETED"
  }
]
```

---

## **3. Appointment Yeniləmək**

**Endpoint:** `{base_url}/customer/update-appointment/{id}`
**Method:** `PUT`

### **Request Body**

```json
{
  "appointmentDate": "2025-08-15",
  "appointmentTime": "14:00:00",
  "serviceIds": [101, 102]
}
```

### **Response**

`createAppointment` endpointində olduğu formatda qaytarılır.

---

## **4. Appointment Silmək**

**Endpoint:** `{base_url}/customer/update-appointment/{id}`
**Method:** `DELETE`

### **Request:**

* Sadəcə `id` URL-də göndərilir.
* Response olaraq heç nə qaytarılmır.

---

Aşağıdakı kimi sənə uyğun **README.md** mətni hazırladım — sən bunu birbaşa layihənin kök qovluğuna qoysan, Barber API-ləri üçün izahlı sənəd olacaq.

---

# 💈 Barber (Entity) API Documentation

Bu sənəd, Barber rolundakı istifadəçilərin öz profilini idarə etməsi və müştərilərin barberlər haqqında məlumat axtarması üçün nəzərdə tutulmuş API-lərin təsvirini təqdim edir.

---

## 🔍 Public Endpoints

### 1. **Bütün Barberləri Oxumaq (Search Result)**

**Endpoint:**

```
GET {base_url}/public/get-barbers
```

**Açıqlama:**
Axtarış ekranında bütün barberləri göstərmək üçün istifadə olunur.

**Response nümunəsi:**

```json
[
  {
    "id": 1,
    "name": "John Doe",
    "photoUrl": "https://example.com/photos/johndoe.jpg",
    "serviceTypes": [
      {
        "id": 101,
        "name": "HAIRCUT",
        "description": "Classic men's haircut"
      },
      {
        "id": 102,
        "name": "BEARD_TRIM",
        "description": "Professional beard trim and shaping"
      }
    ],
    "rating": 4.8,
    "targetGender": "MALE",
    "location": "123 Main Street, Anytown"
  }
]
```

---

### 2. **Barber Detalları (ID üzrə)**

**Endpoint:**

```
GET {base_url}/public/get-barber/{id}
```

**Açıqlama:**
Seçilmiş barber haqqında tam məlumat qaytarır.

**Response nümunəsi:**

```json
{
  "id": 123,
  "name": "Alex Johnson",
  "photoUrl": "https://example.com/photos/alexjohnson.jpg",
  "location": "789 Pine Street, Anytown",
  "rating": 4.7,
  "targetGender": "MALE",
  "services": [
    {
      "serviceId": 101,
      "serviceName": "Haircut",
      "price": 30.00
    }
  ],
  "reviews": [
    {
      "reviewId": 501,
      "reviewerName": "Mark Davis",
      "rating": 5,
      "comment": "Great haircut and friendly service!"
    }
  ],
  "schedules": [
    {
      "scheduleId": 801,
      "dayOfWeek": "MONDAY",
      "startTime": "09:00:00",
      "endTime": "18:00:00"
    }
  ]
}
```

---

## 💼 Barber Endpoints

### 1. **Barber Profilini Görüntüləmək**

**Endpoint:**

```
GET {base_url}/barber/profile
```

**Açıqlama:**
Giriş etmiş barberin profil məlumatlarını qaytarır.

---

### 2. **Barber Profilini Yeniləmək**

**Endpoint:**

```
PATCH {base_url}/barber/update-barberProfile/
```

**Açıqlama:**
Barberin profilini yeniləmək üçün istifadə olunur. Request body **entity**-yə əsaslanır və dinamikdir.

**Mümkün dəyişikliklər:**

* **location** — İş ünvanı
* **services** — Təklif olunan xidmətlər və qiymətləri
* **schedules** — İş günləri və saatları

---

## 📱 Frontend İstifadə Qaydası

Barber rolunda giriş edən istifadəçi:

1. **Ana ekranda** profilini yaratmaq və ya yeniləmək imkanı görəcək.
2. **Location** seçəcək.
3. **Xidmətlər** və qiymətləri əlavə edəcək.
4. **İş saatlarını** müəyyən edəcək.

Müştəri tərəfdə isə:

* **Axtarış ekranında** `get-barbers` endpoint-dən gələn siyahı göstərilir.
* Seçilmiş barberin detalları `get-barber/{id}` endpoint-i ilə gətirilir.

---
Aşağıdakı kimi sadə və aydın şəkildə **Review** API üçün README hissəsi hazırladım:

---

## ⭐ Review API

### 1. **Review vermək (yalnız müştərilər üçün)**

Barber rolundakı istifadəçilər review verə bilməzlər. Bu, barberlər arasında əsassız və mənasız rəylərin qarşısını almaq üçündür.

**Endpoint:**

```
POST {base_url}/customer/give-review/{id}
```

* `{id}` → Review veriləcək barberin unikal identifikatoru

**Request Body:**

```json
{
  "rating": 5,
  "comment": "Great service and a fantastic haircut!"
}
```

* `rating` — Ulduz sayı (mütləq olmalıdır, 1-5 arası)
* `comment` — Şərh (istəyə bağlı, amma yazılarsa, `rating` mütləq olmalıdır)

---

### 2. **Review-ların göstərilməsi**

Barberin bütün review-ları `GET {base_url}/public/get-barber/{id}` endpointindən alınır və frontenddə barberin profili və ya detalları göstərilərkən görünür.

---

**Qeyd:**

* Müştəri yalnız ulduz verə bilər, amma şərh yazarsa, ulduz vermək mütləqdir.
* Barberlər review verə bilməz.

---


Aşağıdakı kimi **Schedule API** üçün README hissəsini hazırladım:

---

## 📅 Schedule (İş Qrafiki) API - Barber üçün

Barberlər öz iş saatlarını idarə etmək üçün bu endpointlərdən istifadə edirlər.

---

### 1. **Yeni iş qrafiki yaratmaq**

**Endpoint:**

```
POST {base_url}/barber/create-schedule
```

**Request Body:**

```json
{
  "dayOfWeek": "MONDAY",    // "TUESDAY", "WEDNESDAY" və s.
  "startTime": "09:00:00",
  "endTime": "17:00:00"
}
```

**Response:**

```json
{
  "dayOfWeek": "MONDAY",
  "startTime": "09:00:00",
  "endTime": "17:00:00"
}
```

---

### 2. **Mövcud iş qrafikini yeniləmək**

**Endpoint:**

```
PATCH {base_url}/barber/update-schedule/{id}
```

* `{id}` — Yenilənəcək iş qrafikinin ID-si

**Request Body:**

* Entity strukturuna uyğun, dinamik olaraq dəyişə bilər (məsələn, `dayOfWeek`, `startTime`, `endTime`)

**Response:**

```json
{
  "dayOfWeek": "MONDAY",
  "startTime": "09:00:00",
  "endTime": "17:00:00"
}
```

---

### 3. **İş qrafikini silmək**

**Endpoint:**

```
DELETE {base_url}/barber/delete-schedule/{id}
```

* `{id}` — Silinəcək iş qrafikinin ID-si

**Response:**

* Boş (void), heç bir data qaytarılmır

---

Bu endpointlər barberlərə öz iş günlərini və saatlarını rahat idarə etməyə imkan verir.

---

Aşağıda **Service API** üçün README.md hissəsini hazırladım:

---

## 💈 Service API - Barberin Təklif Etdiyi Xidmətlər

---

### 1. **Yeni xidmət yaratmaq**

**Endpoint:**

```
POST {base_url}/barber/create-service
```

**Request Body:**

```json
{
  "serviceType": "HAIRCUT",
  "description": "A classic men's haircut with a modern touch.",
  "durationMinutes": 45,
  "price": 35.00
}
```

**Response:**

```json
{
  "id": 101,
  "serviceType": "HAIRCUT",
  "description": "A stylish haircut tailored to your preferences.",
  "durationMinutes": 45,
  "price": 30.00
}
```

---

### 2. **Mövcud xidməti yeniləmək**

**Endpoint:**

```
PATCH {base_url}/barber/update-service/{id}
```

* `{id}` — Yenilənəcək xidmətin ID-si

**Request Body:**

* Entity strukturuna uyğun dəyişikliklər (məsələn, `serviceType`, `description`, `durationMinutes`, `price`)

**Response:**

```json
{
  "id": 101,
  "serviceType": "HAIRCUT",
  "description": "A stylish haircut tailored to your preferences.",
  "durationMinutes": 45,
  "price": 30.00
}
```

---

### 3. **Xidməti silmək**

**Endpoint:**

```
DELETE {base_url}/barber/delete-service/{id}
```

* `{id}` — Silinəcək xidmətin ID-si

**Response:**

* Boş (void), heç bir data qaytarılmır

---

### 4. **Xidmətin təfərrüatlarını oxumaq**

**Endpoint:**

```
GET {base_url}/barber/read-service/{id}
```

* `{id}` — Xidmətin ID-si

**Qeyd:**

* Bu endpoint frontend-də xidmətin detallarını göstərmək üçün istifadə olunur.
* Gələcəkdə silinə bilər, çünki məntiq olaraq barber profilində göstərilən xidmətlərlə birlikdə gəlməsi daha faydalıdır.

---

### 5. **Barberin bütün xidmətlərini oxumaq (gələcəkdə silinə bilər)**

**Endpoint:**

```
GET {base_url}/barber/read-services/
```

* Bu endpoint barberin bütün xidmətlərini oxumaq üçün nəzərdə tutulub, amma məntiqi səbəbdən frontend-də istifadə etmək tövsiyə edilmir.
* Xidmətlər, review-lar və cədvəllər (`schedules`) bir yerdə profil ekranına yüklənməsi daha uyğundur.

---

Bu API-lər barberlərə xidmətlərini idarə etməkdə tam rahatlıq verir.

---
---

## ⚠️ Problemlər və Gələcək Təkmilləşdirmələr

### 1. **Review-ların idarəsi**

* İstifadəçi artıq ulduz və ya review vermişsə, onun təkrar review verməsinin qarşısı alınmalıdır.
* İstifadəçilərə mövcud review-larını redaktə etmək imkanı əlavə edilməlidir ki, eyni şəxs yüzlərlə review göndərə bilməsin.

### 2. **Profilin silinməsi**

* İstifadəçilər və barberlər üçün profil silmək funksiyası əlavə edilməlidir.
* Bu, hesabların idarəsi və təhlükəsizliyi baxımından vacibdir.

### 3. **Barber axtarışı (search) funksiyası**

* Barberləri ada görə axtarmaq funksiyası əlavə olunmalıdır.
* Bu, istifadəçilərin sevdikləri barberləri daha asan tapması üçün önəmlidir.

### 4. **Appointment vaxtının idarəsi**

* Hazırda rezervasiya zamanı yalnız başlanğıc vaxtı təyin olunur.
* Rezervasiyanın bitmə vaxtı (end time) hesablanmır və göstərilmir.
* Gələcəkdə end time servis müddətinə əsaslanaraq backend tərəfindən avtomatik hesablanmalıdır.
* Servislərin hər birinin müddətinə əlavə olaraq 10 dəqiqə tampon vaxt əlavə edilə bilər.



---








