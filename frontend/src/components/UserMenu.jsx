import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { useSession } from "../context/useSession.js"
import { deleteUser } from "../utils/api.js"
import { ROUTES } from "../utils/routes.js"

function UserMenu() {
  const [open, setOpen] = useState(false)
  const [confirmDelete, setConfirmDelete] = useState(false)
  const [deleteError, setDeleteError] = useState(null)
  const { currentUser, currentAccount, switchUser, switchAccount } = useSession()
  const navigate = useNavigate()

  if (!currentUser) return null

  async function handleDeleteUser() {
    try {
        await deleteUser(currentUser.id)
        switchUser()
        setOpen(false)
        navigate(ROUTES.HOME)
    } catch (err) {
        setDeleteError(err.message)
    }
  }

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
            onClick={() => { switchAccount(); setOpen(false); navigate(ROUTES.HOME) }}
            className="w-full text-left px-4 py-2 text-sm text-slate-600 hover:bg-slate-50"
          >
            Byt konto
          </button>
          <button
            onClick={() => { switchUser(); setOpen(false); navigate(ROUTES.HOME) }}
            className="w-full text-left px-4 py-2 text-sm text-slate-600 hover:bg-slate-50"
          >
            Byt användare
          </button>
          <button
            onClick={() => { setOpen(false); navigate(ROUTES.MANAGE_ACCOUNT) }}
            className="w-full text-left px-4 py-2 text-sm text-slate-600 hover:bg-slate-50"
          >
            Hantera konto
          </button>
          <button
            onClick={() => { setOpen(false); navigate(ROUTES.MANGAGE_USER) }}
            className="w-full text-left px-4 py-2 text-sm text-slate-600 hover:bg-slate-50"
          >
            Hantera Användare
          </button>
          <div className="border-t border-slate-100 mt-1 pt-1">
            {!confirmDelete ? (
              <button
                onClick={() => setConfirmDelete(true)}
                className="w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50"
              >
                Ta bort mitt konto
              </button>
            ) : (
              <div className="px-4 py-2">
                <p className="text-xs text-slate-500 mb-2">Är du säker? Detta går inte att ångra.</p>
                {deleteError && <p className="text-xs text-red-600 mb-2">{deleteError}</p>}
                <div className="flex gap-2">
                  <button
                    onClick={handleDeleteUser}
                    className="text-xs bg-red-600 text-white px-2 py-1 rounded"
                  >
                    Ja, ta bort
                  </button>
                  <button
                    onClick={() => { setConfirmDelete(false); setDeleteError(null) }}
                    className="text-xs bg-slate-100 text-slate-700 px-2 py-1 rounded"
                  >
                    Avbryt
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  )
}

export default UserMenu