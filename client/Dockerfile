FROM node:lts-alpine

WORKDIR /client

COPY ./client/package.json ./
COPY ./client/package-lock.json ./

RUN npm install

COPY . .

EXPOSE 5173

CMD ["npm", "run", "dev"]
