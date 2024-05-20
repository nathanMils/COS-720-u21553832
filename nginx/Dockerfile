FROM nginx:alpine
RUN rm /etc/nginx/conf.d/default.conf

ARG NGINX_PROFILE=prod

COPY ./nginx.${NGINX_PROFILE}.conf /etc/nginx/conf.d