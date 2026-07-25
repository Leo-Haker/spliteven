import { NavLink } from "react-router-dom" 

function linkStyle({isActive}){
    return isActive
    ? "text-slate-900 font-medium"
    : "text-slate-600 hover:text-slate-900 transition-colors"
}

function Navbar(){
    return (
    <div className="flex gap-4 bg-white px-6 py-4 shadow-sm">
      <span className="font-semibold text-slate-800 pr-4 text-2xl ">SplitEven</span>

      <nav className="flex gap-6 pt-2">
        <NavLink to="/" className={linkStyle}> Hem </NavLink>
        <NavLink to="/register" className={linkStyle}> Lägg till utgift </NavLink>
        <NavLink to="/expense" className={linkStyle}> Se utgifter </NavLink>
        <NavLink to="/balance" className={linkStyle}> Balansöversikt </NavLink>
      </nav>
    </div>
    )
}

export default Navbar
   