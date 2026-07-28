const API_BASE = "http://localhost:8080/api"

export async function createPerson(name, email) {
  const res = await fetch(`${API_BASE}/persons`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, email }),
  })
  if (!res.ok) throw new Error("Kunde inte skapa användare")
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