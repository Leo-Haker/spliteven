import { NavLink } from "react-router-dom" 
import UserMenu from "./UserMenu.jsx"

function linkStyle({isActive}){
    return isActive
    ? "text-slate-900 font-medium"
    : "text-slate-600 hover:text-slate-900 transition-colors"
}

function Navbar(){
    return (
    <div className="flex flex-col md:flex-row md:items-center gap-3 md:gap-4 bg-white px-6 py-4 shadow-sm">

        <div className="flex items-center justify-between md:flex-1">
            <span className="font-semibold text-slate-800 pr-4 text-2xl">SplitEven</span>
            <div className="md:hidden"><UserMenu /></div>
        </div>

        <nav className="flex flex-wrap justify-center gap-4 md:flex-1 md:gap-6 pt-2 md:pt-0">
            <NavLink to="/" className={linkStyle}> Hem </NavLink>
            <NavLink to="/register" className={linkStyle}> Lägg till utgift </NavLink>
            <NavLink to="/expense" className={linkStyle}> Se utgifter </NavLink>
            <NavLink to="/balance" className={linkStyle}> Balansöversikt </NavLink>
        </nav>

        <div className="hidden md:flex md:flex-1 justify-end">
            <UserMenu />
        </div>

    </div>
    )
}

export default Navbar