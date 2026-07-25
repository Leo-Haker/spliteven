import Navbar from './components/Navbar.jsx'

function Balance() {
  return (
    <div>
        <Navbar/>

   <div className="min-h-screen bg-slate-50 flex items-center justify-center">
      <div className="bg-white rounded-2xl shadow-md p-8 w-full max-w-sm">
        <h1 className="text-2xl font-semibold text-slate-800 mb-1">
          Balance
        </h1>
        <p className="text-slate-500 text-sm mb-6">
          Håll koll på det ni lägger ut för varandra
        </p>

        <nav className="flex flex-col gap-3">
          <button className="w-full rounded-lg bg-slate-900 text-white py-2.5 font-medium hover:bg-slate-700 transition-colors">
            Lägg till utgift
          </button>
          <button className="w-full rounded-lg bg-slate-100 text-slate-800 py-2.5 font-medium hover:bg-slate-200 transition-colors">
            Se utgifter
          </button>
          <button className="w-full rounded-lg bg-slate-100 text-slate-800 py-2.5 font-medium hover:bg-slate-200 transition-colors">
            Balansöversikt
          </button>
        </nav>
      </div>
    </div>
    </div>
 
  )
}

export default Balance