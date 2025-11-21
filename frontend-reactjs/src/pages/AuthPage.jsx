import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Card from "../components/ui/Card";
import Input from "../components/ui/Input";
import Button from "../components/ui/Button";
import api from "../services/api";
import { useAuth } from "../context/AuthContext";

const AuthPage = ({ type }) => {
  const [role, setRole] = useState("CLIENT");
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    name: "",
    experienceLevel: "BEGINNER",
    skills: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();

  const navigate = useNavigate();

  // Redirect based on role
  const onLoginSuccess = (user) => {
    if (user.role === "CLIENT") {
      navigate("/client");
    } else if (user.role === "FREELANCER") {
      navigate("/freelancer");
    } else {
      navigate("/");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      if (type === "login") {
        const { token, user } = await api.login(
          formData.email,
          formData.password
        );

        login(user, token);
        onLoginSuccess(user);
      } else {
        const regData = {
          ...formData,
          role,
          // Only include experience level and skills if freelancer
          experienceLevel:
            role === "FREELANCER" ? formData.experienceLevel : null,
          skills:
            role == "FREELANCER"
              ? formData.skills.split(",").map((s) => s.trim())
              : null,
        };

        const newUser = await api.register(regData);

        login(newUser, newUser.token);
        onLoginSuccess(newUser);
      }
    } catch (err) {
      setError(err.message || "An error occurred");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10">
      <Card>
        <h2 className="text-2xl font-bold mb-6 text-center">
          {type === "login" ? "Welcome Back" : "Create Account"}
        </h2>

        {error && (
          <div className="bg-red-100 text-red-700 p-3 rounded-md mb-4 text-sm">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          {type === "register" && (
            <>
              <div className="flex gap-2 mb-6 bg-gray-100 p-1 rounded-md">
                <button
                  type="button"
                  onClick={() => setRole("CLIENT")}
                  className={`flex-1 py-2 rounded text-sm font-medium transition-colors ${
                    role === "CLIENT"
                      ? "bg-white shadow text-blue-600"
                      : "text-gray-500 hover:text-gray-700"
                  }`}
                >
                  Client
                </button>
                <button
                  type="button"
                  onClick={() => setRole("FREELANCER")}
                  className={`flex-1 py-2 rounded text-sm font-medium transition-colors ${
                    role === "FREELANCER"
                      ? "bg-white shadow text-blue-600"
                      : "text-gray-500 hover:text-gray-700"
                  }`}
                >
                  Freelancer
                </button>
              </div>

              <Input
                label="Full Name"
                value={formData.name}
                onChange={(e) =>
                  setFormData({ ...formData, name: e.target.value })
                }
                required
              />
            </>
          )}

          <Input
            label="Email Address"
            type="email"
            value={formData.email}
            onChange={(e) =>
              setFormData({ ...formData, email: e.target.value })
            }
            required
          />

          <Input
            label="Password"
            type="password"
            value={formData.password}
            onChange={(e) =>
              setFormData({ ...formData, password: e.target.value })
            }
            required
          />

          {role === "FREELANCER" && (
            <>
              <Input
                label="Skills (Atleast 3 skills separated by commas)"
                value={formData.skills}
                onChange={(e) =>
                  setFormData({ ...formData, skills: e.target.value })
                }
                placeholder={"React, AWS, Spring Boot"}
                required
              />

              {role === "FREELANCER" && (
                <div className="mb-4">
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Experience Level
                  </label>
                  <select
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    value={formData.experienceLevel}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        experienceLevel: e.target.value,
                      })
                    }
                  >
                    <option value="BEGINNER">Beginner (0-2 years)</option>
                    <option value="INTERMEDIATE">
                      Intermediate (2-5 years)
                    </option>
                    <option value="EXPERT">Expert (5+ years)</option>
                  </select>
                </div>
              )}
            </>
          )}
          <Button type="submit" className="w-full" disabled={loading}>
            {loading
              ? "Processing..."
              : type === "login"
              ? "Log In"
              : "Sign Up"}
          </Button>
        </form>
      </Card>
    </div>
  );
};

export default AuthPage;
