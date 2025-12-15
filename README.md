# Paw Log 🐾
###### 2025-2 객체지향 프로그래밍 PetLab 팀 프로젝트 

<br>

## 🔴 Git 규칙

1. 코드 작성은 **각자 담당한 issue**에서 **branch 생성** 후 해당 브랜치에서 작업 (브랜치 이름 규칙은 하단 참고)
2. 되도록 하나의 `commit`에는 한 가지 목적만
3. `commit` 후 **본인이 작업한 브랜치**에 `push` (master, develop 브랜치로의 직접 `push` 금지)
4. **본인이 작업한** 브랜치에서 **정상 작동**되는 것을 확인한 뒤에, GitHub에서 **작업했던 브랜치 → develop**로 `PR (Pull Request)` 올리기
5. 충돌 방지를 위해 `git pull` 자주 실행하기, 충돌 시 로컬에서 해결 후 `push`
6. 불필요한 파일 포함 금지

<br>

## ⭐ commit message 작성 규칙 ― `<type>: <subject>`
###### 예) feat: implement read() method in Pet
### - `<type>`: 아래 중 해당하는 커밋의 성격 작성
* __feat__: 새로운 기능 추가, 기존의 기능을 요구 사항에 맞추어 수정
* __fix__: 버그 수정
* __build__: 빌드 관련 수정 / 모듈 설치 또는 삭제
* __chore__: 환경, 의존성, 설정 변경 등 코드 외 수정
* __design__: 사용자 UI 디자인 변경
* __style__: 코드 스타일, 코드 포맷 수정 (예: 세미콜론 누락, 들여쓰기 등)
* __refactor__: 기능 변화 없는 코드 수정 (예: 변수 이름 변경)
* __chore__: 빌드, 설정 파일, 라이브러리 업데이트 등, 그 외 기타 수정
* __docs__: 문서/주석 추가, 수정
* __test__: 테스트 코드 추가, 수정
### - `<subject>`: 수정/변경한 사항 작성
* 끝에 마침표를 적지 않음
* 명령문으로 작성 (과거형 X)
* 50자 이내로 간결하게 작성

<br>

## ⭐ branch 이름 규칙

* 기능 추가 : `feature/기능명` ex. feature/pet-Info-Ui
* 버그 수정 : `fix/버그명` ex. fix/login-error
* 리팩토링 : `refactor/클래스명` ex.refactor/PetManager
* 테스트용 : `test/기능명` ex. test/order-table

 <br>

## 🐈 Project ― 일정 및 진척도 관리
[링크](https://github.com/users/011-029/projects/1) 혹은 상단의 `Projects` 탭
