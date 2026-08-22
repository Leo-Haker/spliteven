import { useState, useEffect, useCallback } from "react"
import Navbar from "../components/Navbar.jsx"
import Table from "../components/Table.jsx"
import EditableAccountName from "../components/EditName.jsx"
import AddMemberForm from "../components/AddMemberForm.jsx"
import SelectAccountStep from "../components/SelectAccountStep.jsx.jsx"
import { useSession } from "../context/useSession.js"
import { getAccount, removeMember, renameAccount } from "../utils/api.js"
import  { styles } from "../utils/styles.js"

function ManageAccountPage() {
  const { currentAccount, setCurrentAccount } = useSession()
  const [members, setMembers] = useState([])
  const [error, setError] = useState(null)
  const currentAccountId = currentAccount?.id

  const refreshAccount = useCallback(() => {
    if (!currentAccountId) return
    getAccount(currentAccountId)
      .then((account) => {
        setMembers(account.members)
        setCurrentAccount(account)
      })
      .catch((err) => setError(err.message))
  }, [currentAccountId, setCurrentAccount])

  useEffect(() => {
    refreshAccount()
  }, [refreshAccount])

  async function handleRemove(personId) {
    try {
      await removeMember(currentAccount.id, personId)
      refreshAccount()
    } catch (err) {
      setError(err.message)
    }
  }

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
          <EditableAccountName key={currentAccount.id} entity={currentAccount} onRenamed={setCurrentAccount} rename={renameAccount} />

          {error && <p className="text-red-600 text-sm my-2">{error}</p>}

          <Table
            columns={[
              { key: "name", header: "Namn" },
              { key: "email", header: "E-post" },
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
            data={members}
            emptyMessage="Inga medlemmar än"
          />

          <div className="mt-4">
            <AddMemberForm accountId={currentAccount.id} onSent={refreshAccount} />
          </div>
        </div>
      </div>
    </div>
  )
}

export default ManageAccountPage