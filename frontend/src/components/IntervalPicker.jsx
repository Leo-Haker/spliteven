function IntervalPicker({ from, to, onFromChange, onToChange }) {
  return (
    <div className="flex gap-3 items-center">
      <label className="text-sm text-slate-600">
        Från
        <input
          type="month"
          value={from}
          onChange={(e) => onFromChange(e.target.value)}
          className="ml-2 rounded-lg border border-slate-200 px-2 py-1 text-sm"
        />
      </label>
      <label className="text-sm text-slate-600">
        Till
        <input
          type="month"
          value={to}
          onChange={(e) => onToChange(e.target.value)}
          className="ml-2 rounded-lg border border-slate-200 px-2 py-1 text-sm"
        />
      </label>
    </div>
  )
}

export default IntervalPicker