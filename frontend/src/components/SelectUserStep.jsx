import { useEffect, useState } from "react"
import { useSession } from "../context/SessionContext.jsx"
import { styles } from "../utils/styles.js"
import { createPerson, login } from "../utils/api.js"
import CreateUserForm from "./CreateUserForm.jsx"
import Login from "./Login.jsx"

function SelectUserStep() {
  const { setCurrentUser } = useSession()
  const [showCreateForm, setShowCreateForm] = useState(false)
  const [showLogin, setShowLogin] = useState(false)
  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [error, setError] = useState(null)

  function resetVariables(){
    setName(""); setEmail(""); setPassword(""); setError(null);
  }

  async function createNewUser(e) {
    e.preventDefault()
    try {
        const savedPerson = await createPerson(name, email, password)
        setCurrentUser(savedPerson)
    } catch (err) {
        setError(err.message)
    }
    resetVariables()
  }

  async function handleLogin(e){
    e.preventDefault()
    try {
        const loggedInPerson = await login(email, password)
        setCurrentUser(loggedInPerson)
    } catch (err) {
        setError(err.message)
    }
     resetVariables()
  }


  return (
    <div className={styles.card}>
      <h1 className={styles.cardTitle}>Välkommen!</h1>
      <p className={styles.cardSubtitle}>Logga in eller skapa en ny användare</p>

      {error && <p className="text-red-600 text-sm mb-2">{error}</p>}

      <nav className="flex flex-col gap-3">
        <button
          onClick={() => {setShowCreateForm(!showCreateForm); setShowLogin(false); resetVariables()}}
          className={showLogin || showCreateForm ? styles.buttonSecondary : styles.buttonPrimary}
        >
          Skapa användare
        </button>

        {showCreateForm && (
          <CreateUserForm
            name={name} setName={setName}
            email={email} setEmail={setEmail}
            password={password} setPassword={setPassword}
            onSubmit={createNewUser}
          />
        )}

        <button 
            onClick={() => { setShowLogin(!showLogin); setShowCreateForm(false); resetVariables()}}
            className={styles.buttonSecondary}>
          Inloggning
        </button>

        {showLogin && (
            <Login
                email={email} setEmail={setEmail}
                password={password} setPassword={setPassword}
                onSubmit={handleLogin}
            />
        )}
      </nav>
    </div>
  )
}

export default SelectUserStep