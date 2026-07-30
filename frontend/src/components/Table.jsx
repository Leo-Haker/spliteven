function Table({columns, data, emptyMessage="Ingen data att visa"}) {
    if (data.length === 0) {
        return <p className="text-slate-500 text-sm text-center py-6">{emptyMessage}</p>
    }

    return (
        <table className="w-full text-sm">
            <thead>
                <tr className="border-b border-slate-200">
                    {columns.map((col) => (
                        <th key={col.key} className="text-left py-2 px-3 font-medium text-slate-600">
                            {col.header}
                        </th>
                    ))}
                </tr>
            </thead>
            <tbody>
                {data.map((row, i) => (
  <tr key={row.id ?? i} className="border-b border-slate-100 hover:bg-slate-50">
    {columns.map((col) => (
      <td key={col.key} className="py-2 px-3 text-slate-700">
        {col.render ? col.render(row) : row[col.key]}
      </td>
    ))}
  </tr>
))}
            </tbody>
        </table>
    )
}

export default Table