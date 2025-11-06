# 일정 관리 API 명세서 & ERD

> 레벨 요구사항(Lv 1 ~ Lv 4)을 기반으로 한 API 명세서와 ERD

---

## 개요

간단한 일정(CRUD) 서비스의 REST API 명세서입니다. 주요 원칙:

* 응답에서 `비밀번호`(password) 필드는 절대 반환하지 않습니다.
* `작성일(createdDateTime)`, `수정일(modifiedDateTime)`은 JPA Auditing(`@CreatedDate`, `@LastModifiedDate`)을 사용합니다.
* 일정의 고유 식별자(ID)는 DB에서 자동 생성(auto-increment) 됩니다.
* 일정 수정/삭제 시 클라이언트는 해당 일정의 `비밀번호`를 함께 전송하여 인증(check)을 수행합니다.

---

## 데이터 모델 (Entity)

### Schedule (일정)

* `num_id` : BIGINT, PK, AUTO_INCREMENT — 각 일정의 고유 ID
* `wri_title` : VARCHAR(50) — 일정 제목 (수정 가능)
* `wri_content` : TEXT — 일정 내용
* `wri_name` : VARCHAR(20) — 작성자명 (수정 가능)
* `password` : VARCHAR(20) — 일정 비밀번호
* `created_date_time` : DATETIME/TIMESTAMP — 작성일 (JPA Auditing으로 자동 세팅)
* `modified_date_time` : DATETIME/TIMESTAMP — 수정일 (JPA Auditing으로 자동 세팅)

추가 제약/인덱스:

* `NOT NULL` 필드: `wri_title`, `wri_content`, `wri_name`, `password`
* 인덱스: `wri_name` 컬럼에 인덱스를 두어 작성자 기반 조회 성능 향상

---

## SQL DDL 예시 (MySQL)

```sql
CREATE TABLE schedules (
  num_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  wri_title VARCHAR(50) NOT NULL,
  wri_content TEXT NOT NULL,
  wri_name VARCHAR(20) NOT NULL,
  password VARCHAR(20) NOT NULL,
  created_date_time DATETIME NOT NULL,
  modified_date_time DATETIME NOT NULL,
  INDEX idx_wri_name (wri_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## ERD (간단 다이어그램)

```
+-----------------------------+
|          schedules          |
+-----------------------------+
| num_id   (PK) BIGINT        |
| wri_title VARCHAR(50)       |
| wri_content TEXT            |
| wri_name  VARCHAR(20)       |
| password VARCHAR(20)        |
| created_date_time DATETIME  |
| modified_date_time DATETIME |
+-----------------------------+
```

---

## API 명세

### 1) 일정 생성 — `POST /schedules`

* 설명: 새로운 일정 생성
* Request Header:

    * `Content-Type: application/json`
* Request Body (JSON):

```json
{
  "wriTitle": "회의 준비",
  "wriContent": "회의 자료 준비 및 회의실 예약",
  "wriName": "홍길동",
  "password": "1234"
}
```

* 동작 설명:
    * `created_date_time`와 `modified_date_time`은 현재 시각으로 설정
* 성공 응답: `201 Created`

```json
{
  "numId": 1,
  "wriTitle": "회의 준비",
  "wriContent": "회의 자료 준비 및 회의실 예약",
  "wriName": "홍길동",
  "createdDateTime": "2025-11-06T09:00:00",
  "modifiedDateTime": "2025-11-06T09:00:00"
}
```

---

### 2) 전체 일정 조회(선택적 작성자 필터) — `GET /schedules`

* 설명: 작성자명 기준으로 필터링 가능하며, `modifiedDateTime` 기준 내림차순 정렬
* Query Parameters:

    * `wriName` (optional): 작성자명
* 예시 요청:

    * `GET /schedules` (전체 조회)
    * `GET /schedules?wriName=홍길동` (작성자별 조회)
* 성공 응답: `200 OK` (배열 반환)

```json
[
  {
    "numId": 1,
    "wriTitle": "A일정",
    "wriContent": "...",
    "wriName": "홍길동",
    "createdDateTime": "2025-11-05T10:00:00",
    "modifiedDateTime": "2025-11-05T12:00:00"
  },
  {
    "numId": 2,
    "wriTitle": "회의 준비",
    "wriContent": "회의 자료 준비",
    "wriName": "김유림",
    "createdDateTime": "2025-11-04T08:00:00",
    "modifiedDateTime": "2025-11-04T09:00:00"
  }
]
```

* 구현 권장사항:

    * Repository에서 DB 쿼리로 필터링 및 정렬 처리

        * 예: `findByWriNameOrderByModifiedDateTimeDesc(String wriName)`
        * 또는 `findAllByOrderByModifiedDateTimeDesc()`

---

### 3) 선택 일정 조회 — `GET /schedules/{numId}`

* 설명: 특정 일정 단건 조회
* Path Parameter: `numId` = `numId`
* 성공 응답: `200 OK`

```json
{
  "numId": 1,
  "wriTitle": "회의 준비",
  "wriContent": "회의 자료 준비 및 회의실 예약",
  "wriName": "홍길동",
  "createdAt": "2025-11-06T09:00:00",
  "updatedAt": "2025-11-06T09:00:00"
}
```

---

### 4) 일정 수정 — `PUT /schedules/{numId}`

* 설명: 특정 일정의 `wriTitle`, `wriName`만 수정 가능. 요청에 `password`가 반드시 포함되어야 함.
* Path Parameter: `numId`
* Request Header: `Content-Type: application/json`
* Request Body (JSON):

```json
{
  "wriTitle": "회의 준비 - 업데이트",
  "wriName": "홍길동",
  "password": "1234"
}
```

```json
{
  "numId": 1,
  "wriTitle": "회의 준비 - 업데이트",
  "wriContent": "회의 자료 준비 및 회의실 예약",
  "wriName": "홍길동",
  "createdAt": "2025-11-06T09:00:00",
  "updatedAt": "2025-11-06T10:15:00"
}
```

---

### 5) 일정 삭제 — `DELETE /schedules/{numId}`

* 설명: 특정 일정 삭제, 요청 시 `password` 전달 필요


```json
{ "password": "1234" }
```
* 성공 응답: `204 No Content`

---

## 구현 체크리스트

* [ ] Entity 및 Repository 구현
* [ ] JPA Auditing 설정(`@EnableJpaAuditing`, `@CreatedDate`, `@LastModifiedDate`)
* [ ] DTO 매핑 (Entity -> DTO)
* [ ] Password hashing (bcrypt)
* [ ] Validation 및 ExceptionHandler
* [ ] 테스트: 단위 + 통합 테스트

---

### 참고: 간단한 서비스 흐름 (요약)

1. 생성: 클라이언트 -> `POST /schedules` (password 전달) -> 서버 해시 저장 -> 201 반환
2. 조회(전체/작성자): 클라이언트 -> `GET /schedules[?wriName=]` -> DB에서 필터+정렬 -> DTO 반환
3. 조회(단건): `GET /schedules/{numId}` -> DTO 반환
4. 수정: `PUT /schedules/{numId}` + body(password 포함) -> 비밀번호 검증 -> 수정 -> DTO 반환
5. 삭제: `DELETE /schedules/{numId}` + body(password 포함) -> 비밀번호 검증 -> 삭제 -> 204
