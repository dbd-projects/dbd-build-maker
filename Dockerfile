
FROM bigtruedata/sbt:0.13.15-2.11 as builder

WORKDIR /build
COPY ./ ./

RUN sbt dist


FROM openjdk:8-alpine3.8

WORKDIR /app
COPY --from=builder /build/target/universal/dbd-character-service-0.1.0.zip ./

RUN apk add unzip bash && unzip dbd-character-service-0.1.0.zip && rm dbd-character-service-0.1.0.zip
RUN chmod +x dbd-character-service-0.1.0/bin/dbd-character-service

EXPOSE 9000

CMD ["bash", "dbd-character-service-0.1.0/bin/dbd-character-service", "-Dplay.http.secret.key=asdfghjklas","-Dplay.evolutions.db.default.autoApply=true"]