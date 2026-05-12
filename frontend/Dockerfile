# 개발용 Dockerfile
FROM node:20-alpine

WORKDIR /app

# 의존성 먼저 복사 (캐시 활용)
COPY package*.json .
RUN npm install

# 소스코드 복사
COPY . .

EXPOSE 5173

# --host 옵션: 컨테이너 외부에서 접근 가능하게
CMD ["npm", "run", "dev", "--", "--host"]
