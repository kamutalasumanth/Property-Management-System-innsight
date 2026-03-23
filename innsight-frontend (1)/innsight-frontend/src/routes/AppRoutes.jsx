import { useState, useEffect } from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import NavBar from "../components/NavBar";
import Login from "../pages/Login";
import Register from "../pages/Register";
import PropertyList from "../pages/PropertyList";
import PropertyDetails from "../pages/PropertyDetails";
import CreateProperty from "../pages/CreateProperty";
import EditProperty from "../pages/EditProperty";
import Favorites from "../pages/Favorites";
import OwnerOffers from "../pages/OwnerOffers";
import ProtectedRoute from "./ProtectedRoute";
import OwnerRoute from "./OwnerRoute";
import { getUser, clearAuth } from "../utils/auth";

export default function AppRoutes() {
  const [user, setUser] = useState(getUser());
  const authenticated = !!user;
  const role = user?.role;

  const handleLogout = () => {
    clearAuth();
    setUser(null);
    window.location.href = "/login";
  };

  useEffect(() => {
    const syncAuth = () => setUser(getUser());
    window.addEventListener("storage", syncAuth);
    return () => window.removeEventListener("storage", syncAuth);
  }, []);

  return (
    <BrowserRouter>
      {authenticated && <NavBar role={role} onLogout={handleLogout} />}

      <Routes>
        {/* Public routes */}
        <Route path="/" element={<Navigate to="/properties" replace />} />
        <Route path="/login" element={authenticated ? <Navigate to="/properties" replace /> : <Login />} />
        <Route path="/register" element={authenticated ? <Navigate to="/properties" replace /> : <Register />} />

        {/* Protected routes */}
        <Route path="/properties" element={<ProtectedRoute><PropertyList /></ProtectedRoute>} />
        <Route path="/properties/:id" element={<ProtectedRoute><PropertyDetails /></ProtectedRoute>} />
        
        <Route path="/properties/create" element={<ProtectedRoute><OwnerRoute><CreateProperty /></OwnerRoute></ProtectedRoute>} />
        <Route path="/properties/:id/edit" element={<ProtectedRoute><OwnerRoute><EditProperty /></OwnerRoute></ProtectedRoute>} />
        <Route path="/owner/offers" element={<ProtectedRoute><OwnerRoute><OwnerOffers /></OwnerRoute></ProtectedRoute>} />
        
        <Route path="/favorites" element={<ProtectedRoute><Favorites /></ProtectedRoute>} />
      </Routes>
    </BrowserRouter>
  );
}
