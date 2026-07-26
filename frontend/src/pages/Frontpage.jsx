import { useSession } from "../context/SessionContext.jsx"
import Navbar from "../components/Navbar.jsx"
import SelectUserStep from "../components/SelectUserStep.jsx"
import SelectAccountStep from "../components/SelectAccountStep.jsx"

function Frontpage() {
  const { currentUser, currentAccount, setCurrentAccount } = useSession()

  return (
    <div>
      <Navbar />
      <div className="min-h-screen bg-slate-50 flex items-center justify-center">
        {!currentUser && <SelectUserStep />}
        {currentUser && !currentAccount && (
          <SelectAccountStep onAccountSelected={setCurrentAccount} />
        )}
        {currentUser && currentAccount && (
          <p>Klar! Inloggad som {currentUser.name}, konto: {currentAccount.name}</p>
        )}
      </div>
    </div>
  )
}

export default Frontpage