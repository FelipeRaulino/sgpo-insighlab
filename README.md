# SGPO - Insight Lab

## O que se trata

O Sistema de Gestão de Processos de uma Organização é uma aplicação desenvolvida como parte do desafio para desenvolvedor Full-Stack proposto pela [Insight Lab](https://www.insightlab.ufc.br/). Este sistema oferece uma plataforma capaz de gerenciar o ciclo de vida dos fornecedores de uma organização. A aplicação pode ser acessada em: [SGPO | Insight Lab]().

> Observação: A API foi implantada no Render.com utilizando o plano gratuito. Devido a isso, as requisições podem apresentar um tempo de resposta maior do que o desejado. Infelizmente, eu recomendo um pouco de paciência ao realizar operações na versão disponível na web, pois o tempo de processamento pode variar. A documentação da API pode ser acessada em: [Swagger](https://sgpo-backend-latest.onrender.com/swagger-ui/index.html)

## Principais recursos

- **Cadastro de Fornecedores**: Os usuários podem adicionar novos fornecedores ao sistema, inserindo detalhes como nome, CNPJ/CPF, telefone, e-mail, entre outros dados relevantes.
- **Visualização e Edição de Fornecedores**: O sistema permite visualizar uma lista completa de todos os fornecedores cadastrados, além de oferecer a funcionalidade de editar as informações de cada fornecedor necessário.
- **Exclusão de Fornecedores**: Os usuários têm a capacidade de excluir fornecedores do sistema, garantindo uma gestão eficiente da base de dados e a remoção de fornecedores não mais relevantes para a organização.
- **Autenticação e Autorização**: A aplicação oferece recursos avançados de autenticação e autorização, garantindo que apenas usuários autorizados tenham acesso às funcionalidades de gerenciamento de fornecedores.

## Destaques

### Backend

- **CRUD de Usuários e Fornecedores**: Funcionalidades completas de criação, leitura, atualização e exclusão para usuários e fornecedores.
- **Autenticação e Proteção de Rotas**: Implementação de autenticação e autorização utilizando JWT, garantindo a segurança das rotas específicas.
- **Documentação da API**: A API é documentada utilizando Swagger, facilitando a compreensão e uso por outros desenvolvedores.
- **Versionamento de Banco de Dados**: Utilização do Flyway para versionamento e migração do banco de dados, assegurando a consistência e controle de alterações.
- **Testes Unitários**: Cobertura de testes unitários com JUnit e Mockito para garantir a qualidade e funcionalidade do código.
- **Testes de Integração**: Testes de integração de todas as rotas da API utilizando JUnit, TestContainers e RestAssured, garantindo a robustez do sistema.

### Frontend

- **Uso de Ant Design**: Utilização da biblioteca de componentes Ant Design para uma interface de usuário moderna e responsiva.
- **Gerenciamento de Estado**: Implementação de gerenciamento de estado utilizando o hook useContext do React, garantindo uma experiência de usuário consistente.
- **Telas Adaptáveis às Funções dos Usuários**: As interfaces são adaptáveis às diferentes funções dos usuários, fornecendo a cada tipo de usuário acesso às funcionalidades apropriadas.

## Como executar

Instruções para configurar e executar a aplicação localmente:

### Pré-requisitos

- Node.js
- Docker
- Java 11+

### Backend

1. Navegue até o diretório do backend:

   ```bash
   cd backend
   ```

2. Configure o arquivo `.env` com as variáveis de ambiente necessárias.

3. Execute o Docker Compose para iniciar os serviços do banco de dados e da aplicação:
   ```bash
   docker-compose up --build
   ```

### Frontend

1. Navegue até o diretório do frontend:

   ```bash
   cd frontend
   ```

2. Configure o arquivo `.env` com as variáveis de ambiente necessárias.

3. Instale as dependências do projeto:

   ```bash
   npm install
   ```

4. Inicie a aplicação:
   ```bash
   npm run dev
   ```

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir uma issue ou enviar um pull request.

## Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.
