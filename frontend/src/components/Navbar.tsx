import { Link, NavLink } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const navLinkClass = ({ isActive }: { isActive: boolean }) =>
  `px-3 py-2 rounded-md text-sm font-medium transition-colors ${
    isActive ? 'bg-primary text-white' : 'text-slate-700 hover:bg-slate-200'
  }`;

export const Navbar: React.FC = () => {
  const { isAuthenticated, logout } = useAuth();

  return (
    <header className="bg-white shadow-sm">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-4 py-4">
        <Link to="/" className="text-xl font-semibold text-primary">
          Pat Map
        </Link>
        {isAuthenticated ? (
          <nav className="flex items-center gap-4">
            <NavLink to="/categories" className={navLinkClass}>
              Categories
            </NavLink>
            <NavLink to="/places" className={navLinkClass}>
              Places
            </NavLink>
            <NavLink to="/map" className={navLinkClass}>
              Map
            </NavLink>
            <button
              onClick={logout}
              className="rounded-md bg-slate-900 px-3 py-2 text-sm font-medium text-white shadow hover:bg-slate-700"
            >
              Logout
            </button>
          </nav>
        ) : (
          <nav className="flex items-center gap-4">
            <NavLink to="/login" className={navLinkClass}>
              Login
            </NavLink>
            <NavLink to="/register" className={navLinkClass}>
              Register
            </NavLink>
          </nav>
        )}
      </div>
    </header>
  );
};
