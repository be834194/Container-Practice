#命令 引数 という形で記述する
#必ずFROM命令で開始する

#イメージのベースを指定
FROM maven:3.8.5-eclipse-temurin-17-alpine AS builder
#作業ディレクトリの指定
WORKDIR /srv
COPY ./src ./src
COPY ./pom.xml .
#/bin/sh -c ○○の形でコマンドが実行される
RUN mvn package

#FROMは複数記述できる(それ以前の命令で作られた状態が、クリアになることに注意)
FROM openjdk:17
#--from=別名 で、構築したイメージを参照できる
COPY --from=builder /srv/target/Container-Practice-0.0.1-SNAPSHOT.jar /target/Container-Practice.jar
#["実行ファイル", "パラメータ1", "パラメータ2"]
ENTRYPOINT ["java", "-jar", "/target/Container-Practice.jar"]