FROM owasp/modsecurity-crs:nginx-alpine
USER root
ARG NGINX_PROFILE=prod
ARG MODSECURITY_PROFILE=critical
COPY ./nginx.${NGINX_PROFILE}.conf /etc/nginx/templates/conf.d/default.conf.template
COPY ./logging.conf /etc/nginx/templates/conf.d/logging.conf.template
COPY ./modsecurity.${MODSECURITY_PROFILE}.conf /etc/nginx/templates/conf.d/modsecurity.conf.template