import { useState } from "react"
import { useSession } from "../context/SessionContext.jsx"
import { styles } from "../utils/styles.js"
import { createPerson } from "../utils/api.js"
import CreateUserForm from "./CreateUserForm.jsx"

function SelectUserStep() {
  const { setCurrentUser } = useSession()
  const [showCreateForm, setShowCreateForm] = useState(false)
  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [error, setError] = useState(null)

  async function createNewUser(e) {
    e.preventDefault()
    try {
        const savedPerson = await createPerson(name, email)
        setCurrentUser(savedPerson)
    } catch (err) {
        setError(err.message)
    }
  }

  return (
    <div className={styles.card}>
      <h1 className={styles.cardTitle}>Välkommen!</h1>
      <p className={styles.cardSubtitle}>Välj användare eller skapa en ny</p>

      {error && <p className="text-red-600 text-sm mb-2">{error}</p>}

      <nav className="flex flex-col gap-3">
        <button
          onClick={() => setShowCreateForm(!showCreateForm)}
          className={styles.buttonPrimary}
        >
          Skapa användare
        </button>

        {showCreateForm && (
          <CreateUserForm
            name={name} setName={setName}
            email={email} setEmail={setEmail}
            onSubmit={createNewUser}
          />
        )}

        <button className={styles.buttonSecondary}>
          Välj användare
        </button>
      </nav>
    </div>
  )
}

export default SelectUserStep