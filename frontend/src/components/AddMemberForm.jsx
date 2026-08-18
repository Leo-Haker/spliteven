import { useState } from "react"
import { createMembershipRequest } from "../utils/api.js"
import { styles } from "../utils/styles.js"

function AddMemberForm({ accountId, onSent }) {
  const [email, setEmail] = useState("")
  const [error, setError] = useState(null)
  const [success, setSuccess] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setSuccess(false)
    try {
        await createMembershipRequest(accountId, email)
        setEmail("")
        setSuccess(true)
        if (onSent) onSent()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <form onSubmit={handleSubmit} className="flex gap-2">
      <input
        type="email"
        placeholder="E-postadress"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        className={styles.input}
        required
      />
      <button type="submit" className="bg-slate-900 text-white px-4 rounded-lg text-sm">
        Skicka förfrågan
      </button>
      {error && <p className="text-red-600 text-xs">{error}</p>}
      {success && <p className="text-green-600 text-xs">Förfrågan skickad!</p>}
    </form>
  )
}

export default AddMemberForm