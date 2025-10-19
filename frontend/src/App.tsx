import { Navigate, Route, Routes } from 'react-router-dom';
import { Navbar } from './components/Navbar';
import { ProtectedRoute } from './components/ProtectedRoute';
import { CategoriesPage } from './pages/CategoriesPage';
import { LoginPage } from './pages/LoginPage';
import { MapPage } from './pages/MapPage';
import { PlacesPage } from './pages/PlacesPage';
import { RegisterPage } from './pages/RegisterPage';

export const App: React.FC = () => {
  return (
    <div className="min-h-screen bg-slate-100">
      <Navbar />
      <main className="pb-16">
        <Routes>
          <Route path="/" element={<Navigate to="/categories" replace />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          <Route element={<ProtectedRoute />}>
            <Route path="/categories" element={<CategoriesPage />} />
            <Route path="/places" element={<PlacesPage />} />
            <Route path="/map" element={<MapPage />} />
          </Route>

          <Route path="*" element={<Navigate to="/categories" replace />} />
        </Routes>
      </main>
    </div>
  );
};
