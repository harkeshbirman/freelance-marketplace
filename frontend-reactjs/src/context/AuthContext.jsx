import { createContext, useContext, useState, useEffect } from "react";
import api from "../services/api";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const restoreUser = async (token) => {
    if (!token) return null;

    const res = await api
      .restoreUser(token)
      .then((data) => data)
      .catch((err) => {
        console.error("Failed to restore user:", err);
        return null;
      });

    setUser(res);
    return res;
  };

  useEffect(() => {
    let mounted = true;
    const token = localStorage.getItem("token");

    if (!token) {
      setLoading(false);
      return;
    }

    (async () => {
      setLoading(true);
      const restored = await restoreUser(token);

      if (!mounted) return;

      if (restored) {
        setUser(restored);
      } else {
        localStorage.removeItem("token");
        setUser(null);
      }

      setLoading(false);
    })();

    return () => {
      mounted = false;
    };
  }, []);

  const login = (userData, token) => {
    localStorage.setItem("token", String(token));
    setUser(userData);
  };

  const logout = () => {
    localStorage.removeItem("token");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
