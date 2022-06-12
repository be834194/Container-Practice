#命令 引数 という形で記述する
#必ずFROM命令で開始する
#FROMは複数記述できる(それ以前の命令で作られた状態が、クリアになることに注意)

#イメージ名:タグ
FROM maven:3.8.5-eclipse-temurin-17-alpine
COPY ./src ./src
COPY ./pom.xml .
#/bin/sh -c ○○の形でコマンドが実行される
RUN mvn package

#イメージ名:タグ
FROM openjdk:17
COPY ./target/Container-Practice-0.0.1-SNAPSHOT.jar /app/Container-Practice-0.0.1-SNAPSHOT.jar
#["実行ファイル", "パラメータ1", "パラメータ2"]
ENTRYPOINT ["java", "-jar", "/app/Container-Practice-0.0.1-SNAPSHOT.jar"]