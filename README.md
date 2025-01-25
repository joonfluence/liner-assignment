## 프로젝트 이름

- 이 프로젝트는 Kotlin과 Java를 사용하여 개발된 서버 애플리케이션이며, 주요 기능은 피드 하이라이팅 기능입니다. 

## 순서


## 주요 기능

- 하이라이팅: 피드 하이라이팅 기능

## ERD 및 DDL, 실행계획

```sql

```

```sql

```

## 🛠 기술 스택

- Backend: Spring Boot, JPA
- Database: MySQL
- API Testing: JUnit
- Containerization: Docker

## 설치 방법

1. 프로젝트 클론

```
git clone https://github.com/username/repository-name.git
cd repository-name
```
2. 환경 변수 설정

다음 환경 변수를 설정합니다:

```
export DB_HOST=your-database-host
export DB_USER=your-database-user
export DB_PASSWORD=your-database-password
```

3. 의존성 설치

Gradle을 사용하여 의존성을 설치합니다.

```
./gradlew build
```

4. 데이터베이스 설정

application.properties 또는 application.yml 파일을 설정하여 데이터베이스 연결 정보를 구성합니다.

5. 서버 실행

```
./gradlew bootRun
```

6. Docker로 실행 (선택 사항)

```
docker build -t project-name .
docker run -p 8080:8080 project-name
```

7. 단위 테스트

코드 변경 후 단위 테스트를 실행하여 기능이 정상적으로 동작하는지 확인합니다.

```
./gradlew test
```

2. 통합 테스트

SpringBootTest와 MockMvc를 사용하여 API 통합 테스트를 실행할 수 있습니다.

```
./gradlew test --tests "com.example.project.SomeTest"
```
