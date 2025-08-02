<img width="823" height="478" alt="image" src="https://github.com/user-attachments/assets/3460e8b8-760b-497e-9a97-95d66e4560a8" />

docker run --name postgres-container --network my-app-network -e POSTGRES_USER=testuser -e POSTGRES_PASSWORD=password -e POSTGRES_DB=testdb -p 5432:5432 -d postgres:latest

docker run -d -e "SPRING_PROFILES_ACTIVE=docker" --name naming-registry-container --network my-app-network -p 8761:8761 jherzog89/naming-registry-service:v1

docker run -d -e "SPRING_PROFILES_ACTIVE=docker" --name authentication-container --network my-app-network -p 8777:8777 jherzog89/authentication-service:v1

docker run -d -e "SPRING_PROFILES_ACTIVE=docker" --name api-gateway-container --network my-app-network -p 8765:8765 jherzog89/api-gateway-service:v1

docker run -d -e "SPRING_PROFILES_ACTIVE=docker" --name reverse-string-container --network my-app-network -p 8080:8080 jherzog89/reverse-string-service:v1

docker run -d -e "SPRING_PROFILES_ACTIVE=docker" --name angular-app-container --network my-app-network -p 4200:80 jherzog89/angular-app:v1

docker run -d -e "SPRING_PROFILES_ACTIVE=docker" --name reverse-words-container --network my-app-network -p 8090:8090 jherzog89/reverse-words-service:v1

