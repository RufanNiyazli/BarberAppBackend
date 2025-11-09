

# An app like Booksy App Backend

---

## 📌 Entities and Types

### 1. **User**

**Table:** `user`

| Field                       | Type                  | Description                                     |
| --------------------------- | --------------------- | ----------------------------------------------- |
| `id`                        | `Long`                | Unique identifier                               |
| `username`                  | `String`              | Username                                        |
| `email`                     | `String`              | Email address                                   |
| `password`                  | `String`              | Encrypted password                              |
| `role`                      | `RoleType` *(enum)*   | User role (**ADMIN**, **CUSTOMER**, **MASTER**) |
| `gender`                    | `GenderType` *(enum)* | Gender (**MALE**, **WOMAN**)                    |
| `phoneNumber`               | `String`              | Phone number                                    |
| `profilePicture`            | `String`              | Profile picture URL                             |
| `createdAt`                 | `LocalDateTime`       | Creation date                                   |
| `updatedAt`                 | `LocalDateTime`       | Last update date                                |
| `verificationCodeExpiresAt` | `LocalDateTime`       | Verification code expiration date               |
| `verificationCode`          | `String`              | Email verification code                         |
| `enabled`                   | `boolean`             | Whether the account is active or not            |

**Relationships:**

* `List<Appointment>` — **OneToMany** (customer reservations)
* `List<RefreshToken>` — **OneToMany** (refresh tokens)

---

# Master Entity

**Table:** `master`

This entity represents barbers (masters) and their detailed information.

## Fields

| Field             | Type                  | Description                                |
| ----------------- | --------------------- | ------------------------------------------ |
| `id`              | `Long`                | Unique identifier                          |
| `user`            | `User`                | Associated user (OneToOne)                 |
| `name`            | `String`              | Master’s name                              |
| `profilePhotoUrl` | `String`              | Profile picture URL                        |
| `galleryPhotos`   | `List<String>`        | Gallery of additional photos               |
| `location`        | `String`              | Address                                    |
| `rating`          | `Double`              | Overall rating                             |
| `targetGender`    | `GenderType` *(enum)* | Gender served                              |
| `createdAt`       | `LocalDateTime`       | Creation date                              |
| `updatedAt`       | `LocalDateTime`       | Last update date                           |
| `is_available`    | `Boolean`             | Availability status                        |
| `masterType`      | `MasterType`          | Type of master (Barber, Hairdresser, etc.) |

## Relationships

* `List<Service>` — **OneToMany** (offered services)
* `List<Appointment>` — **OneToMany** (accepted appointments)
* `List<Review>` — **OneToMany** (customer reviews)
* `List<Schedule>` — **OneToMany** (work schedule)

## Notes

* `galleryPhotos` uses `@ElementCollection` to store multiple images.
* `is_available` shows whether the master is currently available.
* All OneToMany relations are mapped via the `mappedBy` attribute.

### About `is_available` Field

* The field **defaults to `false`**.
* Indicates whether the master is **working today**.
* Each time the master opens the app, the system shows a notification.
* This allows real-time monitoring of the master’s daily work status.
* Purpose: helps customers see if a master is currently active or unavailable.

### About `masterType` Field

* Indicates whether this person is a **barber**, **hairdresser**, or **tattoo/laser artist**.
* Purpose: to ensure only relevant service types appear for that master type on the frontend.

---

### 3. **Appointment**

**Table:** `appointment`

| Field             | Type                         | Description               |
| ----------------- | ---------------------------- | ------------------------- |
| `id`              | `Long`                       | Unique identifier         |
| `customer`        | `User`                       | Customer who made booking |
| `master`          | `Master`                     | Booked master             |
| `services`        | `List<Service>`              | Selected services         |
| `appointmentDate` | `LocalDate`                  | Appointment date          |
| `appointmentTime` | `LocalTime`                  | Appointment start time    |
| `status`          | `ReservationStatus` *(enum)* | Reservation status        |
| `createdAt`       | `LocalDateTime`              | Creation date             |
| `updatedAt`       | `LocalDateTime`              | Last update date          |

---

### 4. **Service**

**Table:** `service`

| Field             | Type                   | Description             |
| ----------------- | ---------------------- | ----------------------- |
| `id`              | `Long`                 | Unique identifier       |
| `master`          | `Master`               | Master offering service |
| `serviceType`     | `ServiceType` *(enum)* | Type of service         |
| `description`     | `String`               | Service description     |
| `durationMinutes` | `Integer`              | Duration in minutes     |
| `price`           | `Double`               | Service price           |
| `createdAt`       | `LocalDateTime`        | Creation date           |
| `updatedAt`       | `LocalDateTime`        | Last update date        |

---

### 5. **Review**

**Table:** `reviews`

