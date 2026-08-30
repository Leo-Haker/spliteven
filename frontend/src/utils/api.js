const API_BASE = "http://localhost:8080/api"

export async function createPerson(name, email, password) {
  const res = await fetch(`${API_BASE}/persons`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, email, password}),
  })
  if (!res.ok) {
  const errorBody = await res.json().catch(() => null)
  throw new Error(errorBody?.message || "Kunde inte skapa användare")
    }
  return res.json()
}

export async function login(email, password) {
    const res = await fetch(`${API_BASE}/auth/login`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({email, password})
    })
    if (!res.ok) throw new Error("Fel e-post eller lösenord")
        return res.json()
}

export async function renameUser(personId, name) {
    const res = await fetch(`${API_BASE}/persons/${personId}`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({personId, name})
    })

    if(!res.ok) throw new Error("Kunde inte byta namn")
    return res.json()
}

export async function deleteUser(id) {
    const res = await fetch(`${API_BASE}/persons/${id}`, {
        method: "DELETE"
    })
    if (!res.ok) {
        const message = await res.text()
        throw new Error(message || "Kunde inte ta bort användaren")
    }
    return res.json()
}



export async function createAccount(name, personId) {
  const res = await fetch(`${API_BASE}/accounts`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, personId }),
  })
  if (!res.ok) throw new Error("Kunde inte skapa konto")
  return res.json()
}

export async function getBalances(personId, from, to){
    const res = await fetch(`${API_BASE}/persons/${personId}/balances?from=${from}&to=${to}`)
    if (!res.ok) throw new Error("Kunde inte hämta konto för balansöversikt")
    return res.json()
}

export async function getAccount(accountId){
    const res = await fetch(`${API_BASE}/accounts/${accountId}`)
    if (!res.ok) throw new Error("Kund inte hämta konto")
    return res.json() 
}

export async function renameAccount(accountId, name) {
    const res = await fetch(`${API_BASE}/accounts/${accountId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({name}),
    })

    if (!res.ok) throw new Error("Kunde inte byta namne")
    return res.json()
}

export async function addMember(accountId, personId){
    const res = await fetch(`${API_BASE}/accounts/${accountId}/members/${personId}`, {
        method: "POST"
    })

    if (!res.ok) throw new Error("Kunde inte lägga till medlem")
    return res.json()
}

export async function removeMember(accountId, personId){
    const res = await fetch(`${API_BASE}/accounts/${accountId}/members/${personId}`,{
        method: "DELETE",
    })
    if (!res.ok) throw new Error("Kunde inte ta bort medlem")
    return res.json()
}

export async function getAccountsForPerson(personId) {
  const res = await fetch(`${API_BASE}/persons/${personId}/accounts`)
  if (!res.ok) throw new Error("Kunde inte hämta konton")
  return res.json()
}

export async function createExpense(accountId, paidById, description, amount, date, income = false) {
  const res = await fetch(`${API_BASE}/expense`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ accountId, paidById, income, description, amount, date }),
  })
  if (!res.ok) throw new Error("Kunde inte spara utgift")
  return res.json()
}

export async function getExpensesForAccount(accountId){
    const res = await fetch(`${API_BASE}/expense/account/${accountId}`)
    if (!res.ok) throw new Error("Kunde inte hämta utgifter")
    return res.json()
} 

export async function getAllPersons(){
    const res = await fetch(`${API_BASE}/persons`)
    if (!res.ok) throw new Error("Kunde inte hämta alla personer")
    return res.json()
}

export async function findPersonByEmail(email) {
    const res = await fetch(`${API_BASE}/persons/by-email?email=${encodeURIComponent(email)}`)
    if (!res.ok) throw new Error("Ingen användare med den e-postadressen")
    return res.json()
}

export async function createMembershipRequest(accountId, email) {
    const res = await fetch(`${API_BASE}/accounts/${accountId}/requests`, {
        method: "POST",
        headers: { "Content-Type": "application/json"},
        body: JSON.stringify({ email }),
    })

    if (!res.ok) throw new Error("Kunde inte skicka förfrågan")
}

export async function getPendingRequests(personId) {
    const res = await fetch(`${API_BASE}/requests/${personId}/requests`)
    if (!res.ok) throw new Error("Kunde inte hämta förfrågningar")
    return res.json()
}

export async function acceptRequest(requestId) {
    const res = await fetch(`${API_BASE}/requests/${requestId}/accept`, { method: "POST"})
    if (!res.ok) throw new Error("kunde inte godkänna förfrågan")
}

export async function declineRequest(requestId) {
    const res = await fetch(`${API_BASE}/requests/${requestId}/decline`, { method: "POST"})
    if (!res.ok) throw new Error("kunde inte neka förfrågan")
}