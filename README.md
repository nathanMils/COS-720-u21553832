# Project Overview

## Frontend
- **Framework**: Vite Vue.js
- **CSS Framework**: Tailwind CSS

## Backend
- **Framework**: Spring Boot
- **Database**: Postgres (AWS RDS)
- **Proxy**: NGINX

## Cloud Service Provider
- **Platform**: AWS
- **Deployment**: Using EC2 instance and docker compose

## Version Control
- **Repository**: GitHub

## Logging
- **AWS CloudWatch/CloudWatch Agent**: Collects logs and forwards them to central repo for analysis and alerting

## Testing
- **Backend**: JUnit
- **Frontend**: Cypress

## Security Testing
- **OWASP ZAP**: Web application and Rest API scanning (full/baseline) both as part of CI/CD and locally to allow stronger testing
- **SonarQube**: Spring static java code analysis, done locally to avoid costs
- **Image/ Container Scanning**: Docker desktop with Docker Scout, done locally (too expensive)
- **EC2 Scanning**: Using AWS inspector to scan EC2 instance for vulnerabilities (only for free trial period)