| Field       | Type            | Description           |
| ----------- | --------------- | --------------------- |
| `id`        | `Long`          | Unique identifier     |
| `customer`  | `User`          | Customer who reviewed |
| `master`    | `Master`        | Reviewed master       |
| `rating`    | `Integer`       | Rating (1–5 stars)    |
| `comment`   | `String`        | Review text           |
| `createdAt` | `LocalDateTime` | Created at            |

---

### 6. **Schedule**

**Table:** `schedule`

| Field       | Type            | Description       |
| ----------- | --------------- | ----------------- |
| `id`        | `Long`          | Unique identifier |
| `master`    | `Master`        | Related master    |
| `dayOfWeek` | `DayOfWeek`     | Day of the week   |
| `startTime` | `LocalTime`     | Work start time   |
| `endTime`   | `LocalTime`     | Work end time     |
| `createdAt` | `LocalDateTime` | Creation date     |
| `updatedAt` | `LocalDateTime` | Last update date  |

---

### 7. **RefreshToken**

**Table:** `refresh_token`

| Field       | Type      | Description            |
| ----------- | --------- | ---------------------- |
| `id`        | `Long`    | Unique identifier      |
| `token`     | `String`  | Refresh token string   |
| `createdAt` | `Date`    | Creation date          |
| `expiredAt` | `Date`    | Expiration date        |
| `revoked`   | `boolean` | Whether revoked or not |
| `user`      | `User`    | Token owner (user)     |

---

## 📌 Enums and Values

### **GenderType**

```java
MALE,
WOMAN
```

### **ReservationStatus**

```java
PENDING,    // Waiting
CONFIRMED,  // Confirmed
CANCELED    // Canceled
```

### **RoleType**

