import { useState, useEffect } from "react"
import Navbar from "../components/Navbar.jsx"
import Table from "../components/Table.jsx"
import IntervalPicker from "../components/IntervalPicker.jsx"
import Frontpage from "./Frontpage.jsx"
import { useSession } from "../context/useSession.js"
import { getBalances } from "../utils/api.js"

function currentYearMonth(){
    const now = new Date()
    return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, "0")}`
}

function Balance() {
    const { currentUser, currentAccount } = useSession()
    const [balances, setBalances] = useState([])
    const [from, setFrom] = useState(currentYearMonth())
    const [to, setTo] = useState(currentYearMonth())
    const [error, setError] = useState(null)

    useEffect(() => {
        if (!currentUser) return

        getBalances(currentUser.id, from, to)
            .then(setBalances)
            .catch((err) => setError(err.message))
    }, [currentUser, from, to])


    if (!currentUser || !currentAccount) return <Frontpage/>

  return (
    <div>
      <Navbar />
      <div className="min-h-screen bg-slate-50 p-8">
        <h1 className="text-2xl font-semibold text-slate-800 mb-4">Balansöversikt</h1>

        <IntervalPicker from={from} to={to} onFromChange={setFrom} onToChange={setTo} />

        {error && <p className="text-red-600 text-sm my-2">{error}</p>}

        <div className="bg-white rounded-2xl shadow-md p-6 mt-4">
          <Table
            columns={[
              { key: "accountName", header: "Konto" },
              {
                key: "myBalance",
                header: "Saldo",
                render: (row) =>
                  row.myBalance >= 0 ? (
                    <span className="text-green-600">+{row.myBalance} kr</span>
                  ) : (
                    <span className="text-red-600">{row.myBalance} kr</span>
                  ),
              },
            ]}
            data={balances}
            emptyMessage="Inga konton att visa saldo för"
          />
        </div>
      </div>
    </div>
  )
}

export default Balance