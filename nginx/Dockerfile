FROM owasp/modsecurity-crs:nginx-alpine
USER root
ARG NGINX_PROFILE=prod
ARG MODSECURITY_PROFILE=critical
COPY ./nginx.${NGINX_PROFILE}.conf /etc/nginx/templates/conf.d/default.conf.template
COPY ./logging.conf /etc/nginx/templates/conf.d/logging.conf.template
COPY ./modsecurity.${MODSECURITY_PROFILE}.conf /etc/nginx/templates/conf.d/modsecurity.conf.template
ENV ALLOWED_METHODS="GET HEAD POST OPTIONS DELETE"
ENV MODSEC_RULE_ENGINE=on
RUN if [ "$MODSECURITY_PROFILE" = "extreme" ] ; then export BLOCKING_PARANOIA=3 ; else export BLOCKING_PARANOIA=1 ; fi