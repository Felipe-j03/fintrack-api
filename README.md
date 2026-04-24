# FinTrack API 💰

API REST para controle financeiro pessoal, desenvolvida com Java e Spring Boot.

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.2
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL
- Lombok
- JUnit 5 + Mockito
- Swagger/OpenAPI
- Docker Compose

## ⚙️ Como rodar localmente

### Pré-requisitos
- Java 17+
- PostgreSQL
- Maven

### Passos

```bash
# Clone o repositório
git clone https://github.com/Felipe-j03/fintrack-api.git
cd fintrack-api
```


A API pode ser testada pelo Swagger UI, que estará disponível em `https://fintrack-api-ain0.onrender.com/swagger-ui/index.html`

## 🔐 Autenticação

A API usa JWT. Fluxo:

1. Cadastre um usuário em `POST /api/auth/cadastro`
2. Faça login em `POST /api/auth/login` e copie o token
3. Use o token no header: `Authorization: Bearer {token}`

## 📌 Endpoints

### Auth
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/cadastro` | Cadastro de usuário |
| POST | `/api/auth/login` | Login e geração do token |

### Transações
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/transacoes` | Criar transação |
| GET | `/api/transacoes` | Listar transações |
| GET | `/api/transacoes/saldo` | Consultar saldo |
| GET | `/api/transacoes/resumo` | Resumo por categoria |
| GET | `/api/transacoes/extrato` | Extrato por período |

## 🧪 Testes

```bash
./mvnw test
```

## 📁 Estrutura do projeto

```
src/main/java/com/fintrack/fintrack_api/
├── controller/     → Endpoints HTTP
├── service/        → Regras de negócio
├── repository/     → Acesso ao banco
├── model/          → Entidades JPA
├── dto/            → Objetos de transferência
├── security/       → Filtro JWT
└── exception/      → Tratamento de erros
```