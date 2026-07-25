import { Link } from "react-router-dom"

function Frontpage() {
  return (
    <div>
         <div className="flex gap-4 bg-white px-6 py-4 shadow-sm">
      <span className="font-semibold text-slate-800 pr-4 text-2xl ">SplitEven</span>

      <nav className="flex gap-6 pt-2">
        <Link to="/" className="text-slate-600 hover:text-slate-900 transition-colors">
          Hem
        </Link>
        <Link to="/register" className="text-slate-600 hover:text-slate-900 transition-colors">
          Lägg till utgift
        </Link>
        <Link to="/expenses" className="text-slate-600 hover:text-slate-900 transition-colors">
          Se utgifter
        </Link>
        <Link to="/balance" className="text-slate-600 hover:text-slate-900 transition-colors">
          Balansöversikt
        </Link>
      </nav>
    </div>

        <div className="min-h-screen bg-slate-50 flex items-center justify-center">
            <div className="bg-white rounded-2xl shadow-md p-8 w-full max-w-sm">
            <h1 className="text-2xl font-semibold text-slate-800 mb-1">
                Välj konto
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

export default Frontpage