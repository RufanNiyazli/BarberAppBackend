
# master App Backend

Bu backend həm **customer** (müştəri), həm də **master** (bərbər) istifadəçilər üçün nəzərdə tutulmuşdur.
Burada customer-lar qeydiyyatdan keçib master-ə rezervasiya edə, həmçinin master-lərə ulduz və şərh yaza bilərlər.
master-lər isə özləri haqqında və təqdim etdikləri xidmətlər haqqında məlumat verə bilərlər.


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
| `role`                      | `RoleType` *(enum)*   | İstifadəçi rolu (**ADMIN**, **CUSTOMER**, **master**) |
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

# Master Entity

**Cədvəl:** `master`

Bu entity bərbərləri və onların məlumatlarını təmsil edir.

## Sahələr

| Sahə              | Tip                   | İzah                                                   |
|-------------------|-----------------------|--------------------------------------------------------|
| `id`              | `Long`                | Unikal identifikator                                   |
| `user`            | `User`                | Əlaqəli istifadəçi (OneToOne)                          |
| `name`            | `String`              | Bərbərin adı                                           |
| `profilePhotoUrl` | `String`              | Profil şəkil URL-i                                     |
| `galleryPhotos`   | `List<String>`        | Bərbərin əlavə şəkil qalereyası                        |
| `location`        | `String`              | Ünvan                                                  |
| `rating`          | `Double`              | Ümumi reytinq                                          |
| `targetGender`    | `GenderType` *(enum)* | Xidmət etdiyi cins                                     |
| `createdAt`       | `LocalDateTime`       | Yaradılma tarixi                                       |
| `updatedAt`       | `LocalDateTime`       | Yenilənmə tarixi                                       |
| `is_available`    | `Boolean`             | Mövcud olub-olmaması                                   |
| `masterType`       | `MasterType`          | |

## Əlaqələr

* `List<Service>` — **OneToMany** (təklif olunan xidmətlər)
* `List<Appointment>` — **OneToMany** (qəbul edilən rezervasiyalar)
* `List<Review>` — **OneToMany** (müştəri rəyləri)
* `List<Schedule>` — **OneToMany** (iş qrafiki)

## Qeyd

- `galleryPhotos` sahəsi `@ElementCollection` vasitəsilə əlavə şəkillərin saxlanması üçün istifadə olunur.
- `is_available` sahəsi bərbərin hazırda mövcud olub-olmamasını göstərir.
- OneToMany əlaqələr `mappedBy` atributu ilə `Master` entity-si ilə əlaqələndirilmişdir.
- 
## is_available Sahəsi Haqqında

- `is_available` sahəsi **həmişə default olaraq `false`** olaraq təyin olunub.
- Bu sahə bərbərin **bugün işləyib-işləmədiyini** göstərmək üçün istifadə olunur.
- Hər dəfə barber app-ə daxil olduqda, sistem avtomatik olaraq xəbərdarlıq göstərir.
- Bu yolla, barberin bugünkü iş statusu real vaxtda izlənilə bilir.
- Məqsəd: bərbər təcili iş üçün mövcud olduqda bunu qeyd edə bilir və müştəri də rahatlıqla görə bilir ki, bərbər hazırda işləyirmi və ya işdə deyil.

## masterType Sahəsi Haqqında
**masterType** o deməkdir ki, bunun bərbər, qadın saç ustası və yaxud da tatuaj/laser ustası olduğunu göstərmək üçün verilib.  
Bunun səbəbi isə odur ki, biz front-end-də ona servisləri göstərərkən yalnız özünə aid olanları göstərəcəyik.

---

### 3. **Appointment**

**Cədvəl:** `appointment`

