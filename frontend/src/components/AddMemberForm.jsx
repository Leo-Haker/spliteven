import { useState } from "react"
import { addMember } from "../utils/api.js"
import { styles } from "../utils/styles.js"

function AddMemberForm({ accountId, onAdded }) {
  const [personId, setPersonId] = useState("")
  const [error, setError] = useState(null)

  async function handleSubmit(e) {
    e.preventDefault()
    try {
      await addMember(accountId, personId)
      setPersonId("")
      onAdded()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <form onSubmit={handleSubmit} className="flex gap-2">
      <input
        type="number"
        placeholder="Person-ID"
        value={personId}
        onChange={(e) => setPersonId(e.target.value)}
        className={styles.input}
        required
      />
      <button type="submit" className="bg-slate-900 text-white px-4 rounded-lg text-sm">
        Lägg till
      </button>
      {error && <p className="text-red-600 text-xs">{error}</p>}
    </form>
  )
}

export default AddMemberForm