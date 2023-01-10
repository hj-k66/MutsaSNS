#  멋사스네스🦁

---
```
💭 SNS의 주요 기능(회원 가입 및 로그인, 게시글 CRUD, 댓글 CRUD, 좋아요, 알람)의 API를 구현하고 배포한 개인 프로젝트입니다.
```  

✨ [Swagger 배포 링크](http://ec2-54-180-105-205.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/#/)  
📚 [프로젝트 문서](https://www.notion.so/936ccaa1d4cd4a588fc3a9ebc7080a22)
## 🛠 개발환경
- 에디터 : Intellij Ultimate
- 개발 툴 : SpringBoot 2.7.7
- 자바 : JAVA 11
-  빌드 : Gradle 7.5.1
- 서버 : AWS EC2 t2.micro
- 배포 : Docker
- 데이터베이스 : MySql 8.0
- 필수 라이브러리 : SpringBoot Web, MySQL, Spring Data JPA, Lombok, Spring Security

## ✔ 기능 

---
- 인프라 구축
  - [X] AWS EC2에 docker로 배포
  - [X] Gitlab Cl & Crontab CD 구성
  - [X] Swagger 구성
- 회원 가입 및 로그인
  - [X] 회원가입 및 로그인 기능 구현
    - 인증, 인가 필터 구현
- 게시글 CRUD
  - [X] 포스트 작성 기능 구현
  - [X] 포스트 조회 기능 구현
    - 상세 조회
    - 리스트 조회(Pagable)
  - [X] 포스트 수정 기능 구현
  - [X] 포스트 삭제 기능 구현
    - soft delete
- 댓글 CRUD
  - [X] 댓글 목록 조회 기능 구현
  - [X] 댓글 작성 기능 구현
  - [X] 댓글 수정 기능 구현
  - [X] 댓글 삭제 기능 구현
    - soft delete
- 좋아요
  - [X] 좋아요 누르기 기능 구현
    - 중복 시 좋아요 취소
  - 좋아요 갯수 조회 기능 구현
- 마이피드
  - [X] 마이피드 조회 기능 구현
    - 내가 작성한 글만 보이는 기능
- 알람
  - [X] 알림 리스트 조회 기능 구현
    - 로그인한 user의 알람 리스트 조회
    - 특정 포스트의 댓글 달리고, 좋아요 눌리면 알람 등록
- [X] 테스트 코드

## 📝 API 명세서
| 구분 | 기능 | METHOD | API |
| --- | --- | --- | --- |
| 유저 | 회원가입 | POST | /api/v1/users/join |
|  | 로그인 | POST | /api/v1/users/login |
| 게시글 | 게시글 전체 조회 | GET | /api/v1/posts |
|  | 게시글 단건 조회 | GET | /api/v1/posts/{id} |
|  | 게시글 작성 | POST | /api/v1/posts |
|  | 게시글 수정 | PUT | /api/v1/posts/{id} |
|  | 게시글 삭제 | DELETE | /api/v1/posts/{id} |
| 댓글 | 댓글 작성 | POST | /api/v1/posts/{postsid}/comments |
|  | 댓글 수정 | PUT | /api/v1/posts/{postsid}/comments/{commentId} |
|  | 댓글 삭제 | DELETE | /api/v1/posts/{postsid}/comments/{commentId} |
|  | 댓글 목록 조회 | GET | /api/v1/posts/{postsid}/comments |
| 좋아요 | 좋아요 누르기 | POST | /api/v1/posts/{postsid}/likes |
|  | 좋아요 갯수 조회 | GET | /api/v1/posts/{postsid}/likes |
| 마이피드 | 마이피드 조회 | GET | /api/v1/posts/my |
| 알람 | 알람 리스트 조회 | GET | /api/v1/alarms |

## 두번째 미션 요약

---

📝[전체 프로젝트 작업 목록](https://www.notion.so/936ccaa1d4cd4a588fc3a9ebc7080a22)
- [댓글 작성](https://www.notion.so/58bb10e2ea8b4eb1991656cab9a7cb58)
  - 로그인 안한 사용자 예외처리
    - Spring security 익명 사용자 학습
  - 토큰 관련 예외 처리
    - Custom Filter 학습
  - DTO, Entity 분리 이유 정리
- [댓글 수정](https://www.notion.so/8c1475d750f04c5ca08d97c76de00610)
  - jpa dirty checking 이용해 entity 값 변경 메소드 정의
    - jpa dirty checking 학습
    - 트랜잭션 학습
    - save(), saveAndFlush() 차이점 학습
- [댓글 삭제](https://www.notion.so/705af53a42d54ccd816fde0bb72a864f)
  - soft delete로 구현
  - 게시글 삭제 로직 변경
    - 게시글 삭제될 경우, 해당 게시글의 모든 좋아요, 댓글 삭제되게 수정
- [Validator](https://www.notion.so/f5c3e3d7ed2f402bbb42574de0edab63)
  - 공통된 검증로직은 Validator 클래스가 책임지게 분리
- [좋아요 누르기](https://www.notion.so/757a58ca4a9c45968282093a9ad04b5f)
  - 좋아요 2번 누르는 경우, 취소
  - Optional의 orElse(), orElseGet() 차이 학습
### 회고
#### 느낀점과 배운점
이번에 구현하면서 주어진 요구사항을 만족시키는 것 뿐만아니라 필요한 지식을 습득하고 정리하는데 집중을 했다.
트러블 슈팅을 했던 것이나 기능 구현을 위해 필요했던 개념들을 학습하고 정리하면서 내가 뭘 모르고 있었고 무엇을 이번 개발을 통해 배웠는지 꺠달을 수 있었다.  
jpa와 security는 여전히 어려웠지만 custom filter도 만들어보고 수정 로직을 짜면서 jpa에 대해 내부로직을 좀 더 이해할 수 있었다.
삭제를 soft delete로 구현하는 것과 좋아요 2번 누르면 취소되는걸 구현하는데 시간이 좀 걸렸지만 많은 걸 배울 수 있었고, 재미있게 개발했다.
#### 아쉬웠던 부분
기능 구현에 초점을 맞추느라 Test를 많이 신경쓰지 못했다. 특히 Service 로직이 이전 미션보다 복잡해져서 리팩토링 시 ServiceTest를 추가하고 싶다.

## 첫번째 미션 요약

---

**[접근 방법]**
```
목표 : 모든 결정에 항상 "왜?"를 생각하자!
```
### 고민했던 거
##### service와 repository 간 dto로 연결해야 할까?
  - 문제 : 여태까지 수업시간에서는 service와 repository를 dto를 사용하지 않고 entity로 가져왔었음.
#### JWT에는 user의 어떤 정보를 담는게 좋을까?
  - 문제 : JWT에는 많은 정보를 담는건 좋지 않으니,(왜? 좋지 않을까 찾아보기)
  - 해결과정
    - 가장 대표성이 있는 정보를 담자 >> userId 담음
    - userName으로 변경
      - 이미 요구사항에 userName을 써야하는 부분이 있어 userName이 적절함.
#### JWT Token Exception 처리를 어떻게 해야 할까?
  - 문제 : INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다.")
와 같이 JWT관련 처리는 `JwtTokenFilter` 에서 하고 있음 >> ExceptionHandler 영향이 여기까지 미치지 않음 >> 그럼 어떻게?
  - 해결과정
    - `CustomAccessDeniedHadler`, `CustomAuthenticationEntryPoint` 로   JWTFilter에서 생긴 오류를 처리 >> 결과로 나와야하는  Response객체를 Handler의 response의 바디값으로 파싱
#### 포스트 작성 및 수정 등에서 로그인 안했을 시 에러나는 시나리오를 Token이 아예 없는 걸로 해야하나?
- 하면 CustomAuthenticationEntryPoint 에러 실행되면서 INVALID_TOKEN 에러 발생 >> INVALID_PERMISSION 가 실행되야하지 않을까?

**[특이사항]**
#### 궁금한 점
- 회원가입이나 로그인 등 토큰이 필요없어 넣지 않는 메소드들도 JWTTokenFilter를 통과하는데 부분적으로 필터를 통과하게 할 순 없을까?
- 포스트 작성 실패 테스트
  - Contoller테스트로 수행했지만 when, thenThrow를 사용하는게 아닌 perform()  메소드만 수행하여 401에러 반환 테스트만 수행
