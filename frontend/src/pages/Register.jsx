import Navbar from '../components/Navbar.jsx'
import Frontpage from './Frontpage.jsx';
import AddExpenseForm from '../components/AddExpenseForm.jsx';
import Table from '../components/Table.jsx';
import { useState, useEffect, useCallback } from 'react';
import { getExpensesForAccount } from '../utils/api.js';
import { useSession } from '../context/useSession.js'

function Register() {
    const { currentUser, currentAccount } = useSession();
    const [error, setError] = useState("")
    const [expenses, setExpenses] = useState([])
    const currentUserId = currentUser?.id
    const currentAccountId = currentAccount?.id

    const loadMyExpenses = useCallback(() => {
        if (!currentUserId || !currentAccountId) return
        getExpensesForAccount(currentAccountId)
            .then((data) => {
                const myExpenses = data.filter((e) => e.paidById === currentUserId)
                setExpenses(myExpenses)
            })
            .catch((err) => setError(err.message))
    }, [currentAccountId, currentUserId])

    useEffect(() => {
        loadMyExpenses()
    }, [loadMyExpenses])

    function handleExpenseAdded() {
        loadMyExpenses()
    }
    

    if (!currentUser || !currentAccount) return <Frontpage/>

  return (

    <div>
        <Navbar/>
        <div className="min-h-screen bg-slate-50 p-8">
            <h1 className="text-2xl font-semibold text-slate-800 mb-4">
                Lägg till utgift - {currentUser.name}
            </h1>

            {error && <p className="text-red-600 text-sm mb-4">{error}</p>}

            <div className="flex flex-col md:flex-row gap-6">
                <div className="w-full md:w-80 bg-white rounded-2xl shadow-md p-6">
                    <AddExpenseForm onAdded={handleExpenseAdded} />
                </div>

                <div className="flex-1 bg-white rounded-2xl shadow-md p-6">
                <Table
                    columns={[
                        { key: "date", header: "Datum"},
                        { key: "description", header: "Beskrivning"},
                        { key: "amount", header: "Belopp", 
                            render: (row) => 
                                row.income ? 
                                (<span className="text-red-600">-{row.amount} kr</span>)
                                :
                                (<span className="text-green-600">+{row.amount} kr</span>)
                        },
                        {
  key: "type",
  header: "Typ",
  render: (row) => (
    row.income
      ? <span className="text-xs bg-red-50 text-red-600 px-2 py-0.5 rounded-full">Inkomst</span>
      : <span className="text-xs bg-green-50 text-green-600 px-2 py-0.5 rounded-full">Utgift</span>
  ),
},
                    ]}
                    data={expenses}
                    emptyMessage="Inga utgifter registrerade än"
                    />
            </div>
        
        </div>
      </div>
    </div>

    
  )
}

export default Register