| Sahə              | Tip                          | İzah                        |
| ----------------- | ---------------------------- | --------------------------- |
| `id`              | `Long`                       | Unikal identifikator        |
| `customer`        | `User`                       | Rezervasiya edən istifadəçi |
| `master`          | `master`                     | Rezervasiya olunan bərbər   |
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
| `master`          | `master`               | Xidmət sahibi bərbər       |
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
| `master`    | `master`        | Rəy yazılan bərbər   |
| `rating`    | `Integer`       | Ulduz sayı           |
| `comment`   | `String`        | Rəy mətni            |
| `createdAt` | `LocalDateTime` | Tarix                |

---

### 6. **Schedule**

**Cədvəl:** `schedule`

| Sahə        | Tip             | İzah                 |
| ----------- | --------------- | -------------------- |
| `id`        | `Long`          | Unikal identifikator |
| `master`    | `master`        | Əlaqəli bərbər       |
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
master
```
### **MasterType**

```java
BARBER,
HAIRDRESSER,
LASER,
TATTOO
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

İstifadəçilər **MASTER** və **CUSTOMER** rollarına ayrılır.
Bunun məqsədi təhlükəsizliyi artırmaqdır — **CUSTOMER** kimi daxil olmuş istifadəçi **MASTER**-ə aid API-lərə müdaxilə edə bilməz.

## API URL strukturu

3 növ URL var (security əsaslı):

* `/public/**` — authentication tələb etmir (misal: `/public/login`, `/public/register`)
* `/master/**` — yalnız **MASTER** roluna icazə verilir
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
  "password": "SecurePass123!",
  "role": "ADMIN",  // ola bilər: "ADMIN", "CUSTOMER", "MASTER"
  "gender": "MALE",
  "phoneNumber": "+1234567890"
}

```

> Frontend-də register zamanı ilk öncə istifadəçi `role` seçsin, sonra digər sahələri doldursun.
> `gender` seçiminin məqsədi istifadəçiyə uyğun (gender-based) master-ləri göstərməkdir.

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
  "roleType": "master" / veya "CUSTOMER"
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
* **master:** `{base_url}/master/create-appointment`

**Method:** `POST`

### **Request Body**

```json
{
  "masterId": 1,
  "serviceIds": [67890, 54321],
  "appointmentDate": "2025-08-15",
  "appointmentTime": "14:30:00"
}
```

### **Response**

```json
{
  "id": 56789,
  "masterName": "John Smith",
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
  "appointmentEndTime": "11:00:00",
  "status": "CONFIRMED"
}

