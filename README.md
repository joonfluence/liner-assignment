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
docker-compose up -d 
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

4. **API 호출**: Swagger 링크를 통해 API를 호출할 수 있습니다.

## 주요 기능

- 하이라이팅: 피드 하이라이팅 기능

## ERD 및 DDL, 실행계획

![ERD.png](/src/main/resources/file/img.png)

```sql
CREATE TABLE users (
    id BigInt PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL
);

CREATE TABLE pages (
    id BigInt PRIMARY KEY,
    url VARCHAR(2083) NOT NULL, -- URL 길이를 고려하여 2083자로 설정
    title VARCHAR(255) NOT NULL,
    user_id BigInt NOT NULL
);

CREATE INDEX idx_page_user_id ON pages (user_id);

CREATE TABLE highlights (
    id BigInt PRIMARY KEY,
    highlighted_text TEXT NOT NULL,
    color_bar VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    page_id BigInt NOT NULL,
    user_id BigInt NOT NULL
);

CREATE INDEX idx_highlight_page_id ON highlights (page_id);
CREATE INDEX idx_highlight_user_id ON highlights (user_id);

CREATE TABLE feed_items (
    id BigInt PRIMARY KEY,
    user_id BigInt NOT NULL,
    page_id BigInt NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    visibility VARCHAR(20) NOT NULL
);

CREATE INDEX idx_feed_item_user_id ON feed_items (user_id);
CREATE INDEX idx_feed_item_page_id ON feed_items (page_id);
CREATE INDEX idx_feed_item_visibility ON feed_items (visibility);

CREATE TABLE feed_item_mentioned_userss (
    feed_item_id BigInt NOT NULL,
    mentioned_user_id BigInt NOT NULL,
    PRIMARY KEY (feed_item_id, mentioned_user_id)
);

CREATE INDEX idx_feed_item_mentioned_users ON feed_item_mentioned_userss (mentioned_user_id);

CREATE TABLE feed_item_highlights (
    id BigInt PRIMARY KEY,
    feed_item_id BigInt NOT NULL,
    highlight_id BigInt NOT NULL
);

CREATE INDEX idx_feed_item_highlight_feed_item_id ON feed_item_highlights (feed_item_id);
CREATE INDEX idx_feed_item_highlight_highlight_id ON feed_item_highlights (highlight_id);
```