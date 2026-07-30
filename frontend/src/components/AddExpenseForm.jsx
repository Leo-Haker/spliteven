import { useState, useEffect } from "react"
import { useSession } from "../context/SessionContext.jsx"
import { getAccountsForPerson, createExpense } from "../utils/api.js"
import { styles } from "../utils/styles.js"

function AddExpenseForm({ onAdded }) {
  const { currentAccount, currentUser } = useSession()
  const [accountId, setAccountId] = useState(currentAccount?.id ?? "")
  const [accounts, setAccounts] = useState([])
  const [description, setDescription] = useState("")
  const [amount, setAmount] = useState("")
  const [date, setDate] = useState(new Date().toISOString().slice(0, 10))
  const [error, setError] = useState(null)

  useEffect(() => {
    if (!currentUser) return
    getAccountsForPerson(currentUser.id)
      .then(setAccounts)
      .catch((err) => setError(err.message))
  }, [currentUser])

  async function handleSubmit(e) {
    e.preventDefault()
    try {
      await createExpense(accountId, currentUser.id, description, Number(amount), date)
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

      <select
        value={accountId}
        onChange={(e) => setAccountId(e.target.value)}
        className={styles.input}
        required
      >
        <option value="" disabled>Välj konto</option>
        {accounts.map((a) => (
          <option key={a.id} value={a.id}>{a.name}</option>
        ))}
      </select>

      <input
        type="text"
        placeholder="Beskrivning"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        className={styles.input}
        required
      />

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