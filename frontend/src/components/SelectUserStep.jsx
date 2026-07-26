import { useState } from "react"
import { useSession } from "../context/SessionContext.jsx"
import { styles } from "../utils/styles.js"
import CreateUserForm from "./CreateUserForm.jsx"

function SelectUserStep() {
  const { setCurrentUser } = useSession()
  const [showCreateForm, setShowCreateForm] = useState(false)
  const [name, setName] = useState("")
  const [email, setEmail] = useState("")

  function createNewUser(e) {
    e.preventDefault()
    setCurrentUser({ id: null, name, email })
  }

  return (
    <div className={styles.card}>
      <h1 className={styles.cardTitle}>Välkommen!</h1>
      <p className={styles.cardSubtitle}>Välj användare eller skapa en ny</p>

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