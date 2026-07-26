import { useState } from "react"
import { useSession } from "../context/SessionContext.jsx"
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
    <div className="bg-white rounded-2xl shadow-md p-8 w-full max-w-sm">
      <h1 className="text-2xl font-semibold text-slate-800 mb-1">Välkommen!</h1>
      <p className="text-slate-500 text-sm mb-6">Välj användare eller skapa en ny</p>

      <nav className="flex flex-col gap-3">
        <button
          onClick={() => setShowCreateForm(!showCreateForm)}
          className="w-full rounded-lg bg-slate-900 text-white py-2.5 font-medium hover:bg-slate-700 transition-colors"
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

        <button className="w-full rounded-lg bg-slate-100 text-slate-800 py-2.5 font-medium hover:bg-slate-200 transition-colors">
          Välj användare
        </button>
      </nav>
    </div>
  )
}

export default SelectUserStep