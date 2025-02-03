# Example-Schedule

## Schedule Lv.1 ~ 6

### ERD
![Image](https://github.com/user-attachments/assets/439c330f-3c05-46be-a96b-68b6b388401a)

### API 명세서
|기능|Method|URL| request  |response|상태코드|
|--|--|--|----------|--|--|
|할일 등록|`POST`|/schedule| 요청 body  |등록 정보|200: 정상등록|
|할일 목록 조회|`GET`|/schedule/| 요청 param |응답 정보|200: 정상조회|
|할일 단건 조회|`GET`|/schedule/{id}| -        |응답 정보|200: 정상조회|
|할일 수정|`PATCH`|/schedule/{id}| 요청 body  |응답 정보|200: 정상수정|
|할일 삭제|`DELETE`|/schedule/{id}| 요청 body  |-|200: 정상삭제|

https://documenter.getpostman.com/view/15624547/2sAYX2NjTG

