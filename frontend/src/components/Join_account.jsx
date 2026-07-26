import { Link } from "react-router-dom"
import Navbar from './Navbar.jsx'

function Join_account() {
  return (
    <div>
         
    <Navbar/>

        <div className="min-h-screen bg-slate-50 flex items-center justify-center">
            <div className="bg-white rounded-2xl shadow-md p-8 w-full max-w-sm">
            <h1 className="text-2xl font-semibold text-slate-800 mb-1">
                Välja användare
            </h1>
            <p className="text-slate-500 text-sm mb-6">
                Text med info
            </p>

            <nav className="flex flex-col gap-3">
                <button className="w-full rounded-lg bg-slate-900 text-white py-2.5 font-medium hover:bg-slate-700 transition-colors">
                Skapa konto
                </button>
                <button className="w-full rounded-lg bg-slate-100 text-slate-800 py-2.5 font-medium hover:bg-slate-200 transition-colors">
                Välj befintligt
                </button>
                <button className="w-full rounded-lg bg-slate-100 text-slate-800 py-2.5 font-medium hover:bg-slate-200 transition-colors">
                Användare
                </button>
            </nav>
            </div>
        </div>
    </div>
    
  )
}

export default Join_account