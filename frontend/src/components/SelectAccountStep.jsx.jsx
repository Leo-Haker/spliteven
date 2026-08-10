import { useEffect, useState } from "react"
import { styles } from "../utils/styles.js"
import CreateAccountForm from "./CreateAccountForm"
import { createAccount, getAccountsForPerson } from "../utils/api.js"
import { useSession } from "../context/SessionContext.jsx"


function SelectAccountStep({ onAccountSelected }) {
    const { currentUser } = useSession()
    const [showCreateForm, setShowCreateForm] = useState(false)
    const [showJoinForm, setShowJoinForn] = useState(false)
    const [name, setName] = useState("")
    const [error, setError] = useState(null) 

    const [existingAccounts, setExistingAccounts] = useState([])
    const [selectedAccountId, setSelectedAccountId] = useState("")

    useEffect(() => {
        if (!showJoinForm || !currentUser) return

        getAccountsForPerson(currentUser.id)
            .then(setExistingAccounts)
            .catch((err) => setError(err.message))
    }, [showJoinForm, currentUser])

    async function createNewAccount(e) {
        e.preventDefault()
        try{
            const savedAccount = await createAccount(name, currentUser.id)
            onAccountSelected(savedAccount)
        } catch (err) {
            setError(err.message) 
        }
    }

    function selectExistingAccount(e) {
        e.preventDefault()
        const account = existingAccounts.find((a) => a.id === Number(selectedAccountId))
        if (account) {
            onAccountSelected(account)
        }
    }

  return (
    <div className={styles.card}>
      <h1 className={styles.cardTitle}>Välj konto</h1>
      <p className={styles.cardSubtitle}>Skapa ett nytt eller gå med i ett befintligt</p>

      {error && <p className="text-red-600 text-sm mb-2">{error}</p>}  

      <nav className="flex flex-col gap-3">
        <button
          onClick={() => {setShowCreateForm(!showCreateForm); setShowJoinForn(false)}}
          className={showCreateForm || showJoinForm ?  styles.buttonSecondary : styles.buttonPrimary}
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

        <button 
            onClick={() => { setShowJoinForn(!showJoinForm); setShowCreateForm(false)}}
            className={styles.buttonSecondary}>
          Välj befintligt
        </button>

        {showJoinForm && (
            existingAccounts.length === 0 ? (
                <p className={styles.cardSubtitle}>Inga konton att välja mellan</p>
            ) : (
                <form onSubmit={selectExistingAccount} className="flex flex-col gap-2 -mt-1">
                    <select
                        value={selectedAccountId}
                        onChange={(e) => setSelectedAccountId(e.target.value)}
                        className={styles.input}
                        required
                    >
                        <option value="" disabled>Välj konto</option>
                        {existingAccounts.map((a) => (
                            <option key={a.id} value={a.id}>{a.name}</option>
                        ))}

                    </select>
                    <button type="submit" className={styles.buttonPrimary}>
                        Bekräfta
                    </button>
                </form>
            )
        )}


      </nav>
    </div>
  )
}

export default SelectAccountStep