## 프로젝트 이름

- 이 프로젝트는 Kotlin과 Java를 사용하여 개발된 서버 애플리케이션이며, 주요 기능은 피드 하이라이팅 기능입니다.

## 순서

## 🛠 기술 스택

- Language: Kotlin 1.9 (JVM 17)
- Backend: Spring Boot 3.2.5, JPA
- Database: MySQL 8.0
- API Testing: JUnit5
- Containerization: Docker, Docker Compose
- 인메모리 데이터 그리드: Redisson

## 확인 순서

1. Docker 실행 (Docker 설치 방법 : https://docs.docker.com/get-docker/)

- Docker Desktop을 설치하고 실행합니다.
- Docker Compose 를 설치하고 실행합니다.

```bash
docker-compose up -d # 프로젝트 루트 디렉토리에서 실행 
```

- MySQL 서버 시작: MySQL 서버가 시작되면 애플리케이션은 MySQL을 사용하여 데이터를 저장합니다.
- DDL 실행
    - (만약 테이블이 정상적으로 생성되지 않았다면) 아래 파일을 확인하여 MySQL 서버 내에 테이블을 생성합니다.
    - /src/main/resources/sql/ddl.sql

2. **애플리케이션 실행**

```bash
./gradlew bootRun
```

3. 테스트코드 실행 확인

```bash
./gradlew test
```

4. **API 호출**: Postman 링크를 통해 API를 호출할 수 있습니다.
   - https://documenter.getpostman.com/view/18460012/2sAYQi9mex

## 주요 기능

- 하이라이팅: 피드 하이라이팅 기능

## 실행계획

1. 피드 아이템과 페이지를 조회해오는 쿼리

```sql
SELECT fi.id,
       u.id,
       u.name,
       p.id,
       p.url,
       p.title,
       fi.created_at,
       fi.visibility
FROM feed_items fi
         JOIN users u ON fi.user_id = u.id
         JOIN pages p ON fi.page_id = p.id
WHERE (fi.visibility = 'PUBLIC')
   OR (fi.visibility = 'PRIVATE' AND fi.user_id = ?)
   OR (fi.visibility = 'MENTIONED' AND fi.id IN
                                    (SELECT feed_item_id
                                     FROM feed_item_mentioned_users
                                     WHERE mentioned_user_id = ?))
ORDER BY p.first_highlighted_at DESC;
```

- 인덱스 설계
  - 이 쿼리에서 주요한 부분은 feed_items.user_id, feed_items.page_id, 그리고 pages.first_highlighted_at입니다. 
  - 쿼리에서 WHERE 절의 조건들이 여러 개의 복잡한 논리 연산을 포함하고 있기 때문에, 각각의 조건에서 빠르게 조회되도록 visibility 인덱스를 추가해주었습니다.

```sql
CREATE INDEX idx_feed_item_user_id ON feed_items (user_id);
CREATE INDEX idx_feed_item_page_id ON feed_items (page_id);
CREATE INDEX idx_feed_item_visibility ON feed_items (visibility);
CREATE INDEX idx_feed_item_user_page_visibility ON feed_items (user_id, page_id, visibility);
```  

  - feed_items 테이블의 user_id와 page_id에 인덱스를 추가하고, feed_items.first_highlighted_at에도 인덱스를 추가하여 쿼리 성능을 크게 향상시켰습니다.
    - 참고로 feed_items.first_highlighted_at은 해당 유저의 피드 아이템 중 가장 최근에 하이라이팅된 페이지의 시간을 나타냅니다. 

```sql
CREATE INDEX idx_pages_first_highlighted_at ON feed_items (first_highlighted_at);
```

  - 서브쿼리 (SELECT feed_item_id FROM feed_item_mentioned_users WHERE mentioned_user_id = ?)는 성능에 영향을 미칠 수 있어, 이 부분에 인덱스를 추가하여 최적화 하였습니다.

```sql
CREATE INDEX idx_feed_item_mentioned_user_id ON feed_item_mentioned_users (mentioned_user_id);
```

2. 조회해온 피드 아이템의 세부 하이라이트 내역을 조회하는 쿼리

```sql
SELECT h.id, h.color, h.created_at, h.page_id, h.text, h.user_id
FROM highlights h
WHERE h.page_id IN (?, ?, ?)
  AND h.user_id = ?;
```

- 인덱스 설계
  - highlights 테이블에서 page_id와 user_id가 중요한 컬럼입니다. 두 컬럼에 인덱스를 추가하여 쿼리 성능을 높일 수 있습니다.
  -  이렇게 page_id와 user_id에 복합 인덱스를 추가하면, 해당 조건을 동시에 만족하는 레코드를 빠르게 조회할 수 있어 성능이 개선됩니다.


```sql
CREATE INDEX idx_highlight_page_user ON highlights (page_id, user_id);
```

## ERD 및 DDL 

![img.png](/src/main/resources/file/img.png)

```sql
CREATE TABLE users
(
    id         BigInt PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE TABLE pages
(
    id                 BigInt PRIMARY KEY,
    url                VARCHAR(2083) NOT NULL, -- URL 길이를 고려하여 2083자로 설정
    title              VARCHAR(255)  NOT NULL,
    user_id            BigInt        NOT NULL,
    created_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         VARCHAR(255),
    updated_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by         VARCHAR(255)
);

CREATE INDEX idx_page_user_id ON pages (user_id);

CREATE TABLE highlights
(
    id         BigInt PRIMARY KEY,
    text       TEXT         NOT NULL,
    color      VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    page_id    BigInt       NOT NULL,
    user_id    BigInt       NOT NULL
);

CREATE INDEX idx_highlight_page_id ON highlights (page_id);
CREATE INDEX idx_highlight_user_id ON highlights (user_id);
CREATE INDEX idx_highlight_page_user_id ON highlights (page_id, user_id);

CREATE TABLE feed_items
(
    id         BigInt PRIMARY KEY,
    user_id    BigInt      NOT NULL,
    page_id    BigInt      NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    visibility VARCHAR(20) NOT NULL,
    first_highlighted_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_feed_item_user_id ON feed_items (user_id);
CREATE INDEX idx_feed_item_page_id ON feed_items (page_id);
CREATE INDEX idx_feed_item_visibility ON feed_items (visibility);
CREATE INDEX idx_feed_item_user_page_visibility ON feed_items (user_id, page_id, visibility);
CREATE INDEX idx_feed_item_first_highlighted_at ON feed_items (first_highlighted_at);

CREATE TABLE feed_item_mentioned_users
(
    id                BigInt    PRIMARY KEY AUTO_INCREMENT,
    feed_item_id      BigInt    NOT NULL,
    mentioned_user_id BigInt    NOT NULL,
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(255),
    updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by        VARCHAR(255),
    first_highlighted_at TIMESTAMP     NULL
);

CREATE INDEX idx_feed_item_feed_item_id ON feed_item_mentioned_users (feed_item_id);
CREATE INDEX idx_feed_item_mentioned_users ON feed_item_mentioned_users (mentioned_user_id);
CREATE INDEX idx_feed_item_feed_item_id_mentioned_users ON feed_item_mentioned_users (mentioned_user_id, feed_item_id);

CREATE TABLE feed_item_highlights (
    id BigInt PRIMARY KEY,
    feed_item_id BigInt NOT NULL,
    highlight_id BigInt NOT NULL
);

CREATE INDEX idx_feed_item_highlight_feed_item_id ON feed_item_highlights (feed_item_id);
CREATE INDEX idx_feed_item_highlight_highlight_id ON feed_item_highlights (highlight_id);
```
