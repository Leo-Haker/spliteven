function CreateUserForm({ name, setName, email, setEmail, onSubmit }) {
  return (
    <form onSubmit={onSubmit} className="flex flex-col gap-2 -mt-1">
      <input
        type="text"
        placeholder="Namn"
        value={name}
        onChange={(e) => setName(e.target.value)}
        className="w-full rounded-lg border border-slate-200 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-slate-900"
        required
      />
      <input
        type="email"
        placeholder="E-postadress"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        className="w-full rounded-lg border border-slate-200 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-slate-900"
        required
      />
      <button
        type="submit"
        className="w-full rounded-lg bg-slate-900 text-white py-2 text-sm font-medium hover:bg-slate-700 transition-colors"
      >
        Bekräfta
      </button>
    </form>
  )
}

export default CreateUserForm