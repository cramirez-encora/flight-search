FROM node:20-alpine AS build
WORKDIR /app
COPY . .
RUN npm install --legacy-peer-deps && npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
RUN echo "=================" && cat /etc/nginx/conf.d/default.conf && echo "================="
EXPOSE 80