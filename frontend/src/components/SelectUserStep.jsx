import { useEffect, useState } from "react"
import { useSession } from "../context/SessionContext.jsx"
import { styles } from "../utils/styles.js"
import { createPerson, getAllPersons } from "../utils/api.js"
import CreateUserForm from "./CreateUserForm.jsx"
import SelectDropdown from "./SelectDropdown.jsx"

function SelectUserStep() {
  const { setCurrentUser } = useSession()
  const [showCreateForm, setShowCreateForm] = useState(false)
  const [showJoinForm, setShowJoinForn] = useState(false)
  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [error, setError] = useState(null)

  const [existingUsers, setExistingUsers] = useState([])
    const [selectedUserId, setSelectedUserId] = useState("")

  useEffect(() => {
    if (!showJoinForm) return

    getAllPersons()
        .then(setExistingUsers)
        .catch((err) => setError(err.message))
  }, [showJoinForm, setExistingUsers])

  async function createNewUser(e) {
    e.preventDefault()
    try {
        const savedPerson = await createPerson(name, email)
        setCurrentUser(savedPerson)
    } catch (err) {
        setError(err.message)
    }
  }

  function selectExistingUser(e){
    e.preventDefault()
    const user =  existingUsers.find((user) => user.id === Number(selectedUserId))

    if (user) {
        setCurrentUser(user)
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
          className={showCreateForm ? styles.buttonSecondary : styles.buttonPrimary}
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

        <button 
            onClick={() => { setShowJoinForn(!showJoinForm); setShowCreateForm(false)}}
            className={styles.buttonSecondary}>
          Välj användare
        </button>

        {showJoinForm && (
            existingUsers.length === 0 ? (
                <p className={styles.cardSubtitle}>Inga användare att välja mellan</p>
            ) : (
                <form onSubmit={selectExistingUser} className="flex flex-col gap-2 -mt-1">
                    <SelectDropdown
                        items={existingUsers}
                        value={selectedUserId}
                        onChange={setSelectedUserId}
                        placeholder="Välj användare"
                        getLabel={(user) => user.name}
                        getValue={(user) => user.id}
                    />
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

export default SelectUserStep