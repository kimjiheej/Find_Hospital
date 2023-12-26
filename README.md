## <img src="https://img.shields.io/badge/Android-색상?style=for-the-badge&logo=Android&logoColor=white"> 경기도 병원 찾기 애플리케이션


## ❗ 어떠한 앱인가   
- 모바일 응용 수업의 기말 개인 프로젝트이다.
- 안드로이드 스튜디오로 제작한 앱이다. (kotlin 사용) 
- 경기도에 있는 병원들을 구 별로 검색할 수 있다. openAPI - (용인구.수지구.처인구) 
- 검색한 병원의 위치와 실제 사용자의 위치의 경로를 볼 수 있다. (구글맵 사용) 
- 찾은 병원을 개인 다이어리에 저장할 수 있다. 


## 🛠 사용 기술. 스택   
🛠   <img src="https://img.shields.io/badge/Android-색상?style=for-the-badge&logo=Android Studio&logoColor=white">
<img src="https://img.shields.io/badge/SQLite-0099E5?style=for-the-badge&logo=SQLite&logoColor=white">
<img src="https://img.shields.io/badge/Kotlin-A100FF?style=for-the-badge&logo=Kotlin&logoColor=white">
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white) <br>
<br>
🛠  사용 API : GoogleMap openAPI(병원의 위치 마커 표시 기능) 
<br>
 경기도 병원 목록 : - [💜  공공데이터 포털 API ](https://www.data.go.kr/data/15097347/openapi.do
)   




## UI (서비스 페이지)
🍎 용인에 있는 구별로 (수지구.처인구.기흥구) 병원 리스트를 검색할 수 있다. <br>
🍎 병원 recyclerView 를 클릭하면 구글맵에서 병원의 위치를 볼 수 있다. 
🍎 사용자의 위치와 병원의 위치를 마커로 표시하여 polyline 으로 이어준다.<br>

![20231226_202957](https://github.com/kimjiheej/Find_Hospital/assets/66732343/1e51355b-6a53-4e24-900a-ffd0284908bf) ![20231226_205309](https://github.com/kimjiheej/Find_Hospital/assets/66732343/05bf1873-740c-4704-af1e-b0cc4f378123)


🍎 SQLiteDatabase 를 통해 recyclerView 의 병원을 롱클릭하면 저장할 수 있다.  <br>
🍎 다이어리에서 병원 이름을 이용하여 삭제도 할 수 있다. <br>
![yes](https://github.com/kimjiheej/Find_Hospital/assets/66732343/2d27dd19-99dc-4e2e-a70f-fd0da1e9d6c1) ![20231226_210322](https://github.com/kimjiheej/Find_Hospital/assets/66732343/4c1951ac-4b9c-4d33-bcf2-44f6d4504d6f)
