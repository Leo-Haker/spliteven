import { useState } from "react"
import { styles } from "../utils/styles.js"
import CreateAccountForm from "./CreateAccountForm"
import { createAccount } from "../utils/api.js"


function SelectAccountStep({ onAccountSelected }) {
    const [showCreateForm, setShowCreateForm] = useState(false)
    const [name, setName] = useState("")

    async function createNewAccount(e) {
        e.preventDefault()
        try{
            const savedAccount = await createAccount(name, currentUser.id)
            onAccountSelected(savedAccount)
        } catch (err) {
            setError(err.message) 
        }
    }

  return (
    <div className={styles.card}>
      <h1 className={styles.cardTitle}>Välj konto</h1>
      <p className={styles.cardSubtitle}>Skapa ett nytt eller gå med i ett befintligt</p>

      {error && <p className="text-red-600 text-sm mb-2">{error}</p>}  

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