import { useState } from "react"
import { removeMember } from "../utils/api.js"
import { useSession } from "../context/useSession.js"

function DeleteUserForm({ onSent }) {
  const [error, setError] = useState(null)
  const [success, setSuccess] = useState(false)
  const { currentUser, currentAccount} = useSession()

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setSuccess(false)
    try {
        await removeMember(currentAccount.id, currentUser.id)
        setSuccess(true)
        if (onSent) onSent()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <form onSubmit={handleSubmit} className="flex gap-2">
      <button type="submit" className="bg-slate-900 text-white px-4 rounded-lg text-sm">
        Ta bort användare
      </button>
      {error && <p className="text-red-600 text-xs">{error}</p>}
      {success && <p className="text-green-600 text-xs">Användaren är nu borttagen</p>}
    </form>
  )
}

export default DeleteUserForm