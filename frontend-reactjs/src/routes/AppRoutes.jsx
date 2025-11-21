import { Routes, Route } from "react-router-dom";
import LandingPage from "../pages/LandingPage";
import AuthPage from "../pages/AuthPage";
import ClientDashboard from "../pages/ClientDashboard";
import FreelancerDashboard from "../pages/FreelancerDashboard";
import ProtectedRoute from "../components/auth/ProtectedRoute";

const AppRoutes = () => {
  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/" element={<LandingPage />} />

      <Route path="/login" element={<AuthPage type="login" />} />

      <Route path="/register" element={<AuthPage type="register" />} />

      {/* Protected Routes */}
      <Route
        path="/client"
        element={
          <ProtectedRoute requiredRole="CLIENT">
            <ClientDashboard />
          </ProtectedRoute>
        }
      />

      <Route
        path="/freelancer"
        element={
          <ProtectedRoute requiredRole="FREELANCER">
            <FreelancerDashboard />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
};

export default AppRoutes;
