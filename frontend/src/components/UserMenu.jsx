import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { useSession } from "../context/SessionContext.jsx"

function UserMenu() {
  const [open, setOpen] = useState(false)
  const { currentUser, currentAccount, switchUser, switchAccount } = useSession()
  const navigate = useNavigate()

  if (!currentUser) return null

  return (
    <div className="relative">
      <button
        onClick={() => setOpen(!open)}
        className="text-sm text-slate-600 hover:text-slate-900 transition-colors"
      >
        {currentUser.name}{currentAccount ? ` · ${currentAccount.name}` : ""} ▾
      </button>

      {open && (
        <div className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-md border border-slate-100 py-1 z-10">
          <button
            onClick={() => { switchAccount(); setOpen(false); navigate("/frontpage") }}
            className="w-full text-left px-4 py-2 text-sm text-slate-600 hover:bg-slate-50"
          >
            Byt konto
          </button>
          <button
            onClick={() => { switchUser(); setOpen(false); navigate("/frontpage") }}
            className="w-full text-left px-4 py-2 text-sm text-slate-600 hover:bg-slate-50"
          >
            Byt användare
          </button>
          <button
            onClick={() => { setOpen(false); navigate("/manage-account") }}
            className="w-full text-left px-4 py-2 text-sm text-slate-600 hover:bg-slate-50"
          >
            Hantera konto
          </button>
        </div>
      )}
    </div>
  )
}

export default UserMenu