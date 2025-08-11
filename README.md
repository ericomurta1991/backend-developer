# backend-developer
# API de Gerenciamento de Usuários e Documentações

Esta API REST em Java com Spring Boot permite gerenciar usuários e suas documentações associadas, com as seguintes funcionalidades:

- CRUD completo para **Usuários** (`/users`)
- CRUD completo para **Documentações de Usuário** (`/user-documentation`)
- Validações conforme desafio (ex: CPF obrigatório, nome até 50 caracteres, tamanho máximo de arquivo 2MB)
- Paginação via parâmetros `page` e `limit`
- Tratamento padronizado de erros com mensagens claras e códigos HTTP apropriados
- Integração com banco SQL em memória (H2)

---

## Endpoints e Funcionalidades

### Usuários (`/users`)

- `GET /users?page=0&limit=10` — Lista paginada de usuários
- `GET /users/{id}` — Busca usuário por ID
- `POST /users` — Cria novo usuário
- `PUT /users/{id}` — Atualiza usuário existente
- `DELETE /users/{id}` — Remove usuário

### Documentações de Usuário (`/user-documentation`)

- `GET /user-documentation?page=0&limit=10` — Lista paginada de documentações
- `GET /user-documentation/{id}` — Busca documentação por ID
- `POST /user-documentation` — Cria nova documentação (arquivo obrigatório, até 2MB)
- `PUT /user-documentation/{id}` — Atualiza documentação existente
- `DELETE /user-documentation/{id}` — Remove documentação

---

## Acesso à documentação Swagger UI

Acesse a documentação interativa no Swagger para testar a API e ver detalhes dos endpoints

A API roda na porta 8080 por padrão.

Swagger UI: http://localhost:8080/swagger-ui.html

Postman: use a base URL http://localhost:8080

## Exemplos de requisições JSON (Postman)

### Criar Usuário (POST `/users`)

```json
{
  "name": "João Silva",
  "cpf": "123.456.789-00"
}
```

### Criar UserDocumentation (Post `/user-documentation`)
```json 
{
  "type": "CPF",
  "number": "12345678900",
  "userId": 1,
  "file": "(arquivo do documento até 2MB)"
}
```

### Criar Documentação (POST /user-documentation)
Content-Type: multipart/form-data

Campos:

type (string): "CPF", "CNPJ" ou "Outros"

number (string): número do documento

userId (number): ID do usuário associado

file (arquivo): arquivo do documento (máximo 2MB)

Exemplo via Postman: envie um formulário com esses campos.

Atualizar Documentação (PUT /user-documentation/{id})
Content-Type: multipart/form-data

Mesmos campos do POST; file pode ser omitido para manter arquivo anterior.


###Paginação
Todas as listas paginadas aceitam os parâmetros:

page (número da página, default 0)

limit (tamanho da página, default 10)

Exemplo:

GET /users?page=1&limit=5

###Tratamento de Erros
A API retorna erros padronizados com JSON contendo:

timestamp: data e hora do erro

status: código HTTP (ex: 400, 404)

error: descrição do erro (ex: "Bad Request")

message: mensagem clara para o cliente (ex: "CPF é obrigatório")

path: endpoint acessado


Exemplos de erros comuns:

400 Bad Request: dados inválidos, CPF repetido, arquivo > 2MB

404 Not Found: recurso não encontrado

500 Internal Server Error: erro inesperado


###Observações
O CPF deve seguir o formato "XXX.XXX.XXX-XX" e ser único.

O nome do usuário deve ter no máximo 50 caracteres.

Arquivos para documentação não podem exceder 2MB.

Exceções técnicas não são expostas diretamente, apenas mensagens amigáveis.

Rodando a API localmente
Clone o repositório

Rode com Maven ou Gradle (ex: mvn spring-boot:run)

Acesse Swagger UI para testar os endpoints

Use Postman para requisições reais conforme exemplos acima
