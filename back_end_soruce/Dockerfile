## 1. build된 jar 파일이 있는 경우 (수동 빌드)
# FROM 사용할 플랫폼을(이미지) 받아옴
FROM eclipse-temurin:17-jre-alpine
#받아온 이미지의 시작 위치를 지정(플랫폼 마다 시작위치는 다를 수있음) /app 이다른 이름이 될 수있음
WORKDIR /app
#내 빌드 파일을 .app 폴더에 복사
COPY build/libs/*.jar ./
# plain이 붙은 파일을 제외하고 app.jar로 만들어 달라는 명령어 -v 가 해당하는 파일을 뺴고
RUN mv $(ls *.jar | grep -v plain) app.jar

# 파일을 아래와 같이 샐행해라
ENTRYPOINT ["java", "-jar", "app.jar"]

## 2. 자동 build 후 jar 파일로 실행되게 수정(멀티스테이징)
#FROM gradle:8.5-jdk17-alpine as build
#WORKDIR /app
## .(첫번째) : 현재 프로젝트 경로의 소스, .(두번쨰) gradle 이미지의 경로에 복사
## 현재 java 소스를 /app 폴더 로 복사
#COPY . .
### daemon 쓰레드를 쓰지 않음으로써 불필요한 리소스 낭비를 줄인다.
### - gradle 이미지는 기본적으로 백그라운드에서 프로세스(데몬)를 실행
### - 메모리에 JVM이나 빌드 정보를 캐싱
### - 다음 빌드 시 속도가 향상됨
#RUN gradle clean build --no-daemon -x test
## ============ 이 위 까지 빌드 과정 ================
#FROM eclipse-temurin:17-jre-alpine
#WORKDIR /app
## 수동 빌드와 달라진 점은 "--from=build /app/" 추가 됨
#COPY --from=build /app/build/libs/*.jar ./
#RUN mv $(ls *.jar | grep -v plain) app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]