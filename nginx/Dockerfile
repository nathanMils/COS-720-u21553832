FROM owasp/modsecurity-crs:nginx-alpine
USER root
ARG NGINX_PROFILE=prod
ARG MODSECURITY_PROFILE=critical
COPY ./nginx.${NGINX_PROFILE}.conf /etc/nginx/templates/conf.d/default.conf.template
COPY ./logging.conf /etc/nginx/templates/conf.d/logging.conf.template
COPY ./modsecurity.${MODSECURITY_PROFILE}.conf /etc/nginx/templates/conf.d/modsecurity.conf.template

# Parameters for ModSecurity

# Security Engine Setting On -> Enable ModSecurity | Off -> Disable ModSecurity | DetectionOnly -> Enable ModSecurity in DetectionOnly mode
ENV MODSEC_RULE_ENGINE=On

# Logging
ENV MODSEC_AUDIT_LOG=/var/log/nginx/modsec_audit.log
ENV MODSEC_AUDIT_FORMAT=JSON

# CRS Allowed HTTP methods
ENV ALLOWED_METHODS="GET HEAD POST OPTIONS DELETE"

# CRS Sensitivity Settings
ENV PARANOIA=2
ENV BLOCKING_PARANOIA=2
ENV DETECTION_PARANOIA=3
ENV ANOMALY_INBOUND=4

# ModSecurity Default Actions
ENV MODSEC_DEFAULT_PHASE1_ACTION='phase:1,log,auditlog,deny,tag:\"\${MODSEC_TAG}\"'
ENV MODSEC_DEFAULT_PHASE2_ACTION='phase:2,log,auditlog,pass,tag:\"\${MODSEC_TAG}\"'

# ModSecurity Access Rights
ENV MODSEC_REQ_BODY_ACCESS=On