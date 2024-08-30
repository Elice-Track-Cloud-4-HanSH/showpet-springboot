# ShowPet

## 설명
우리동네 반려동물을 자랑할 수 있는 커뮤니티 사이트 입니다.

## 목차
- [기술 스택](#기술-스택)
- [기능](#기능)
- [사용법](#사용법)
- [기타](#기타)

## 기술 스택
### 서버 사이드
- **Spring Boot**: 자바 기반의 웹 애플리케이션 프레임워크. RESTful API 개발 및 웹 애플리케이션을 지원합니다.
- **Java 22**: 최신 기능과 안정성을 제공하는 자바의 최신 버전입니다.
- **Spring Security**: 인증 및 권한 부여를 위한 보안 프레임워크.
- **Spring Data JPA**: 데이터베이스와의 상호작용을 간소화하는 ORM 프레임워크.
- **Spring Data JDBC**: 자바 객체와 관계형 데이터베이스 간의 매핑을 지원하는 프레임워크.
- **H2 Database**: 테스트와 개발을 위한 인메모리 데이터베이스.
- **MySQL**: 배포 시 데이터 영속 저장을 위한 데이터베이스
- **AWS S3 Bucket**: 파일 저장을 위한 외부 Object 데이터베이스

### 프론트 엔드
- **Thymeleaf**: 서버 사이드 렌더링 템플릿 엔진. HTML 템플릿을 통해 동적 웹 페이지를 생성합니다.

### 빌드 도구
- **Gradle**: 프로젝트의 빌드 및 의존성 관리를 위한 도구.

### 개발 및 운영 도구
- **GitLab**: 버전 관리 시스템.

### 기타
- **Lombok**: 자바 코드의 반복을 줄여주는 라이브러리. 데이터 모델 클래스의 boilerplate 코드 생성을 간소화합니다.
- **Postman**: API 테스트 및 디버깅 도구.
- **MapStruct**: 객체 간의 매핑을 자동으로 생성하는 매퍼 프레임워크.

## 기능
- **회원가입**
  - id/pw를 이용한 로그인 기능 제공
  - 
- **카테고리별 조회**
  - 카테고리별 게시글을 작성하여 관련된 게시글을 한번에 확인 가능
  - S3 Bucket을 이용한 이미지 업로드 기능 제공
    - 게시판 삭제 시 해당 게시판의 전체 게시글 삭제
- **게시글 관리**
  - 비회원 게시글 작성, 수정, 삭제 및 조회 기능
  - S3 Bucket을 이용한 이미지 업로드 기능 제공
    - 게시글 삭제 시 해당 게시글의 전체 댓글 삭제
- **댓글 시스템**
  - 게시글에 댓글 추가 및 수정, 삭제 
  - 비밀번호 일치 시 댓글 수정/삭제

## 사용법
- **database**
  - `springboot_db` 데이터베이스를 생성합니다.
- **환경변수**
  - **s3bucket.yaml**:
  ``` yaml
  aws:
    s3:
      bucket:
        name: {bucketname}
      stack.auto: false
      region.static: ap-northeast-2
      credentials:
        accessKey: {ACCESS_KEY}
        secretAccessKey: {SECRET_ACCESS_KEY}
  ```
  - **mysql.yaml**:
  ```yaml
  spring:
    datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://127.0.0.1:3306/springboot_db
      username: springuser
      password: password
  ```
  - **h2.yaml**:
  ```yaml
  spring:
    datasource:
      driver-class-name: org.h2.Driver
      # https://kukim.tistory.com/105
      url: jdbc:h2:mem:showpet
      username: sa
      password: ''
  ```

- **기본 사용법**: 애플리케이션을 실행한 후 웹 브라우저에서 `http://localhost:8080`으로 접근합니다.
  - **게시판**
    - **조회**:
      - `/category` 엔드포인트로 전체 게시판 목록을 조회합니다.
      - `/category/{id}` 엔드포인트로 특정 게시판 목록을 조회합니다.
    - **생성**:
      - `/category` 엔드포인트에서 생성 가능합니다.
      - `/category/new` 엔드포인트로 직접 이동하여 생성할 수 있습니다.
  - **게시글**
    - **조회**:
      - 특정 카테고리에서 게시글 전체 조회 및 특정 게시글 조회가 가능합니다.
      - `/articles/{id}` 엔드포인트로 직접 이동 가능합니다.
    - **작성**:
      - 특정 게시판 페이지에서 작성이 가능합니다.
      - `/articles/add?category-id={id}` 엔드포인트로 이동하여 새 게시글을 작성할 수 있습니다.
        - `category-id`가 없는 경우 default는 1입니다.
      - 작성 시 비밀번호를 요구합니다.
  - **댓글**: 
    - **추가**: 게시글 페이지에서 댓글을 추가합니다. 추가 시 비밀번호를 요구합니다.
    - **수정**: 게시글 페이지에서 댓글을 수정합니다. 수정 시 비밀번호를 요구합니다.
    - **삭제**: 게시글 페이지에서 댓글을 삭제합니다. 삭제 시 비밀번호를 요구합니다.

## 기타
- **참고 링크**: [블로그 포스트](https://www.notion.so/elice-track/6-SHOWPET-2cf07fd54da84c2abbce8b3985981984?pvs=25)