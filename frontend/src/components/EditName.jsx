import { useState, useEffect } from "react"

function EditName({ entity, onRenamed,  rename }) {
  const [editing, setEditing] = useState(false)
  const [name, setName] = useState(entity?.name ?? "")
  const [error, setError] = useState(null)

  useEffect(() => {
    setName(entity?.name ?? "")
  }, [entity])

  async function save() {
    try {
      const updated = await rename(entity.id, name)
      onRenamed(updated)
      setEditing(false)
    } catch (err) {
      setError(err.message)
    }
  }

  if (!editing) {
    return (
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-slate-800">{name}</h1>
        <button onClick={() => setEditing(true)} className="text-sm text-slate-500 hover:underline">
          Byt namn
        </button>
      </div>
    )
  }

  return (
    <div className="flex items-center gap-2 mb-6">
      <input
        value={name}
        onChange={(e) => setName(e.target.value)}
        className="rounded-lg border border-slate-200 px-3 py-1.5 text-sm flex-1"
      />
      <button onClick={save} className="text-sm bg-slate-900 text-white px-3 py-1.5 rounded-lg">
        Spara
      </button>
      {error && <p className="text-red-600 text-xs">{error}</p>}
    </div>
  )
}

export default EditName