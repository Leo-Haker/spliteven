import '../index.css'
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { ROUTES } from '../utils/routes.js'
import { SessionProvider } from '../context/SessionContext.jsx'
import Frontpage from "./Frontpage.jsx"
import Register from "./Register.jsx"
import Expense from "./Expense.jsx"
import Balance from "./Balance.jsx"
import ManageAccount from './ManageAccount.jsx'
import ManageUser from "./ManageUser.jsx"

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <SessionProvider>
            <BrowserRouter>
                <Routes>
                    <Route path={ROUTES.HOME} element={<Frontpage/>} />
                    <Route path={ROUTES.REGISTER} element={<Register />} />
                    <Route path={ROUTES.EXPENSES} element={<Expense />} />
                    <Route path={ROUTES.BALANCE} element={<Balance />} />
                    <Route path={ROUTES.MANAGE_ACCOUNT} element={<ManageAccount />} />
                    <Route path={ROUTES.MANGAGE_USER} element={<ManageUser/>} />
                </Routes>
            </BrowserRouter>
        </SessionProvider>
    </StrictMode>
)
