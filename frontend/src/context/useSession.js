import { useContext } from 'react'
import { SessionContext } from './SessionContextValue.js'

export function useSession() {
  return useContext(SessionContext)
}
