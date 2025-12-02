 HabitFlow — Gerenciador de Hábitos

HabitFlow é uma aplicação web completa para criação, acompanhamento e conclusão de hábitos diários.
O projeto inclui back-end em Java com SQLite, autenticação via token, painel administrativo e uma interface moderna em estilo dark.

 Tecnologias Utilizadas
Backend

Java 17+

Servlets (Jakarta EE)

SQLite

JDBC

Arquitetura em camadas (Controller, Repository, Model, Security)

Frontend

HTML5

CSS (tema dark)

Alpine.js

LocalStorage para sessão do usuário

 Funcionalidades
 Autenticação

Login com token JWT-like simples (gerado manualmente no servidor)

Proteção de rotas usando AuthMiddleware

Sessão salva no navegador via LocalStorage

 Usuários

Cadastro de novos usuários

Login seguro

Painel de hábitos exclusivo para cada usuário

Marcar e desmarcar hábitos como concluídos

Percentual de progresso automático

🛠️ Administração

Disponível apenas para role ADMIN:

Listagem de todos os usuários

Remoção de usuários

Promoção para ADMIN

Gerenciamento global do sistema

 Hábitos

Criar hábito

Excluir hábito

Marcar como concluído (checkbox interativo)

Contador de progresso exibido no dashboard

 Estrutura do Projeto
src/
 ├── controller/
 │    ├── AuthController.java
 │    ├── DashboardController.java
 │    ├── HabitController.java
 │    └── AdminController.java
 │
 ├── repository/
 │    ├── UserRepository.java
 │    ├── HabitRepository.java
 │    └── Database.java
 │
 ├── security/
 │    ├── AuthMiddleware.java
 │    └── TokenGenerator.java
 │
 ├── model/
 │    ├── User.java
 │    └── Habit.java
 │
 └── util/
      └── DatabaseInitializer.java

 Como Rodar o Projeto
1. Clone o repositório
git clone https://github.com/usuario/habitflow.git
cd habitflow

2. Inicie o servidor

Use Tomcat, Jetty ou outro container Java.

Coloque o projeto em:

/webapps/habitflow


E inicie o servidor.

3. O banco será criado automaticamente

O arquivo:

habitflow.db


é gerado na primeira execução com:

Tabela de usuários

Tabela de hábitos

Criação automática de usuário admin (opcional)

 Usuário Admin padrão (opcional)

Se habilitado no DatabaseInitializer:

username: admin
senha: admin

 Rotas principais
Autenticação
POST /login
POST /register

Dashboard
GET /dashboard

Hábitos
GET /api/habits
POST /api/habits/create
PUT /api/habits/{id}/toggle
DELETE /api/habits/{id}

Admin
GET /admin
PUT /admin/promote/{id}
DELETE /admin/{id}

 Frontend

Interface moderna inspirada no GitHub Dark:

Login estiloso

Dashboard escuro com cards

Progresso de hábitos com porcentagem dinâmica

Botão de logout

Design responsivo

 Screenshots (opcionais)

(Adicionar se quiser posteriormente)

Tela de Login

Dashboard

Painel Admin

 Contribuição

Pull Requests são bem-vindos!

 Licença

Projeto sob licença MIT — livre para uso, modificação e distribuição.