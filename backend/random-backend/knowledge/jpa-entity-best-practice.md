# JPA Entity Best Practice

## 완성 예시

```java
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;
}
```

```java
public enum Role {
    USER, ADMIN
}
```

---

## 어노테이션별 설명

### `@Entity`
이 클래스가 JPA가 관리하는 테이블과 매핑된다는 선언.

### `@Table(name = "users")`
매핑할 테이블 이름을 명시. 없으면 클래스명을 그대로 테이블명으로 사용.
클래스명과 테이블명이 다를 수 있으므로 항상 명시하는 게 명확해.

### `@Getter`
Lombok. 모든 필드의 getter를 자동 생성.
`@Setter`를 클래스 레벨에 붙이지 않는 이유는 엔티티의 상태 변경을 아무 곳에서나 할 수 있으면
어디서 데이터가 바뀌었는지 추적이 불가능해지기 때문. 변경이 필요한 필드만 메서드로 열어주는 게 표준.

### `@NoArgsConstructor(access = AccessLevel.PROTECTED)`
JPA는 내부적으로 리플렉션을 통해 기본 생성자를 호출해서 인스턴스를 만든다.
따라서 기본 생성자가 반드시 필요함.

`AccessLevel.PROTECTED`로 막는 이유:
- JPA 프록시는 기본 생성자에 접근 가능 (같은 패키지 또는 상속 관계)
- 외부에서 `new User()`로 빈 객체를 만드는 걸 방지
- `PUBLIC`으로 열면 초기화되지 않은 엔티티가 서비스 레이어에 돌아다닐 수 있음

### `@Builder`
Lombok. 객체 생성 시 어떤 필드에 어떤 값이 들어가는지 명확하게 표현 가능.

```java
// @Builder 없을 때
User user = new User("test@test.com", Role.USER); // 순서 실수 가능

// @Builder 있을 때
User user = User.builder()
        .email("test@test.com")
        .role(Role.USER)
        .build();
```

`@Builder`와 `@NoArgsConstructor`를 같이 쓸 때 주의:
`@Builder`는 내부적으로 모든 필드를 받는 생성자를 만드는데, `@NoArgsConstructor`와 충돌함.
`@AllArgsConstructor`를 같이 명시해줘야 해.

```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
```

### `@Id`
이 필드가 PK임을 선언.

### `@GeneratedValue(strategy = GenerationType.IDENTITY)`
PK 자동 생성 전략. `IDENTITY`는 DB의 AUTO_INCREMENT에 위임.
MySQL 사용 시 표준 선택.

| 전략 | 설명 | 적합한 DB |
|---|---|---|
| `IDENTITY` | DB AUTO_INCREMENT | MySQL, PostgreSQL |
| `SEQUENCE` | DB 시퀀스 객체 사용 | Oracle, PostgreSQL |
| `TABLE` | 별도 키 테이블 사용 | 범용 (성능 나쁨) |
| `AUTO` | JPA가 알아서 선택 | 비권장 (예측 불가) |

### `@Column`
필드와 컬럼의 매핑 세부 설정.

| 속성 | 설명 |
|---|---|
| `nullable = false` | NOT NULL 제약 |
| `unique = true` | UNIQUE 제약 |
| `length = 255` | VARCHAR 길이 (기본값 255) |
| `updatable = false` | UPDATE 시 이 컬럼 제외 |

`nullable = false`를 엔티티에 명시하는 이유:
DB 제약뿐 아니라 JPA 레벨에서도 null 체크가 동작해서 더 빠른 단계에서 오류를 잡을 수 있음.

### `@Enumerated(EnumType.STRING)`
enum 타입 필드를 컬럼에 저장할 때 사용.

`EnumType.STRING` vs `EnumType.ORDINAL`:

| 타입 | 저장 방식 | 문제 |
|---|---|---|
| `ORDINAL` | 0, 1, 2... (숫자) | enum 순서 바뀌면 데이터 깨짐 |
| `STRING` | "USER", "ADMIN" | 안전, 가독성 좋음 |

`ORDINAL`은 절대 쓰면 안 됨. enum에 값 하나 추가했을 때 기존 데이터 전체가 틀어질 수 있음.

---

## BaseTimeEntity

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
```

### `@MappedSuperclass`
이 클래스 자체는 테이블과 매핑되지 않고, 상속받는 엔티티들에게 컬럼만 물려줌.

### `@EntityListeners(AuditingEntityListener.class)`
JPA Auditing 기능 활성화. `createdAt`, `updatedAt`을 자동으로 채워주는 리스너.

### `@CreatedDate` / `@LastModifiedDate`
`createdAt`: 최초 저장 시 자동 입력
`updatedAt`: 저장/수정 시마다 자동 갱신

이게 동작하려면 Application 클래스에 `@EnableJpaAuditing`이 있어야 함:

```java
@SpringBootApplication
@EnableJpaAuditing
public class RandomBackendApplication { ... }
```

---

## 나쁜 예 정리

```java
@Getter
@Setter          // 클래스 레벨 Setter — 어디서든 상태 변경 가능, 추적 불가
@AllArgsConstructor  // public 전체 생성자 — 필드 순서 실수 위험
public class User {

    @GeneratedValue(strategy = GenerationType.AUTO)  // 예측 불가
    private Long id;

    private String role;  // String — "admin", "ADMIN", "Admin" 전부 허용

    @Enumerated(EnumType.ORDINAL)  // 숫자 저장 — 순서 변경 시 데이터 깨짐
    private Role role;
}
```
