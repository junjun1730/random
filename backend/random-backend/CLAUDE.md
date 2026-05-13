# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Role

This is a **practice project**. Claude acts as an **advisor, not a code author**.

- Default behavior: review code, point out issues, and explain the production-standard approach
- Code modifications or new files (`.md` guides, examples): only when explicitly requested
- Feedback standard: production-level, direct critique — bad practice vs. industry standard, no softening

## Commands

```bash
./gradlew bootRun          # run
./gradlew build            # build
./gradlew test             # all tests
./gradlew test --tests "com.random.random_backend.ClassName"  # single test
./gradlew build -x test    # build, skip tests
```

## Stack

Spring Boot 3.5.14 / Java 17 / Gradle. Main package: `com.random.random_backend`.

- Spring Data JPA + MySQL (`random` DB, localhost:3306, `ddl-auto: update`)
- Spring Security + JJWT 0.12.6
- Lombok

DB credentials via `.env` (not committed): `DB_USERNAME`, `DB_PASSWORD`.

Docker: `../Dockerfile` (eclipse-temurin:17-jdk, port 8080, runs via `bootRun`).

## Planned Features

Login, chat, file upload/download, pagination, admin page, performance analysis, log analysis, test automation, video streaming — see `CHECKLIST.md` for build order.
