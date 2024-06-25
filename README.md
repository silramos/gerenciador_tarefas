# Projeto Gerenciador de Tarefas

Este é um projeto de exemplo de um sistema de gerenciamento de tarefas desenvolvido em Java 17, utilizando Spring Boot, MongoDB como banco de dados, Redis para cache e integração com Amazon S3 para armazenamento de anexos. Este README fornecerá uma visão geral do projeto, os pré-requisitos para execução, instruções de instalação e execução, além de informações adicionais relevantes.

## Pré-requisitos

Certifique-se de ter instalado em sua máquina:

- **Java Development Kit (JDK) 17**: Necessário para compilar e executar a aplicação.
- **Docker e Docker Compose**: Para execução dos serviços em contêineres.
- **Maven 3.9.8**: Para gerenciamento de dependências e build do projeto. Essa é uma versão recomendada, mas qualquer versão compatível com JDK 17 deve funcionar.


- **` .`**: Contém o `Dockerfile` e o `docker-compose.yml` para configuração dos contêineres Docker e contém o código-fonte da aplicação Spring Boot.
    - **`src/main/java/`**: Código Java da aplicação.
    - **`src/main/resources/`**: Recursos da aplicação, como arquivos de configuração.
    - **`src/test/java/`**: Testes unitários da aplicação.
    - **`pom.xml`**: Arquivo de configuração do Maven para gerenciar dependências e build.
    - **`.dockerignore`**: Arquivo para especificar quais arquivos e diretórios devem ser ignorados durante o build da imagem Docker.

## Configuração do Ambiente

1. **Variáveis**:
Preencher as variáveis de ambiente em `src/main/resources/application.yml`.

2. **Clonar o Repositório**:
   ```bash
   git clone https://github.com/silramos/gerenciador_tarefas.git
   cd gerenciador_tarefas

3. **Executar projeto com o maven**:
   ```bash
   mvn clean spring-boot:run

4. **Ou executar projeto com o docker compose**:
   ```bash
   docker compose up

## Acessando a Aplicação

Uma vez que o Docker Compose ou o Maven tenham iniciado todos os serviços, a aplicação estará disponível no seguinte host e porta:

- **Aplicação Spring Boot**: http://localhost:8089
- **Swagger**: Será realizado redirecionamento automático para o swagger http://localhost:8089/swagger-ui.html

## Autorização

O projeto conta com Spring Security e é necessária autenticação para utilização dos endpoints. Credenciais disponíveis em `src/main/resources/application.yml`

## Testes

Somente estão implementados os testes unitários da classe `src/main/java/desafios/meus/gerenciadortarefas/service/AnexosService.java`. Para execução dos testes unitários (surefire) basta utilizar o comando:
   ```bash
   mvn clean test