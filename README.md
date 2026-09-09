# HabitFlow — Sistema de Gerenciamento de Hábitos (Java + Javalin + SQLite)

Projeto desenvolvido para a disciplina de Programação Orientada a Objetos (POO).  
O sistema implementa um fluxo completo de autenticação, criação de hábitos e painel administrativo.

---

# Funcionalidades

### Usuário

- Cadastro de usuário
- Login com geração de token
- Primeiro usuário criado vira **admin** automaticamente
- Armazenamento de sessão no `localStorage`
- Dashboard com:
  - Criação de hábitos
  - Listagem de hábitos
  - Toggle (concluir / desfazer)
  - Exclusão de hábito
  - Exibição da porcentagem concluída

### Admin

- Painel administrativo (ao logar como admin digite no final admin.html)
- Listagem de todos os usuários
- Promover usuários para admin
- Excluir usuários
- Listagem global de hábitos do sistema (`/admin/habits`)

### Interface (Frontend)

Inspirada no visual dark do GitHub:

- Login minimalista e responsivo
- Dashboard em dark mode
- Cards de hábito com botões
- Painel admin escuro e moderno

---

# Tecnologias Utilizadas

### Backend

- **Java 21**
- **Javalin 6**
- **Maven**
- **SQLite**
- Arquitetura MVC

### Frontend

- **HTML + CSS**
- **Alpine.js**
- Dark mode customizado

---

# Estrutura de Pastas

src/
└── main/
├── java/br/gov/sp/fatec/pg/oo/
│ ├── controller/
│ │ ├── UserController.java
│ │ ├── HabitController.java
│ │ └── AdminController.java
│ │
│ ├── repository/
│ │ ├── UserRepository.java
│ │ └── HabitRepository.java
│ │
│ ├── security/
│ │ ├── TokenGenerator.java
│ │ └── AuthMiddleware.java
│ │
│ ├── database/
│ │ ├── SQLConnection.java
│ │ └── DatabaseInitializer.java
│ │
│ ├── model/
│ │ ├── User.java
│ │ └── Habit.java
│ │
│ └── Main.java
│
└── resources/static/
├── login.html
├── dashboard.html
├── admin.html
├── test_habitflow.js

---

# Como Rodar o Projeto

## Pré-requisitos

- Java 17+
- Maven
- Navegador (Chrome recomendado)

---

## 2️. Clonar o repositório

git clone https://github.com/fxtbauer/habitflow.git

cd habitflow

---

## 3️. Rodar o servidor Javalin

execute a classe: Main.java

O servidor iniciará em:

http://localhost:7070

---

# Banco de Dados

O arquivo SQLite é criado automaticamente:

habitflow.db

Com as tabelas:

- **users**
- **habits**

Caso queira resetar tudo:

→ Delete o arquivo `habitflow.db`  
→ Rode a aplicação novamente

---

# Rotas Principais

## Autenticação

POST /register
POST /login

## Hábitos

GET /habits
POST /habits
PUT /habits/{id}
DELETE /habits/{id}

## Área Admin

GET /admin/users
GET /admin/habits
PUT /admin/promote/{id}
DELETE /admin/delete/{id}

---

# Testes

O arquivo:

/static/test_habitflow.js

executa os testes completos da API:

- registro
- login
- CRUD hábitos
- CRUD admin
- promover
- excluir

### Como Rodar

1. Abra o navegador
2. Vá para:

http://localhost:7070/login.html

3. Pressione **F12** (Console)
4. Cole o conteúdo do arquivo
5. Aperte **Enter**

Você verá algo assim:

INICIANDO TESTES HABITFLOW
Registro admin -> 201
Login admin -> 200
Criar hábito -> 201
...
TESTES CONCLUÍDOS

---

# Licença

Projeto sob licença **MIT** — livre para uso e modificação.

---

# Desenvolvido por

**Ruan Bauer**
**Natan Sandoval**  
FATEC Praia Grande — 2025  
Disciplina: Programação Orientada a Objetos
