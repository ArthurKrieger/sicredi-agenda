# Sistema de Votação - Cooperativismo

Sistema back-end para gerenciar sessões de votação em assembleias cooperativas.

**Desafio Técnico Sicredi
** - [Ver projeto original no GitHub](https://gist.github.com/virgiliojr94/7882b24932729bed1f0356f38f076abb)

## 🚀 Como Usar

**Pré-requisito:** Docker instalado e rodando

./gradlew bootRun

**Serviços que sobem automaticamente:**

- PostgreSQL (porta 5432)
- LocalStack AWS (porta 4566)
- WireMock para validação de CPF (porta 8089)

## 📡 API

**Base URL:** /api/v1/agendas

### Criar Pauta

POST /api/v1/agendas
{
"description": "Nova pauta"
}

### Abrir Sessão

POST /api/v1/agendas/{agenda-id}/sessions?duration=PT5M

### Votar

POST /api/v1/agendas/{agenda-id}/sessions/{session-id}/votes
{
"cpf": "12345678900",
"vote": "SIM"
}

### Consultar Pauta

GET /api/v1/agendas/{agenda-id}

## ✅ Tarefas Bônus Implementadas

**Integração com CPF** - WireMock simula a API externa de validação de associados

**Mensageria** - AWS SNS/SQS via LocalStack para publicação de resultados

**Performance** - *A ser implementado*

**Versionamento** - API versionada por URL (v1, v2)

## 🛠 Stack

- Java 21 + Spring Boot 3.5.6
- PostgreSQL
- AWS SNS + SQS (LocalStack)
- WireMock (validação CPF)

## 🧪 Testes

./gradlew test

---

Sistema desenvolvido para o desafio técnico da Sicredi, gerenciando sessões de votação em assembleias cooperativas com
controle de pautas, sessões temporizadas e apuração automática de resultados.