```
## appointmentEndTime Sahəsi Haqqında

- `appointmentEndTime` sahəsi **avtomatik təyin olunur**.
- Hər bir xidmətin müddəti nəzərə alınaraq hesablanır.  
  Məsələn, saç kəsimi 30 dəqiqə çəkirsə, bu müddət avtomatik olaraq `appointmentTime`-a əlavə olunur.
- Əlavə olaraq, sistem 10 dəqiqə **buffer vaxtı** da əlavə edir ki, növbəti rezervasiya üçün vaxt qalır.
- Bu sahə müştəri və bərbər üçün görüşün real son vaxtını göstərir və düzgün planlaşdırma təmin edir.

---

## **2. Appointmentləri Oxumaq**

* **Endpoint:** `{base_url}/customer/read-appointments`
* **Method:** `GET`
**Request:** Tələb olunmur. Login olmuş istifadəçiyə aid bütün görüşlər (ən son ediləndən ən əvvələ doğru sıralanmış) qaytarılır.

### **Response**

```json
[
  {
    "id": 56789,
    "masterName": "John Smith",
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
    "masterName": "John Smith",
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
    "masterName": "Emily White",
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

* **Endpoint:** `{base_url}/customer/update-appointment/{id}`
* **Method:** `PUT`

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

Aşağıdakı kimi sənə uyğun **README.md** mətni hazırladım — sən bunu birbaşa layihənin kök qovluğuna qoysan, master API-ləri üçün izahlı sənəd olacaq.

---

## Master (Entity) API Documentation

Bu sənəd, master rolundakı istifadəçilərin öz profilini idarə etməsi və müştərilərin masterlər haqqında məlumat axtarması üçün nəzərdə tutulmuş API-lərin təsvirini təqdim edir.

---

## 🔍 Public Endpoints

### 1. **Bütün masterləri Oxumaq (Search Result)**

**Endpoint:**

```
GET {base_url}/public/get-masters
```

**Açıqlama:**
Axtarış ekranında bütün masterləri göstərmək üçün istifadə olunur.

**Response nümunəsi:**

```json
[
{
  "id": 12345,
  "name": "John Smith",
  "profilePhotoUrl": "https://example.com/profiles/john_smith.jpg",
  "galleryPhotos": [
    "https://example.com/gallery/photo1.jpg",
    "https://example.com/gallery/photo2.jpg"
  ],
  "location": "123 Main Street, Baku, Azerbaijan",
  "rating": 4.8,
  "targetGender": "MALE",
  "services": [
    {
      "serviceId": 101,
      "serviceName": "Haircut",
      "price": 15.0,
      "durationMinutes": 30
    },
    {
      "serviceId": 102,
      "serviceName": "Keratin Treatment",
      "price": 30.0,
      "durationMinutes": 60
    }
  ],
  "reviews": [
    {
      "reviewId": 201,
      "customerName": "Jane Doe",
      "rating": 5,
      "comment": "Great haircut!"
    },
    {
      "reviewId": 202,
      "customerName": "Alex Brown",
      "rating": 4,
      "comment": "Friendly and professional."
    }
  ],
  "schedules": [
    {
      "dayOfWeek": "MONDAY",
      "startTime": "09:00:00",
      "endTime": "18:00:00"
    },
    {
      "dayOfWeek": "TUESDAY",
      "startTime": "09:00:00",
      "endTime": "18:00:00"
    }
  ],
  "is_available": false
}

]
```

---

### 2. **master Detalları (ID üzrə)**

**Endpoint:**

```
GET {base_url}/public/get-master/{id}
```

**Açıqlama:**
Seçilmiş master haqqında tam məlumat qaytarır.

**Response nümunəsi:**

```json
{
  "id": 12345,
  "name": "John Smith",
  "profilePhotoUrl": "https://example.com/profiles/john_smith.jpg",
  "galleryPhotos": [
    "https://example.com/gallery/photo1.jpg",
    "https://example.com/gallery/photo2.jpg"
  ],
  "location": "123 Main Street, Baku, Azerbaijan",
  "rating": 4.8,
  "targetGender": "MALE",
  "services": [
    {
      "serviceId": 101,
      "serviceName": "Haircut",
      "price": 15.0,
      "durationMinutes": 30
    },
    {
      "serviceId": 102,
      "serviceName": "Keratin Treatment",
      "price": 30.0,
      "durationMinutes": 60
    }
  ],
  "reviews": [
    {
      "reviewId": 201,
      "customerName": "Jane Doe",
      "rating": 5,
      "comment": "Great haircut!"
    },
    {
      "reviewId": 202,
      "customerName": "Alex Brown",
      "rating": 4,
      "comment": "Friendly and professional."
    }
  ],
  "schedules": [
    {
      "dayOfWeek": "MONDAY",
      "startTime": "09:00:00",
      "endTime": "18:00:00"
    },
    {
      "dayOfWeek": "TUESDAY",
      "startTime": "09:00:00",
      "endTime": "18:00:00"
    }
  ],
  "is_available": false
}

```

---

## 💼 Barber Endpoints

### 1. **master Profilini Görüntüləmək**

**Endpoint:**

```
GET {base_url}/master/profile
```
**Response**
```json
{
  "id": 12345,
  "name": "John Smith",
  "profilePhotoUrl": "https://example.com/profiles/john_smith.jpg",
  "galleryPhotos": [
    "https://example.com/gallery/photo1.jpg",
    "https://example.com/gallery/photo2.jpg"
  ],
  "location": "123 Main Street, Baku, Azerbaijan",
  "rating": 4.8,
  "targetGender": "MALE",
  "services": [
    {
      "serviceId": 101,
      "serviceName": "Haircut",
      "price": 15.0,
      "durationMinutes": 30
    },
    {
      "serviceId": 102,
      "serviceName": "Keratin Treatment",
      "price": 30.0,
      "durationMinutes": 60
    }
  ],
  "reviews": [
    {
      "reviewId": 201,
      "customerName": "Jane Doe",
      "rating": 5,
      "comment": "Great haircut!"
    },
    {
      "reviewId": 202,
      "customerName": "Alex Brown",
      "rating": 4,
      "comment": "Friendly and professional."
    }
  ],
  "schedules": [
    {
      "dayOfWeek": "MONDAY",
      "startTime": "09:00:00",
      "endTime": "18:00:00"
    },
    {
      "dayOfWeek": "TUESDAY",
      "startTime": "09:00:00",
      "endTime": "18:00:00"
    }
  ],
  "is_available": false
}
```

**Açıqlama:**
Giriş etmiş masterin profil məlumatlarını qaytarır.

---

### 2. **master Profilini Yeniləmək**

**Endpoint:**

<<<<<<< HEAD
```
PATCH {base_url}/master/update-masterProfile/
```
=======
## PATCH {base_url}/master/update-masterProfile/

**Açıqlama:**  
Bu endpoint **masterin profilini yeniləmək** üçün istifadə olunur.  

- Request body **entity**-yə əsaslanır və dinamikdir.  
- Əlavə olaraq, istifadəçi profil şəkli və qalereya şəkilləri əlavə edə bilər.  

### Parametrlər

| Parametr        | Tip                       | İzah |
|-----------------|---------------------------|------|
| `updates`       | `Map<String, Object>`     | Yenilənəcək sahələr və onların dəyərləri |
| `profilePhoto`  | `MultipartFile`           | Profil şəkli (yalnız 1 şəkil seçilə bilər) |
| `galleryPhotos` | `MultipartFile[]`         | Qalereya üçün əlavə şəkillər (maksimum 5 şəkil seçilə bilər) |

### Qeyd

- `profilePhoto` sahəsi yalnız bir şəkil üçün nəzərdə tutulub.  
- `galleryPhotos` sahəsində maksimum 5 şəkil əlavə edilə bilər.  
- Əlavə edilmiş şəkillər `galleryPhotos` listinə əlavə olunur və mövcud şəkillər dəyişdirilə bilər.


**Açıqlama:**
masterin profilini yeniləmək üçün istifadə olunur. Request body **entity**-yə əsaslanır və dinamikdir Əlavə olaraq burda şəkil seçiləcək.

**Mümkün dəyişikliklər:**

* **location** — İş ünvanı
* **services** — Təklif olunan xidmətlər və qiymətləri
* **schedules** — İş günləri və saatları

---

## 📱 Frontend İstifadə Qaydası

master rolunda giriş edən istifadəçi:

1. **Ana ekranda** profilini yaratmaq və ya yeniləmək imkanı görəcək.
2. **Location** seçəcək.
3. **Xidmətlər** və qiymətləri əlavə edəcək.
4. **İş saatlarını** müəyyən edəcək.

Müştəri tərəfdə isə:

* **Axtarış ekranında** `get-masters` endpoint-dən gələn siyahı göstərilir.
* Seçilmiş masterin detalları `get-master/{id}` endpoint-i ilə gətirilir.

---
Aşağıdakı kimi sadə və aydın şəkildə **Review** API üçün README hissəsi hazırladım:

---

## ⭐ Review API

### 1. **Review vermək (yalnız müştərilər üçün)**

master rolundakı istifadəçilər review verə bilməzlər. Bu, masterlər arasında əsassız və mənasız rəylərin qarşısını almaq üçündür.

**Endpoint:**

```
POST {base_url}/customer/give-review/{id}
```

* `{id}` → Review veriləcək masterin unikal identifikatoru

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

masterin bütün review-ları `GET {base_url}/public/get-master/{id}` endpointindən alınır və frontenddə masterin profili və ya detalları göstərilərkən görünür.

---

**Qeyd:**

* Müştəri yalnız ulduz verə bilər, amma şərh yazarsa, ulduz vermək mütləqdir.
* masterlər review verə bilməz.

---

---

## 📅 Schedule (İş Qrafiki) API - master üçün

masterlər öz iş saatlarını idarə etmək üçün bu endpointlərdən istifadə edirlər.

---

### 1. **Yeni iş qrafiki yaratmaq**

**Endpoint:**

```
POST {base_url}/master/create-schedule
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
PATCH {base_url}/master/update-schedule/{id}
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
DELETE {base_url}/master/delete-schedule/{id}
```

* `{id}` — Silinəcək iş qrafikinin ID-si

**Response:**

* Boş (void), heç bir data qaytarılmır

---

Bu endpointlər masterlərə öz iş günlərini və saatlarını rahat idarə etməyə imkan verir.

---

Aşağıda **Service API** üçün README.md hissəsini hazırladım:

---

## 💈 Service API - masterin Təklif Etdiyi Xidmətlər

---

### 1. **Yeni xidmət yaratmaq**

**Endpoint:**

```
POST {base_url}/master/create-service
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
PATCH {base_url}/master/update-service/{id}
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
DELETE {base_url}/master/delete-service/{id}
```

* `{id}` — Silinəcək xidmətin ID-si

**Response:**

* Boş (void), heç bir data qaytarılmır

---

### 4. **Xidmətin təfərrüatlarını oxumaq**

**Endpoint:**

```
GET {base_url}/master/read-service/{id}
```

* `{id}` — Xidmətin ID-si

**Qeyd:**

* Bu endpoint frontend-də xidmətin detallarını göstərmək üçün istifadə olunur.
* Gələcəkdə silinə bilər, çünki məntiq olaraq master profilində göstərilən xidmətlərlə birlikdə gəlməsi daha faydalıdır.

---

### 5. **masterin bütün xidmətlərini oxumaq (gələcəkdə silinə bilər)**

**Endpoint:**

```
GET {base_url}/master/read-services/
```

* Bu endpoint masterin bütün xidmətlərini oxumaq üçün nəzərdə tutulub, amma məntiqi səbəbdən frontend-də istifadə etmək tövsiyə edilmir.
* Xidmətlər, review-lar və cədvəllər (`schedules`) bir yerdə profil ekranına yüklənməsi daha uyğundur.

---
```

```

---

## ⚠️ Problemlər və Gələcək Təkmilləşdirmələr

### 1. **Review-ların idarəsi**

* İstifadəçi artıq ulduz və ya review vermişsə, onun təkrar review verməsinin qarşısı alınmalıdır.
* İstifadəçilərə mövcud review-larını redaktə etmək imkanı əlavə edilməlidir ki, eyni şəxs yüzlərlə review göndərə bilməsin.

### 2. **Profilin silinməsi**

* İstifadəçilər və masterlər üçün profil silmək funksiyası əlavə edilməlidir.
* Bu, hesabların idarəsi və təhlükəsizliyi baxımından vacibdir.

### 3. **master axtarışı (search) funksiyası**

* masterləri ada görə axtarmaq funksiyası əlavə olunmalıdır.
* Bu, istifadəçilərin sevdikləri masterləri daha asan tapması üçün önəmlidir.

### 4. **Appointment vaxtının idarəsi**

* Hazırda rezervasiya zamanı yalnız başlanğıc vaxtı təyin olunur.
* Rezervasiyanın bitmə vaxtı (end time) hesablanmır və göstərilmir.
* Gələcəkdə end time servis müddətinə əsaslanaraq backend tərəfindən avtomatik hesablanmalıdır.
* Servislərin hər birinin müddətinə əlavə olaraq 10 dəqiqə tampon vaxt əlavə edilə bilər.



---









