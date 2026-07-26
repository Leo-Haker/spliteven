import { useState } from "react"
import { styles } from "../utils/styles.js"
import CreateAccountForm from "./CreateAccountForm"


function SelectAccountStep({ onAccountSelected }) {
    const [showCreateForm, setShowCreateForm] = useState(false)
    const [name, setName] = useState("")

    function createNewAccount(e) {
        e.preventDefault()
        onAccountSelected({ id: null, name})
    }

  return (
    <div className={styles.card}>
      <h1 className={styles.cardTitle}>Välj konto</h1>
      <p className={styles.cardSubtitle}>Skapa ett nytt eller gå med i ett befintligt</p>

      <nav className="flex flex-col gap-3">
        <button
          onClick={() => setShowCreateForm(!showCreateForm)}
          className={styles.buttonPrimary}
        >
          Skapa konto
        </button>
        {showCreateForm && (
            <CreateAccountForm
            name={name}
            setName={setName}
            onSubmit={createNewAccount}
            />
        )}

        <button className={styles.buttonSecondary}>
          Gå med i befintligt
        </button>
      </nav>
    </div>
  )
}

export default SelectAccountStep