import { useState } from "react"
import CreateAccountForm from "./CreateAccountForm"


function SelectAccountStep({ onAccountSelected }) {
    const [showCreateForm, setShowCreateForm] = useState(false)
    const [name, setName] = useState("")

    function createNewAccount(e) {
        e.preventDefault()
        onAccountSelected({ id: null, name})
    }

  return (
    <div className="bg-white rounded-2xl shadow-md p-8 w-full max-w-sm">
      <h1 className="text-2xl font-semibold text-slate-800 mb-1">Välj konto</h1>
      <p className="text-slate-500 text-sm mb-6">Skapa ett nytt eller gå med i ett befintligt</p>

      <nav className="flex flex-col gap-3">
        <button
          onClick={() => onAccountSelected({ id: null, name: "Nytt konto" })}
          className="w-full rounded-lg bg-slate-900 text-white py-2.5 font-medium hover:bg-slate-700 transition-colors"
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

        <button className="w-full rounded-lg bg-slate-100 text-slate-800 py-2.5 font-medium hover:bg-slate-200 transition-colors">
          Gå med i befintligt
        </button>
      </nav>
    </div>
  )
}

export default SelectAccountStep