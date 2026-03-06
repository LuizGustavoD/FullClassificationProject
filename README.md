# Projeto IA Back + Front

Plataforma de classificação de imagens com arquitetura distribuída em três partes principais:

- **Frontend** em React + Vite (interface para upload de imagem e visualização de métricas do modelo).
- **Backend** em Spring Boot (orquestra chamadas entre frontend e serviço de inferência).
- **Serviço de inferência** em Flask + TensorFlow (carrega o modelo `.h5` e executa predição).

## Sumário

- [Visão geral da arquitetura](#visão-geral-da-arquitetura)
- [Estrutura do repositório](#estrutura-do-repositório)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Configuração e execução local](#configuração-e-execução-local)
- [Fluxo da aplicação](#fluxo-da-aplicação)
- [Endpoints](#endpoints)
- [Testes](#testes)
- [Pontos de atenção atuais](#pontos-de-atenção-atuais)
- [Melhorias recomendadas](#melhorias-recomendadas)

## Visão geral da arquitetura

```text
Frontend (React/Vite)
	└─ HTTP -> http://localhost:8080
				 Spring Boot (project)
					 └─ HTTP -> http://localhost:5000
									Flask + TensorFlow (predictConnection)
```

### Responsabilidades por serviço

- **Frontend (`frontend/img-vision-hub`)**
	- Upload da imagem.
	- Exibe classe prevista.
	- Exibe dados do modelo sob demanda (summary, optimizer, metrics etc).

- **Backend Java (`project`)**
	- Endpoint `POST /send` recebe a imagem do frontend.
	- Encaminha a imagem para Flask (`/predict`).
	- Converte índice predito (`0-9`) para rótulo em português (ex.: `0 -> Avião`).
	- Disponibiliza endpoints de metadados em `/model/*`.

- **Inferência Python (`predictConnection`)**
	- Carrega o modelo `neuralNetwork/img_class (1).h5`.
	- Pré-processa imagem para `32x32`, normaliza e executa `model.predict`.
	- Retorna `predicted_class`.

## Estrutura do repositório

```text
ProjetoIABackFront/
├─ frontend/img-vision-hub/      # App React + Vite
├─ project/                      # API Spring Boot
├─ predictConnection/            # API Flask de inferência
├─ neuralNetwork/                # Modelo treinado e notebooks
├─ nginx/                        # Configuração Nginx (proxy/SSL local)
├─ docker/                       # Placeholder (dockerfile vazio)
├─ scripts/                      # Placeholder (startServices.ps1 vazio)
└─ translateConnection/          # Estrutura inicial (ainda sem implementação efetiva)
```

## Tecnologias

### Frontend

- React 18
- TypeScript
- Vite
- Tailwind CSS + shadcn/ui
- Vitest

### Backend Java

- Java 21
- Spring Boot (Web MVC)
- Maven
- Lombok

### Inferência

- Python
- Flask
- TensorFlow / Keras
- Pillow / NumPy

## Pré-requisitos

- **Node.js** 18+ e `npm` (ou Bun, caso prefira)
- **Java** 21
- **Python** 3.10+ (recomendado 3.11)
- **Maven Wrapper** (já incluído em `project/mvnw` e `project/mvnw.cmd`)

## Configuração e execução local

> Ordem recomendada: 1) Flask (`predictConnection`), 2) Spring Boot (`project`), 3) Frontend (`frontend/img-vision-hub`).

### 1) Serviço de inferência (Flask)

```powershell
cd predictConnection
python -m venv .venv
.\.venv\Scripts\activate
pip install -r requirements.txt
python app.py
```

Serviço sobe em: `http://localhost:5000`

### 2) Backend Spring Boot

```powershell
cd project
.\mvnw.cmd spring-boot:run
```

Serviço sobe em: `http://localhost:8080`

### 3) Frontend React

```powershell
cd frontend\img-vision-hub
npm install
npm run dev -- --port 5173
```

Frontend em: `http://localhost:5173`

> O projeto está configurado em `src/services/api.ts` para consumir o backend em `http://localhost:8080`.

## Fluxo da aplicação

1. Usuário envia imagem pelo frontend.
2. Frontend faz `POST /send` no Spring Boot.
3. Spring Boot repassa a imagem para Flask `POST /predict`.
4. Flask retorna `predicted_class` (índice numérico).
5. Spring Boot traduz índice para rótulo legível em português.
6. Frontend exibe o resultado ao usuário.

## Endpoints

### Spring Boot (`http://localhost:8080`)

- `POST /send`
	- Form-data: `image`
	- Retorno: texto com classe prevista em português

- `GET /model/accuracy`
	- Retorno: texto fixo (`Acurácia do modelo: 95%`)

- `GET /model/summary`
- `GET /model/lossFunc`
- `GET /model/metrics`
- `GET /model/optimizer`
- `GET /model/layers`
	- Retorno: string com payload retornado pelo Flask

### Flask (`http://localhost:5000`)

- `POST /predict`
	- Form-data: `image`
	- Retorno: JSON com `predicted_class`

- `GET /summary`
- `GET /lossFunc`
- `GET /metrics`
- `GET /optimizer`
- `GET /layers`
	- Retornos JSON com dados do modelo carregado

## Testes

### Frontend

```powershell
cd frontend\img-vision-hub
npm run test
```

Atualmente há teste de exemplo básico em `src/test/example.test.ts`.

### Backend Java

```powershell
cd project
.\mvnw.cmd test
```

Existe teste de contexto Spring (`ProjectApplicationTests`).

## Pontos de atenção atuais

1. **Conflito de porta**
	 - Vite está configurado para `8080` (`vite.config.ts`), mesma porta do Spring Boot.
	 - Solução prática: iniciar frontend em outra porta (`5173`) via CLI.

2. **Caminho absoluto do modelo no Python**
	 - `predictConnection/routes/predictController.py` e `predictConnection/routes/modelDetails.py` usam caminho absoluto local do Windows.
	 - Isso dificulta execução em outras máquinas.

3. **Arquivos de infraestrutura ainda vazios**
	 - `docker/dockerfile` vazio.
	 - `scripts/startServices.ps1` vazio.

4. **Módulo `translateConnection` incompleto**
	 - Estrutura existe, mas sem rotas/serviço implementados de fato.

5. **Nginx com SSL comentado**
	 - Configuração em `nginx/nginx.conf` contém trechos de certificado comentados.

## Melhorias recomendadas

- Externalizar URLs/portas para variáveis de ambiente.
- Remover caminho absoluto do modelo e usar caminho relativo/configurável.
- Implementar CORS explicitamente no backend Java e Flask.
- Criar `docker-compose.yml` para orquestrar frontend + backend + inferência.
- Preencher `scripts/startServices.ps1` para startup automatizado.
- Evoluir cobertura de testes (integração e e2e).

---
