import Navbar from "../components/Navbar.jsx"
import { useSession } from "../context/SessionContext.jsx"

function ManageAccountPage() {
  const { currentAccount } = useSession()

  return (
    <div>
      <Navbar />
      <div className="min-h-screen bg-slate-50 flex items-center justify-center">
        <div className="bg-white rounded-2xl shadow-md p-8 w-full max-w-md">
          <h1 className="text-2xl font-semibold text-slate-800 mb-1">
            {currentAccount ? currentAccount.name : "Inget konto valt"}
          </h1>
          <p className="text-slate-500 text-sm mb-6">Medlemmar i kontot</p>
          {/* TODO: lista medlemmar, formulär för att lägga till fler */}
        </div>
      </div>
    </div>
  )
}

export default ManageAccountPage