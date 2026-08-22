import Navbar from "../components/Navbar.jsx"
import Table from "../components/Table.jsx"
import Frontpage from "./Frontpage.jsx"
import { useState, useEffect } from "react"
import { useSession } from "../context/useSession.js"
import { getExpensesForAccount } from "../utils/api.js"

function Expense() {
    const [expenses, setExpenses] = useState([])
    const {currentUser ,currentAccount} = useSession()
    const [error, setError] = useState(null)
    const [filter, setFilter] = useState("all") 
    const visibleExpenses = filter === "mine"
     ? expenses.filter(e => e.paidByName === currentUser.name)
     : expenses
    

    useEffect(() => {
        if (!currentAccount) return

        getExpensesForAccount(currentAccount.id)
            .then(setExpenses)
            .catch((err) => setError(err.message))
    }, [currentAccount])


    if (!currentUser || !currentAccount) return <Frontpage/>

  return (
    <div>
      <Navbar />
      <div className="min-h-screen bg-slate-50 p-8">
        <h1 className="text-2xl font-semibold text-slate-800 mb-4">
          Utgifter{currentAccount ? ` – ${currentAccount.name}` : ""}
        </h1>

        <div className="flex gap-4 mb-4">
          <label className="flex items-center gap-2 text-sm text-slate-600">
            <input
              type="radio"
              name="filter"
              value="all"
              checked={filter === "all"}
              onChange={() => setFilter("all")}
            />
            Alla utgifter
          </label>
          <label className="flex items-center gap-2 text-sm text-slate-600">
            <input
              type="radio"
              name="filter"
              value="mine"
              checked={filter === "mine"}
              onChange={() => setFilter("mine")}
            />
            Bara mina
          </label>
        </div>


        {error && <p className="text-red-600 text-sm mb-2">{error}</p>}

        <div className="bg-white rounded-2xl shadow-md p-6">
          <Table
            columns={[
              { key: "date", header: "Datum" },
              { key: "description", header: "Beskrivning" },
              { key: "paidByName", header: "Betalad av" },
              { key: "amount", header: "Belopp", render: (row) => `${row.amount} kr` },
            ]}
            data={visibleExpenses}
            emptyMessage="Inga utgifter registrerade än"
          />
        </div>
      </div>
    </div>
  )
}

export default Expense