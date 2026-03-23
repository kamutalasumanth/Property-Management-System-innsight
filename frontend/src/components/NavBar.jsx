import "./NavBar.css";
import { NavLink } from "react-router-dom";

export default function NavBar({ role, onLogout }) {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        
        <div className="navbar-brand">
          <span className="brand-text">Innsight<span className="brand-dot">.</span></span>
        </div>

        <div className="navbar-links">
          <NavLink to="/properties" className={({isActive}) => isActive ? "nav-link active-link" : "nav-link"}>
            Properties
          </NavLink>
          
          {role === "OWNER" && (
            <NavLink to="/owner/offers" className={({isActive}) => isActive ? "nav-link active-link" : "nav-link"}>
              Manage Offers
            </NavLink>
          )}

          {role === "CUSTOMER" && (
            <NavLink to="/favorites" className={({isActive}) => isActive ? "nav-link active-link" : "nav-link"}>
              My Favorites
            </NavLink>
          )}
        </div>

        <div className="navbar-actions">
          <span className="role-badge">
            {role}
          </span>
          <button onClick={onLogout} className="logout-btn">
            Sign Out
          </button>
        </div>
        
      </div>
    </nav>
  );
}
