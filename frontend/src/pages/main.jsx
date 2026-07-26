import './index.css'
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Frontpage from './Frontpage.jsx'
import Register from './Register.jsx'
import Expense from './Expense.jsx'
import Balance from './Balance.jsx'
import ManageAccount from "./ManageAccount.jsx"
import { SessionProvider } from '../context/SessionContext.jsx'

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <SessionProvider>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Frontpage/>} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/expense" element={<Expense />} />
                    <Route path="/balance" element={<Balance />} />
                    <Route path="/manageAccount" element={<ManageAccount />} />
                </Routes>
            </BrowserRouter>
        </SessionProvider>
    </StrictMode>
)
