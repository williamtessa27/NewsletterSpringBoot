# Newsletter Spring Boot — Projet fil rouge

Ce dépôt accompagne une série de newsletters pour apprendre Spring Boot progressivement, du niveau débutant au niveau avancé.

## Objectif

Construire une API REST complète étape par étape :

- architecture en couches
- CRUD
- base de données
- validation
- gestion des erreurs
- DTO et mapping
- sécurité
- tests
- documentation
- déploiement

## Stack

- Java 21
- Spring Boot 3.5.14
- Maven
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 Database
- Spring Security
- OAuth2 Resource Server JWT

## Structure

```text
src/main/java/com/newsletter/springboot
├── controller
├── service
├── repository
├── model
├── dto
├── mapper
├── exception
└── config
```

## Lancer le projet

```bash
mvn spring-boot:run
```

L'application démarre sur :

```text
http://localhost:8080
```

Console H2 :

```text
http://localhost:8080/h2-console
```

Paramètres H2 :

```text
JDBC URL: jdbc:h2:mem:newsletterdb
User: sa
Password: laisser vide
```


## Sécurité JWT

Depuis l'étape #6, les routes `/api/tasks/**` sont protégées.

Comptes de test :

```text
admin / admin123
user  / user123
```

### Générer un token

```http
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Réponse :

```json
{
  "tokenType": "Bearer",
  "accessToken": "eyJ..."
}
```

### Utiliser le token

Ajoute ce header dans tes requêtes protégées :

```http
Authorization: Bearer VOTRE_TOKEN
```


## Endpoints disponibles

### Créer une tâche

```http
POST /api/tasks
Content-Type: application/json
```

```json
{
  "title": "Apprendre Spring Boot",
  "description": "Créer une API REST propre"
}
```

### Lister toutes les tâches

```http
GET /api/tasks
```

### Lire une tâche

```http
GET /api/tasks/1
```

### Lister les tâches terminées

```http
GET /api/tasks/completed
```

### Modifier une tâche

```http
PUT /api/tasks/1
Content-Type: application/json
```

```json
{
  "title": "Apprendre Spring Boot sérieusement",
  "description": "Comprendre les couches, JPA, DTO et validation",
  "completed": true
}
```

### Supprimer une tâche

```http
DELETE /api/tasks/1
```

## Roadmap newsletter

- #1 : Premier projet Spring Boot
- #2 : Architecture Controller / Service
- #3 : Base de données + JPA + CRUD
- #4 : Validation + gestion des erreurs
- #5 : DTO + mapping + architecture propre
- #6 : Spring Security + JWT
- #7 : Tests unitaires et tests d’intégration
- #8 : Documentation Swagger / OpenAPI
- #9 : Dockerisation
- #10 : Déploiement


## Branche recommandée pour cette étape

```bash
git checkout -b step-6-security-jwt
git add .
git commit -m "Ajout de Spring Security et authentification JWT"
git push -u origin step-6-security-jwt
```


## Commandes Git recommandées

```bash
git init
git add .
git commit -m "Initialisation du projet fil rouge Spring Boot"
git branch -M main
git remote add origin https://github.com/VOTRE-USERNAME/newsletter-spring-boot.git
git push -u origin main
```