```java
ADMIN,
CUSTOMER,
MASTER
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

## Roles

Users are divided into **MASTER** and **CUSTOMER** roles to improve security —
a **CUSTOMER** cannot access **MASTER** endpoints.

## API URL Structure

There are 3 types of endpoints (based on security rules):

* `/public/**` — does **not** require authentication (e.g. `/public/login`, `/public/register`)
* `/master/**` — only accessible by **MASTER** users
* `/customer/**` — only accessible by **CUSTOMER** users

**Public endpoints include:**

* `/public/login`
* `/public/register`
* `/public/refresh-accessToken`
* `/public/verify-user`
* `/public/resend-code`

---

## Auth System

### Register

**Endpoint:**

```
POST {base_url}/public/register
```

**Request Body (JSON):**

```json
{
  "username": "exampleUser",
  "email": "user@example.com",
  "password": "SecurePass123!",
  "role": "ADMIN",  // can be "ADMIN", "CUSTOMER", or "MASTER"
  "gender": "MALE",
  "phoneNumber": "+1234567890"
}
```

> On the frontend, users should first choose a `role`, then fill in other details.
> The `gender` field helps show gender-based master recommendations.

---

### Login

**Endpoint:**

```
POST {base_url}/public/login
```

**Request Body (JSON):**

```json
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

> No need to send the role during login — the backend identifies the user role by email.

**Register & Login Response (JSON):**

```json
{
  "accessToken": "string",
  "refreshToken": "string",
  "roleType": "MASTER" // or "CUSTOMER"
}
```

---

### Email Verification

**Endpoint:**

```
POST {base_url}/public/verify-user
```

**Request Body:**

```json
{
  "email": "user@example.com",
  "verificationCode": "123456"
}
```

---

### Access Token Refresh

> **accessToken = 2 hours**, **refreshToken = 24 hours**

**Endpoint:**

```
POST {base_url}/public/refresh-accessToken
```

**Request Body:**

```json
{
  "refreshToken": "your-refresh-token-string"
}
```

---

### Resend Verification Code

**Endpoint:**

```
POST {base_url}/public/resend-code
```

**Request Body:**

```
"user@example.com"
```

---

## 📅 Appointment API Documentation

### 1. **Create Appointment**

Available for both roles:

* **Customer:** `{base_url}/customer/create-appointment`
* **Master:** `{base_url}/master/create-appointment`

**Method:** `POST`

**Request Body:**

```json
{
  "masterId": 1,
  "serviceIds": [67890, 54321],
  "appointmentDate": "2025-08-15",
  "appointmentTime": "14:30:00"
}
```

**Response:**

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

#### About `appointmentEndTime`

* Automatically calculated.
* Based on total duration of selected services.
* An extra 10-minute **buffer** is added for scheduling flexibility.
* This ensures accurate time management for both the customer and master.

---

### 2. **Get All Appointments**

**Endpoint:** `{base_url}/customer/read-appointments`
**Method:** `GET`

No request body.
Returns all appointments belonging to the logged-in user, sorted from newest to oldest.

**Response:**

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
  }
]
```

---

### 3. **Update Appointment**

**Endpoint:** `{base_url}/customer/update-appointment/{id}`
**Method:** `PUT`

**Request Body:**

```json
{
  "appointmentDate": "2025-08-15",
  "appointmentTime": "14:00:00",
  "serviceIds": [101, 102]
}
```

Response same as `create-appointment`.

---

### 4. **Delete Appointment**

**Endpoint:** `{base_url}/customer/update-appointment/{id}`
**Method:** `DELETE`

No request body.
Returns no content.

---

## 💈 Master (Entity) API Documentation

This section describes APIs for managing master profiles and public access for customers.

---

### 🔍 Public Endpoints

#### 1. **Get All Masters**

**Endpoint:**

```
GET {base_url}/public/get-masters
```

Shows all masters (for search screens).

**Response Example:**

```json
[
  {
    "id": 12345,
    "name": "John Smith",
    "profilePhotoUrl": "https://example.com/john.jpg",
    "galleryPhotos": ["https://example.com/gallery1.jpg"],
    "location": "123 Main St, Baku",
    "rating": 4.8,
    "targetGender": "MALE",
    "services": [...],
    "reviews": [...],
    "schedules": [...],
    "is_available": false
  }
]
```

---

#### 2. **Get Master by ID**

**Endpoint:**

```
GET {base_url}/public/get-master/{id}
```

Returns detailed info about a selected master.

---

### 💼 Master Endpoints

#### 1. **View Master Profile**

**Endpoint:**

```
GET {base_url}/master/profile
```

Shows the logged-in master’s full profile data.

---

#### 2. **Update Master Profile**

**Endpoint:**

```
PATCH {base_url}/master/update-masterProfile/
```

**Description:**
Used to update master profile details, including images.

| Parameter       | Type                  | Description                         |
| --------------- | --------------------- | ----------------------------------- |
| `updates`       | `Map<String, Object>` | Key-value pairs of fields to update |
| `profilePhoto`  | `MultipartFile`       | Single profile photo                |
| `galleryPhotos` | `MultipartFile[]`     | Up to 5 gallery photos              |

---

## ⭐ Review API

### 1. **Submit Review (Customers Only)**

**Endpoint:**

```
POST {base_url}/customer/give-review/{id}
```

`{id}` — Master ID being reviewed.

**Request Body:**

```json
{
  "rating": 5,
  "comment": "Excellent service!"
}
```

* `rating` is required (1–5)
* `comment` optional but requires rating if provided
* Masters **cannot** review other masters

---

### 2. **Get Reviews**

Reviews are included in:

```
GET {base_url}/public/get-master/{id}
```

---

## 🕒 Schedule API (For Masters)

### 1. **Create Work Schedule**

**Endpoint:**

```
POST {base_url}/master/create-schedule
```

**Request Body:**

```json
{
  "dayOfWeek": "MONDAY",
  "startTime": "09:00:00",
  "endTime": "17:00:00"
}
```

---

### 2. **Update Schedule**

**Endpoint:**

```
PATCH {base_url}/master/update-schedule/{id}
```

**Request Body:**
Update fields dynamically (`dayOfWeek`, `startTime`, `endTime`).

---

### 3. **Delete Schedule**

**Endpoint:**

```
DELETE {base_url}/master/delete-schedule/{id}
```

---

## 💇 Service API (For Masters)

### 1. **Create Service**

**Endpoint:**

```
POST {base_url}/master/create-service
```

**Request Body:**

```json
{
  "serviceType": "HAIRCUT",
  "description": "A classic men's haircut.",
  "durationMinutes": 45,
  "price": 35.00
}
```

---

### 2. **Update Service**

**Endpoint:**

```
PATCH {base_url}/master/update-service/{id}
```

**Request Body:** dynamic updates (`serviceType`, `description`, `durationMinutes`, `price`)

---

### 3. **Delete Service**

**Endpoint:**

```
DELETE {base_url}/master/delete-service/{id}
```

---

### 4. **Get Service Details**

**Endpoint:**

```
GET {base_url}/master/read-service/{id}
```

---

### 5. **Get All Services (Optional)**

**Endpoint:**

```
GET {base_url}/master/read-services/
```

*Returns all services for the logged-in master (may be deprecated in the future).*

---

## ⚠️ Known Issues and Future Improvements

1. **Review Management**

  * Prevent duplicate reviews from the same user.
  * Allow users to edit their existing reviews.

2. **Profile Deletion**

  * Add delete-account feature for users and masters.

3. **Master Search**

  * Add search by name feature for easy discovery.

4. **Appointment Time Handling**

  * Implement automatic calculation of `endTime` based on service durations and a 10-minute buffer.

---
## License

This project is licensed under the [MIT License](LICENSE).
