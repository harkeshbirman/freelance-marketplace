import { Link, useNavigate } from "react-router-dom";
import { Layout, LogOut } from "lucide-react";
import { useAuth } from "../../context/AuthContext";
import Button from "../ui/Button";

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <nav className="bg-white shadow-sm sticky top-0 z-50">
      <div className="container mx-auto px-6 py-4 flex justify-between items-center">
        <Link to="/" className="flex items-center gap-2">
          <Layout className="text-blue-600" size={28} />
          <span className="text-xl font-bold tracking-tight text-gray-900">
            Marketplace
          </span>
        </Link>
        <div>
          {user ? (
            <div className="flex items-center gap-4">
              <span className="hidden md:inline text-gray-600 text-sm">
                Welcome,{" "}
                <span className="font-bold text-gray-900">{user.name}</span>
              </span>
              <Button
                variant="secondary"
                onClick={handleLogout}
                className="px-3 py-1 text-sm"
              >
                <LogOut size={16} /> Logout
              </Button>
            </div>
          ) : (
            <div className="hidden md:flex gap-4">
              <Link
                to="/login"
                className="font-medium text-gray-600 hover:text-blue-600 px-4 py-2"
              >
                Log In
              </Link>
              <Link
                to="/register"
                className="font-medium text-white bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded-md"
              >
                Sign Up
              </Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
