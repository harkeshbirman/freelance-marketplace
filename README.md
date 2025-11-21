# Freelance Marketplace

A microservices-based Freelance Marketplace with a React frontend. Services are small, independently deployable Spring Boot apps (Auth, Project, Freelancer) each owning its DB, JWT-secured APIs, Logback logging and Actuator endpoints.

## Architecture

- Services: Auth, Project, Freelancer
- Each: own MySQL schema, Spring Security + JWT, SLF4J/Logback, /actuator (health, metrics)
- Inter-service comms: REST with JWT propagation

## Key endpoints

- Auth: register, login, issue/validate JWT
  - /api/auth/register, /api/auth/login, /api/auth/validate-token
- Project: client projects, required skills, match calculation
  - GET /projects/{id}/matches : matchScore = matchedSkills / totalRequiredSkills
- Freelancer: profiles, skills, bids
  - POST /bids
- /actuator/health and /actuator/metrics for all services

## JWT & Security

- All endpoints except /api/auth/\* require JWT
- JWT claims: email, role, issuedAt
- Forward JWT on inter-service calls; enforce role-based auth

## Frontend architecture

- Tech: React(Vite), React Router, Tailwind
- API client: Fetch API
- Auth flow: store JWT in localStorage, and retrieve it when a request is sent.
- Structure :
  - src/
    - pages/ (AuthPage, ClientDashboard, FreelancerDashboard)
    - components/ (shared UI)
    - context/ (AuthContext)
    - services/api.js
- Build & run:
  - npm install
  - npm run dev

## End-to-end

1. User logs in via Auth → receives JWT
2. Client creates project (Project service) with skills
3. Freelancer adds skills, fetches matches, submits bid (Freelancer service)
4. Services compute matchScore; frontend displays role-specific views

## Minimal setup

Requirements: Java 21, Spring Boot 3.5.x, MySQL 9+, Maven, Node 18+  
Backend: mvn spring-boot:run (per service)  
Frontend:

- cd frontend
- npm install
- npm run dev
