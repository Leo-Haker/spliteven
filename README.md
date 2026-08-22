# SplitEven

SplitEven is a web application for sharing expenses and tracking balances between members of shared accounts.

## Project structure

- `frontend/` - React and Vite user interface
- `backend/` - Spring Boot REST API and application logic
- `backend/sql/` - Database schema scripts
- `Cheat-Sheets/` - Project learning notes and technology references

## Requirements

- Node.js and npm
- Java 21
- PostgreSQL

## Setup

1. Install the root development tools:

   ```bash
   npm install
   ```

2. Install frontend dependencies:

   ```bash
   cd frontend
   npm install
   cd ..
   ```

3. Create your local environment file from the committed template:

   ```bash
   cp .env.example .env
   ```

   Replace the placeholder mail values in `.env`. The `.env` file is ignored by Git.

4. Create a PostgreSQL database named `spliteven` and apply `backend/sql/schema.sql`.

5. Configure the database settings in `backend/src/main/resources/application.properties`.
   The application uses `ddl-auto=validate`, so the schema must already exist.

6. The mail credentials from `.env` are loaded automatically when using `npm run dev` from the project root. When running the backend directly, export them in the shell first if invitation emails are needed.

## Run the application

Start both the backend and frontend from the project root:

```bash
npm run dev
```

The frontend runs at `http://localhost:5173` and the backend runs at `http://localhost:8080`.

To run either service separately, see the README in its directory.

## Validation

```bash
cd frontend
npm run lint
npm run build

cd ../backend
./mvnw test
```

## Main workflow

Users can create or select a profile, create or select a shared account, add expenses or income, invite members, and review account balances for a selected period.
