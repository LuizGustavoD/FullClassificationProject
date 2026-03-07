# Projeto IA Back + Front

Plataforma de IA com arquitetura distribuida e foco em classificacao de imagens, autenticacao de usuarios com confirmacao por e-mail e servicos auxiliares para evolucao futura.

## Sumario

- [Arquitetura Geral](#arquitetura-geral)
- [Mapa do Repositorio](#mapa-do-repositorio)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Funcionalidades Implementadas](#funcionalidades-implementadas)
- [Endpoints Disponiveis](#endpoints-disponiveis)
- [Como Executar](#como-executar)
- [Testes](#testes)
- [Estado Atual e Placeholders](#estado-atual-e-placeholders)
- [Roadmap Futuro](#roadmap-futuro)

## Arquitetura Geral

### Visao macro

```text
Frontend (React + Vite)
  -> HTTP :8080 (Spring Boot API)
      -> HTTP :5000 (Flask Inferencia)
      -> HTTP :5001 (Flask Mail Service)

Opcional/infra:
- Nginx como reverse proxy HTTPS local
- Script PowerShell para subir servicos
- Modulos experimentais de traducao e street preview (scaffold)
```

### Fluxo principal de classificacao

1. Usuario envia imagem no frontend.
2. Frontend chama `POST /send` no backend Java.
3. Backend envia multipart para `predictConnection` (`POST /predict`).
4. Flask/TensorFlow devolve `predicted_class` numerico.
5. Spring converte o indice para classe em portugues (`Avião`, `Gato`, etc.).
6. Frontend exibe resultado e feedback visual.

### Fluxo de autenticacao e confirmacao

1. Usuario registra em `POST /auth/register`.
2. Backend salva usuario no MySQL (senha com BCrypt).
3. Backend gera token de validacao JWT e monta link de confirmacao.
4. Backend aciona `mailService` em `POST /send_mail`.
5. Usuario confirma conta em `GET /auth/register/confirm/{uuid}?token=...`.
6. Login retorna JWT de acesso.

## Mapa do Repositorio

```text
ProjetoIABackFront/
|- frontend/img-vision-hub/   # SPA React (upload, classificacao, dados do modelo)
|- project/                   # API principal Spring Boot (auth + orquestracao IA)
|- predictConnection/         # API Flask de inferencia TensorFlow
|- mailService/               # API Flask para envio de e-mail SMTP
|- neuralNetwork/             # Modelo .h5 e notebooks de treinamento
|- nginx/                     # Reverse proxy HTTPS local
|- scripts/                   # Automacao de subida dos servicos
|- translateConnection/       # Scaffold para traducao (incompleto)
|- streetPreviewConnection/   # Scaffold para street preview (incompleto)
|- docker/                    # Dockerfile ainda vazio
`- features.txt               # Backlog curto do projeto
```

## Tecnologias Utilizadas

### Frontend (`frontend/img-vision-hub`)

- Runtime e base: `React 18`, `TypeScript 5`, `Vite 5`, `React Router DOM`.
- UI e design system: `Tailwind CSS`, `tailwindcss-animate`, `shadcn/ui`, `Radix UI` (accordion, dialog, dropdown, popover, toast, tooltip e outros componentes).
- Estado e formularios: `@tanstack/react-query`, `react-hook-form`, `@hookform/resolvers`, `zod`.
- Visualizacao e utilitarios: `lucide-react`, `recharts`, `sonner`, `clsx`, `class-variance-authority`, `tailwind-merge`, `date-fns`, `embla-carousel-react`, `react-day-picker`, `react-resizable-panels`, `vaul`, `cmdk`, `input-otp`, `next-themes`.
- Qualidade e build tooling: `ESLint 9`, `typescript-eslint`, `@vitejs/plugin-react-swc`, `PostCSS`, `Autoprefixer`.
- Testes frontend: `Vitest`, `@testing-library/react`, `@testing-library/jest-dom`, `jsdom`.

### Backend Java (`project`)

- Plataforma: `Java 21`, `Spring Boot 4.0.3`, `Maven`.
- APIs e web: `spring-boot-starter-webmvc`.
- Dados e persistencia: `spring-boot-starter-data-jpa`, `MySQL Connector/J`, `Flyway`.
- Seguranca: `spring-boot-starter-security`, `spring-boot-starter-security-oauth2-resource-server`, `spring-boot-starter-security-oauth2-client`.
- JWT: `JwtEncoder/JwtDecoder` com chave HMAC (`security.jwt.secret`).
- Produtividade: `Lombok`.
- Testes backend: `spring-boot-starter-data-jpa-test`, `spring-boot-starter-security-test`, `spring-boot-starter-webmvc-test`.

### IA e inferencia (`predictConnection`)

- Linguagem e API: `Python`, `Flask 3.1.3`.
- Machine Learning: `TensorFlow 2.20.0`, `Keras 3.12.1`, `tensorboard`.
- Processamento: `NumPy`, `Pillow`, `h5py`.
- Ecossistema cientifico e suporte do stack TensorFlow: `absl-py`, `protobuf`, `grpcio`, `libclang`, `opt_einsum`, `ml_dtypes`, `wrapt` e demais bibliotecas do `requirements.txt`.

### Servico de e-mail (`mailService`)

- `Python`, `Flask`, `smtplib` (stdlib), `email.mime` (stdlib).
- Configuracao SMTP via variaveis de ambiente (`SMTP_SERVER`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD`).

### Infraestrutura e operacao

- `Nginx` com proxy HTTPS local (certificados comentados no estado atual).
- `PowerShell` (`scripts/startServices.ps1`) para subir `predictConnection`, `mailService`, `project` e `frontend` em janelas separadas.
- Preparacao para containerizacao em `docker/` (ainda incompleta).

## Funcionalidades Implementadas

### Frontend

- Tela de classificacao com upload por clique e drag-and-drop.
- Preview da imagem antes da inferencia.
- Acao de classificar com estado de loading e mensagens de erro.
- Exibicao de resultado com icone contextual por classe prevista.
- Tela de informacoes do modelo com cards acionando endpoints de metadados.
- Roteamento entre paginas `Classificacao` e `Modelo`.

### Backend Spring Boot

- Orquestracao de inferencia: recebe imagem, envia para Flask e traduz classe numerica para nome em portugues.
- Exposicao de endpoints de detalhes de modelo (`summary`, `loss`, `metrics`, `optimizer`, `layers`).
- Cadastro de usuario com validacao de username unico.
- Criptografia de senha com `BCryptPasswordEncoder`.
- Login com autenticacao via `AuthenticationManager` e retorno de JWT.
- Confirmacao de conta por link com token de validacao.
- Persistencia de usuarios em MySQL (`JPA`).

### Servicos Python

- `predictConnection`: carregamento de modelo `.h5`, preprocessamento (`32x32`, normalizacao) e predicao.
- `mailService`: envio de e-mail HTML de confirmacao de cadastro.

### Automacao operacional

- Script `startServices.ps1` com opcoes `-SkipPredict`, `-SkipMail`, `-SkipJava`, `-SkipFrontend`.

## Endpoints Disponiveis

### API Java (`http://localhost:8080`)

- `POST /send`: multipart com `image`, retorna classe prevista em texto.
- `GET /model/accuracy`: retorna acuracia textual.
- `GET /model/summary`: retorna resumo do modelo.
- `GET /model/lossFunc`: retorna funcao de perda.
- `GET /model/metrics`: retorna metricas.
- `GET /model/optimizer`: retorna otimizador.
- `GET /model/layers`: retorna camadas do modelo.
- `POST /auth/register`: registra usuario (`username`, `password`, `email`).
- `GET /auth/register/confirm/{uuid}?token=...`: confirma conta.
- `POST /auth/login/login`: autentica usuario e retorna JWT.

### API de inferencia (`http://localhost:5000`)

- `POST /predict`: recebe imagem e retorna `predicted_class`.
- `GET /summary`: resumo do modelo TensorFlow.
- `GET /lossFunc`: funcao de perda.
- `GET /metrics`: metricas.
- `GET /optimizer`: otimizador.
- `GET /layers`: camadas.

### API de e-mail (`http://localhost:5001`)

- `POST /send_mail`: recebe `recipient` e `confirmation_link`, envia e-mail HTML.

## Como Executar

### Pre-requisitos

- `Node.js` 18+ e `npm`.
- `Java` 21.
- `Python` 3.10+.
- MySQL disponivel com schema `ia_project`.

### Opcao 1: subida manual

1. `predictConnection`

```powershell
cd predictConnection
python -m venv .venv
.\.venv\Scripts\activate
pip install -r requirements.txt
python app.py
```

2. `mailService`

```powershell
cd mailService
python app.py
```

3. `project`

```powershell
cd project
.\mvnw.cmd spring-boot:run
```

4. `frontend/img-vision-hub`

```powershell
cd frontend\img-vision-hub
npm install
npm run dev -- --port 5173
```

### Opcao 2: script unico

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\startServices.ps1
```

Exemplo sem frontend:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\startServices.ps1 -SkipFrontend
```

## Testes

- Frontend: `cd frontend\img-vision-hub && npm run test`
- Backend: `cd project && .\mvnw.cmd test`
- Estado atual: existem testes basicos de exemplo/contexto. Cobertura funcional ainda inicial.

## Estado Atual e Placeholders

- `translateConnection` esta em scaffold inicial.
- `translateConnection/app.py` possui apenas import de `Flask`, sem bootstrap da aplicacao.
- `translateConnection/routes/translateController.py` so declara imports/base de blueprint.
- `streetPreviewConnection/app.py` esta vazio.
- Pastas `streetPreviewConnection/config`, `streetPreviewConnection/model` e `streetPreviewConnection/routes` estao vazias ou quase vazias.
- `docker/dockerfile` esta vazio.
- `nginx/nginx.conf` possui configuracao de SSL comentada.
- Frontend e backend conflitam na porta `8080` por padrao (`vite.config.ts` x Spring Boot), exigindo override no frontend.
- `predictConnection` usa caminho absoluto local para o arquivo do modelo, reduzindo portabilidade.

## Roadmap Futuro

### Curto prazo

- Finalizar os modulos placeholders (`translateConnection`, `streetPreviewConnection`) com rotas, servicos e testes.
- Tornar caminhos/portas configuraveis por `.env` em todos os servicos.
- Ajustar porta padrao do frontend para evitar conflito com Spring Boot.
- Concluir containerizacao (preencher `docker/dockerfile` e adicionar `docker-compose.yml`).
- Ativar SSL local no Nginx com certificados validos.

### Medio prazo

- Consolidar fluxo de autenticacao JWT ponta a ponta no frontend (interceptor, armazenamento seguro, renovacao e expiracao).
- Evoluir modelo de autorizacao por papeis (`ROLE_USER`, `ROLE_ADMIN`) em endpoints protegidos.
- Expandir cobertura de testes (integracao backend, API contract, E2E frontend).
- Endurecer seguranca operacional (segredos em vault, politicas CORS e rate limit).

### Longo prazo

- Concluir ciclo de treinamento e versionamento da rede neural (`features.txt`: terminar treinamento).
- Adicionar observabilidade completa (logs estruturados, traces e metricas por servico).
- Preparar deploy cloud com pipeline CI/CD e ambientes isolados.

## Backlog imediato (`features.txt`)

- Implementacao de login com banco MySQL (status: base implementada no backend, falta consolidacao end-to-end no produto).
- Finalizar treinamento da rede neural.
