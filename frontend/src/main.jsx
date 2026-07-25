import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Frontpage from './Frontpage.jsx'
import Register from './Register.jsx'
import Expense from './Expense.jsx'
import Balance from './Balance.jsx'

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Frontpage/>} />
                <Route path="/register" element={<Register />} />
                <Route path="/expense" element={<Expense />} />
                <Route path="/balance" element={<Balance />} />
            </Routes>
        </BrowserRouter>
    </StrictMode>
)
