const authServiceURL = "http://localhost:8081/api/auth";
const projectServiceURL = "http://localhost:8082/api/projects";
const freelancerServiceURL = "http://localhost:8083/api/freelancers";

const api = {
  login: async (email, password) => {
    const user = await fetch(authServiceURL + "/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    }).then((res) => {
      if (!res.ok) throw new Error("Invalid credentials");
      return res.json();
    });

    if (user) return { token: user.token, user };
    throw new Error("Invalid credentials");
  },

  register: async (userData) => {
    // Register user
    await fetch(authServiceURL + "/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        name: userData.name,
        email: userData.email,
        password: userData.password,
        role: userData.role,
        skills: userData.skills,
      }),
    }).then((res) => {
      if (!res.ok) throw new Error("Registration failed");
      return res.json();
    });

    // Auto-login after registration and get token
    const loggedInUser = await fetch(authServiceURL + "/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        email: userData.email,
        password: userData.password,
      }),
    }).then((res) => {
      if (!res.ok) throw new Error("Login after registration failed");
      return res.json();
    });

    // Create profile if role is FREELANCER
    if (userData.role === "FREELANCER") {
      await fetch(freelancerServiceURL + "/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${loggedInUser.token}`,
        },
        body: JSON.stringify({
          userId: loggedInUser.id,
          name: userData.name,
          experienceLevel: userData.experienceLevel,
          skills: userData.skills,
        }),
      })
        .then((res) => {
          if (!res.ok) throw new Error("Profile creation failed");
          return res.json();
        })
        .catch((err) => {
          console.error("Error creating freelancer profile:", err);
        });
    }

    return loggedInUser;
  },

  restoreUser: async (token) => {
    const user = await fetch(authServiceURL + "/validate/token", {
      method: "POST",
      body: JSON.stringify({ token }),
      headers: { "Content-Type": "application/json" },
    }).then((res) => {
      if (!res.ok) throw new Error("Failed to restore user");
      return res.json();
    });

    return {
      id: user.userId,
      name: user.name,
      email: user.email,
      role: String(user.role).substring(5),
      token,
    };
  },

  createProject: async (projectData) => {
    const token = localStorage.getItem("token");

    const newProject = await fetch(projectServiceURL + "/new", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(projectData),
    }).then((res) => {
      if (!res.ok) throw new Error("Failed to create project");
      return res.json();
    });

    return newProject;
  },

  getProjects: async () => {
    const token = localStorage.getItem("token");

    const fetchedProjects = await fetch(projectServiceURL + "/all", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }).then((res) => {
      if (!res.ok) throw new Error("Failed to fetch projects");
      return res.json();
    });

    return fetchedProjects;
  },

  getMatches: async (projectId) => {
    const token = localStorage.getItem("token");

    const matches = await fetch(
      `${freelancerServiceURL}/${projectId}/matching-projects`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    ).then((res) => {
      if (!res.ok) throw new Error("Failed to fetch matches");
      return res.json();
    });

    return matches;
  },

  submitBid: async (bidData) => {
    const token = localStorage.getItem("token");

    const bidResponse = await fetch(freelancerServiceURL + "/bids/new", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(bidData),
    }).then((res) => {
      if (!res.ok) throw new Error("Failed to submit bid");
      return res.json();
    });

    return bidResponse;
  },
};

export default api;
