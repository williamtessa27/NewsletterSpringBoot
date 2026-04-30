# Suivi des étapes de la newsletter

## Tags Git conseillés

Après chaque newsletter, créer un tag :

```bash
git tag newsletter-01
git push origin newsletter-01
```

Exemples :

- newsletter-01-premier-projet
- newsletter-02-architecture
- newsletter-03-crud-jpa
- newsletter-04-validation-erreurs
- newsletter-05-dto-mapping

## Branches optionnelles

Pour une formation plus structurée :

- step-01-premier-projet
- step-02-architecture
- step-03-crud-jpa
- step-04-validation-erreurs
- step-05-dto-mapping
- step-06-security-jwt


## Étape #6 — Spring Security + JWT

Branche recommandée :

```bash
git checkout -b step-6-security-jwt
```

Ajouts principaux :

- dépendance `spring-boot-starter-security`
- dépendance `spring-boot-starter-oauth2-resource-server`
- configuration stateless
- endpoint `/api/auth/login`
- génération de JWT
- protection des routes `/api/tasks/**`
- accès libre à `/h2-console/**` pour le développement local
