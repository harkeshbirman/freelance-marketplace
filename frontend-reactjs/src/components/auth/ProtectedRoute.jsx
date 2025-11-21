import { Navigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

const ProtectedRoute = ({ requiredRole, children }) => {
  const { user, loading } = useAuth();
  // console.log("ProtectedRoute user:", user);
  if (loading) return <div className="p-8 text-center">Loading...</div>;

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (requiredRole && user.role !== requiredRole) {
    // Redirect to their appropriate dashboard if they try to access wrong routes
    return (
      <Navigate
        to={user.role === "CLIENT" ? "/client" : "/freelancer"}
        replace
      />
    );
  }

  return children;
};

export default ProtectedRoute;
