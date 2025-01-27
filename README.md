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

  - feed_items 테이블의 user_id와 page_id에 인덱스를 추가하고, pages.first_highlighted_at에도 인덱스를 추가하여 쿼리 성능을 크게 향상시켰습니다.

```sql
CREATE INDEX idx_pages_first_highlighted_at ON pages (first_highlighted_at);
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