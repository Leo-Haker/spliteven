import { useState, useEffect } from 'react'
import { SessionContext } from './SessionContextValue.js'

function loadFromStorage(key){
    const stored = localStorage.getItem(key)
    return stored ? JSON.parse(stored) : null
}

export function SessionProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(() => loadFromStorage("currentUser"))
  const [currentAccount, setCurrentAccount] = useState(() => loadFromStorage("currentAccount"))

  useEffect(() => {
    if (currentUser) {
        localStorage.setItem("currentUser", JSON.stringify(currentUser))
    } else {
        localStorage.removeItem("currentUser")
    }
  }, [currentUser])

  useEffect(() => {
    if (currentAccount) {
        localStorage.setItem("currentAccount", JSON.stringify(currentAccount))
    } else {
        localStorage.removeItem("currentAccount")
    }
  }, [currentAccount])


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
