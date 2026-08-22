# SplitEven frontend

The frontend is a React application built with Vite. It provides the user interface for profiles, shared accounts, expenses, invitations, and balance overviews.

## Structure

- `src/pages/` - page-level views used by the router
- `src/components/` - reusable forms, menus, tables, and navigation
- `src/context/` - shared session state and the `useSession` hook
- `src/utils/api.js` - functions for calling the backend API
- `src/utils/routes.js` - application route definitions
- `src/index.css` - global styles and Tailwind CSS setup
- `public/` - static public assets

## Requirements

- Node.js and npm
- A running SplitEven backend at `http://localhost:8080`

## Install

```bash
npm install
```

## Run in development

```bash
npm run dev
```

The development server runs at `http://localhost:5173`.

## Validate and build

```bash
npm run lint
npm run build
```

Preview the production build locally with:

```bash
npm run preview
```

## Backend API

The API base URL is currently defined in `src/utils/api.js`. Start the backend separately, or run `npm run dev` from the project root to start both services together.
