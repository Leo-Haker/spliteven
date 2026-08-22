import { useState } from "react"
import { useSession } from "../context/useSession.js"
import {  createExpense } from "../utils/api.js"
import { styles } from "../utils/styles.js"

function AddExpenseForm({ onAdded }) {
  const { currentAccount, currentUser } = useSession()
  const [description, setDescription] = useState("")
  const [amount, setAmount] = useState("")
  const [date, setDate] = useState(new Date().toISOString().slice(0, 10))
  const [error, setError] = useState(null)
  const [filter, setFilter] = useState("expense")
  const income = filter === "income"

  
  async function handleSubmit(e) {
    e.preventDefault()
    try {
      await createExpense(currentAccount.id, currentUser.id, description, Number(amount), date, income)
      setDescription("")
      setAmount("")
      if (onAdded) onAdded()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-2">
      {error && <p className="text-red-600 text-sm">{error}</p>}

      <div className={`${styles.input} bg-slate-50`}>
        <span className="text-slate-500">Konto</span>
        <span className="ml-2 font-medium text-slate-800">
            {currentAccount?.name}
        </span>
      </div>

      <div className="flex px-3 gap-4 mb-4">
          <label className="flex items-center gap-2 text-sm text-slate-600">
            <input
              type="radio"
              name="filter"
              value="expense"
              checked={filter === "expense"}
              onChange={() => setFilter("expense")}
            />
            Utgift
          </label>
          <label className="flex items-center gap-2 text-sm text-slate-600">
            <input
              type="radio"
              name="filter"
              value="income"
              checked={filter === "income"}
              onChange={() => setFilter("income")}
            />
            Inkomst
          </label>
        </div>

     <input
        type="number"
        step="0.01"
        placeholder="Belopp"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
        className={styles.input}
        required
      />

      <input
        type="text"
        placeholder="Beskrivning"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        className={styles.input}
        required
      />


      <input
        type="date"
        value={date}
        onChange={(e) => setDate(e.target.value)}
        className={styles.input}
        required
      />

      <button type="submit" className={styles.buttonPrimary}>
        Lägg till utgift
      </button>
    </form>
  )
}

export default AddExpenseForm