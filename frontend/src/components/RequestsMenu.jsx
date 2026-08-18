import { useState, useEffect } from "react"
import { useSession } from "../context/SessionContext"
import { getPendingRequests, acceptRequest, declineRequest } from "../utils/api"

function RequestMenu(){
    const { currentUser } = useSession()
    const [open, setOpen] =useState(false)
    const [requests, setRequests] = useState([])

    function loadRequests(){
        if(!currentUser) return
        getPendingRequests(currentUser.id)
            .then(setRequests)
            .catch((err) => console.error("Kunde inte hämta förfrågningar:", err))
    }

    useEffect(() => {
        loadRequests()
    }, [currentUser])

    async function handleAccept(id) {
        try {
        await acceptRequest(id)
        loadRequests()
    } catch (err) {
        console.error("Kunde inte godkänna:", err)
    }
    }

    async function handleDecline(id){
        try {
        await declineRequest(id)
        loadRequests()
    } catch (err) {
        console.error("Kunde inte neka:", err)
    }
    }

    if (!currentUser) return null

    return(
        <div className="relative">
            <button
             onClick={() => setOpen(!open)}
             className="relative text-sm text-slate-600 hover:text-slate-900"
            >
                🔔
                {requests.length > 0 && (
                    <span className="absolute -top-1 -right-1 bg-red-600 text-white text-[10px] rounded w-4 h-4 items-center justify-center">
                        {requests.length}
                    </span>
                )}
            </button>

            {open && (
                <div className="absolute right-0 mt-2 w-64 bg-white rounded-lg shadow-md border border-slate-100 py-2 z-10">
                    {requests.length === 0 ? (
                        <p className="px-4 py-2 text-sm text-slate-400">Inga förfrågningar</p>
                    ) : (
                        requests.map((r) => (
                            <div key={r.id} className="px-4 py-2 border-b border-slate-50 last:border-0">
                                <p className="text-sm text-slate-700 mb-1">
                                    Gå med i <strong>{r.accountName}</strong>?
                                </p>
                                <div className="flex gap-2">
                                    <button
                                        onClick={() => handleAccept(r.id)}
                                        className="text-xs bg-slate-900 text-white px-2 py-1 rounded"
                                    >
                                        Godkänn
                                    </button>
                                    <button
                                        onClick={() => handleDecline(r.id)}
                                        className="text-xs bg-slate-100 text-slate-700 px-2 py-1 rounded"
                                    
                                    >Neka</button>

                                </div>
                            </div>
                        ))
                    )}

                </div>
            )}
        </div>
    )
}

export default RequestMenu