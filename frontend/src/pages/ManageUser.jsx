import { useState, useEffect } from "react"
import Navbar from "../components/Navbar.jsx"
import Table from "../components/Table.jsx"
import EditName from "../components/EditName.jsx"
import DeleteUserForm from "../components/DeleteUserForm.jsx"
import SelectAccountStep from "../components/SelectAccountStep.jsx"
import SelectUserStep from "../components/SelectUserStep.jsx"
import { useSession } from "../context/SessionContext.jsx"
import { getAccountsForPerson, removeMember, renameUser} from "../utils/api.js"
import  { styles } from "../utils/styles.js"

function ManageUser() {
  const { currentUser, currentAccount, setCurrentUser } = useSession()
  const [accounts, setAccounts] = useState([])
  const [error, setError] = useState(null)

  useEffect(() => {
    if (!currentUser) return
    refreshAccounts()
}, [currentUser?.id])

  function refreshAccounts() {
    getAccountsForPerson(currentUser.id)
        .then(setAccounts)
        .catch((err) => setError(err.message))
  }

  async function handleRemove(accountId) {
    try {
      await removeMember(accountId, currentUser.id)
      refreshAccounts()
    } catch (err) {
      setError(err.message)
    }
  }

  //If the user deletes itself, get back att Select user.
  if (!currentUser) {
    return  (
      <div>
        <Navbar />
        <div className={styles.buttonMenuCenter}>
            <SelectUserStep/>
        </div>
        
      </div>
    )
  }

  //If the user deletes the current account, get to select account
  if (!currentAccount) {
    return (
      <div>
        <Navbar />
        <div className={styles.buttonMenuCenter}>
            <SelectAccountStep/>
        </div>
        
      </div>
    )
  }

  return (

    <div>
          <Navbar />
      <div className={styles.buttonMenuCenter}>
        <div className="bg-white rounded-2xl shadow-md p-8 w-full max-w-lg">
          <EditName entity={currentUser} onRenamed={setCurrentUser} rename={renameUser} />

          {error && <p className="text-red-600 text-sm my-2">{error}</p>}

          <Table
            columns={[
              { key: "name", header: "Namn" },
              {
                key: "actions",
                header: "",
                render: (row) => (
                  <button
                    onClick={() => handleRemove(row.id)}
                    className="text-red-600 text-sm hover:underline"
                  >
                    Ta bort
                  </button>
                ),
              },
            ]}
            data={accounts}
            emptyMessage="Inga konton än"
          />
        </div>
      </div>
    </div>
  )
}

export default ManageUser