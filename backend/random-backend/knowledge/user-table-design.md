# User 테이블 설계

## 기본 원칙

- 패스워드는 컬럼으로 저장한다. 단, **평문이 아닌 BCrypt 해시값**
- 소셜 로그인을 지원할 경우 **테이블을 분리**하는 것이 표준

---

## 권장 구조 (일반 로그인 + 소셜 로그인 병행)

### users — 공통 정보

```sql
CREATE TABLE users (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    email       VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_users_email (email)
);
```

### user_credentials — 일반 로그인 (이메일/패스워드)

```sql
CREATE TABLE user_credentials (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    user_id     BIGINT       NOT NULL,
    password    VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_credentials_user (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### user_oauth — 소셜 로그인

```sql
CREATE TABLE user_oauth (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    user_id         BIGINT       NOT NULL,
    provider        VARCHAR(20)  NOT NULL,   -- GOOGLE, KAKAO, NAVER 등
    provider_id     VARCHAR(255) NOT NULL,   -- 각 provider의 고유 식별자
    PRIMARY KEY (id),
    UNIQUE KEY uq_oauth_provider (provider, provider_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

---

## 이 구조를 선택한 이유

| 이유 | 설명 |
|---|---|
| 복수 소셜 연동 지원 | 한 유저가 Google + Kakao 둘 다 연동 가능 (user_oauth 행만 추가) |
| 계정 연동 확장 용이 | 일반 계정에 나중에 소셜 연동 추가 가능 |
| nullable 컬럼 없음 | 각 테이블 컬럼이 명확, null 체크 불필요 |

---

## 나쁜 예 — 단일 테이블에 전부 넣기

```sql
CREATE TABLE users (
    ...
    password        VARCHAR(255),  -- 소셜 유저는 NULL
    provider        VARCHAR(20),   -- 일반 유저는 NULL
    provider_id     VARCHAR(255)   -- 일반 유저는 NULL
);
```

**문제점**
- nullable 컬럼이 늘어날수록 애플리케이션 코드에서 null 체크 강제
- 어떤 방식으로 가입한 유저인지 파악하기 어려움
- 복수 소셜 연동 불가 (행이 하나뿐이므로)

---

## 패스워드 컬럼 주의사항

| 패턴 | 문제 |
|---|---|
| `VARCHAR(50)` | BCrypt 해시 결과는 60자 — 잘릴 수 있음 |
| 평문 저장 | 보안 사고 시 전체 유출 |
| `VARCHAR(255)` | 권장 — 알고리즘 변경 여지 확보 |
