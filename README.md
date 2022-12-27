#  멋사스네스

---


## 미션 요구사항 분석 & 체크리스트

---
- [X] AWS EC2에 docker로 배포
- [X] Gitlab Cl & Crontab CD 구성
- [X] Swagger 구성
- [X] 회원가입 및 로그인 기능 구현
  - 인증, 인가 필터 구현
- [X] 포스트 작성 기능 구현
- [X] 포스트 조회 기능 구현
  - 상세 조회
  - 리스트 조회(Pagable)
- [X] 포스트 수정 기능 구현
- [X] 포스트 삭제 기능 구현
- [X] 테스트 코드

## N주차 미션 요약

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
