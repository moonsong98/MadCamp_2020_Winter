# madcamp4_back

### Category

| Method | URI       | Action               |
| ------ | --------- | -------------------- |
| GET    | /category | 카테고리 리스트 읽기 |

### Restaurant

| Method | URI                              | Action                                |
| ------ | -------------------------------- | ------------------------------------- |
| GET    | /restaurant                      | 모든 식당 리스트 읽기                 |
| GET    | /restaurant?category=category_id | 특정 카테고리에 속한 식당 리스트 읽기 |
| GET    | /restaurant/:restr_id            | 특정 식당 정보 읽기                   |
| POST   | /restaurant                      | 특정 식당 정보 추가                   |
| PUT    | /restaurant/:restr_id            | 특정 식당 정보 업데이트               |
| DELETE | /restaurant/:restr_id            | 특정 식당 정보 삭제                   |

### Authorization

| Method | URI                   | Action                                     |
| ------ | --------------------- | ------------------------------------------ |
| POST   | /auth/register        | 회원가입                                   |
| POST   | /auth/login           | 로그인                                     |
| POST   | /auth/logout          | 로그아웃                                   |
| POST   | /auth/update-password | 초기 비밀번호 변경 (Restaurant Owner 한정) |
| POST   | /auth/refresh-token   | Access token 재발급 (Refresh token 필요)   |
