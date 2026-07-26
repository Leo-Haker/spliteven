import { createContext, useContext, useState } from 'react'

const SessionContext = createContext(null)

export function SessionProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null)
  const [currentAccount, setCurrentAccount] = useState(null)

  function switchUser() {
    setCurrentUser(null)
    setCurrentAccount(null)  
  }

  function switchAccount() {
    setCurrentAccount(null)
  }

  return (
    <SessionContext.Provider value={{
      currentUser, setCurrentUser,
      currentAccount, setCurrentAccount,
      switchUser, switchAccount,
    }}>
      {children}
    </SessionContext.Provider>
  )
}

export function useSession() {
  return useContext(SessionContext)
}