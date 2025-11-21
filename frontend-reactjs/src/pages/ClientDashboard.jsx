import { useState, useEffect } from "react";
import Card from "../components/ui/Card";
import Button from "../components/ui/Button";
import Input from "../components/ui/Input";
import api from "../services/api";
import { useAuth } from "../context/AuthContext";

import { Briefcase, X, Search } from "lucide-react";

const ClientDashboard = () => {
  const { user } = useAuth();

  const [view, setView] = useState("projects");
  const [projects, setProjects] = useState([]);
  const [newProject, setNewProject] = useState({
    title: "",
    description: "",
    budget: "",
    skills: "",
  });

  const [selectedProjectMatches, setSelectedProjectMatches] = useState(null);
  const [matches, setMatches] = useState([]);

  const filterProjects = (allProjects) => {
    return allProjects.filter(
      (p) => p.clientId === user.id || p.clientId === undefined
    );
  };

  useEffect(() => {
    if (!user?.id) return;

    (async () => {
      const all = await api.getProjects();
      setProjects(filterProjects(all));
    })();
  });

  const handleCreate = async (e) => {
    e.preventDefault();

    const payload = {
      ...newProject,
      userId: user.id,
      skills: newProject.skills.split(",").map((s) => s.trim()),
    };

    const createdProject = await api.createProject(payload);

    setProjects([...projects, createdProject]);
    setNewProject({ title: "", description: "", budget: "", skills: "" });
    setView("projects");
  };

  const showMatches = async (project) => {
    const results = await api.getMatches(project.id);
    console.log(results);

    setMatches(results);
    setSelectedProjectMatches(project);
    setView("matches");
  };

  return (
    <div className="container mx-auto p-6">
      <div className="flex justify-between items-center mb-8">
        <h2 className="text-3xl font-bold">Client Dashboard</h2>
        <Button onClick={() => setView("create")}>
          <Briefcase size={18} /> Post New Project
        </Button>
      </div>

      {view === "create" && (
        <div className="max-w-2xl mx-auto">
          <Card>
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-xl font-bold">Create New Project</h3>
              <button
                onClick={() => setView("projects")}
                className="text-gray-500"
              >
                <X size={20} />
              </button>
            </div>
            <form onSubmit={handleCreate}>
              <Input
                label="Project Title"
                value={newProject.title}
                onChange={(e) =>
                  setNewProject({ ...newProject, title: e.target.value })
                }
                required
              />
              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Description
                </label>
                <textarea
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  rows="4"
                  value={newProject.description}
                  onChange={(e) =>
                    setNewProject({
                      ...newProject,
                      description: e.target.value,
                    })
                  }
                  required
                />
              </div>
              <Input
                label="Budget (₹)"
                type="number"
                value={newProject.budget}
                onChange={(e) =>
                  setNewProject({ ...newProject, budget: e.target.value })
                }
                required
              />
              <Input
                label="Required Skills (comma separated)"
                placeholder="React, Java, AWS"
                value={newProject.skills}
                onChange={(e) =>
                  setNewProject({ ...newProject, skills: e.target.value })
                }
                required
              />
              <Button type="submit" className="w-full">
                Post Project
              </Button>
            </form>
          </Card>
        </div>
      )}

      {view === "projects" && (
        <div className="grid gap-6">
          {projects.length === 0 && (
            <p className="text-gray-500 text-center py-10">
              No active projects found. Post one to get started!
            </p>
          )}
          {projects.map((p) => (
            <Card key={p.id} className="flex justify-between items-center">
              <div>
                <h3 className="text-xl font-bold text-gray-800">{p.title}</h3>
                <p className="text-gray-600">{p.description}</p>
                <div className="flex gap-2 mt-2">
                  {p.skills.map((s) => (
                    <span
                      key={s}
                      className="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full"
                    >
                      {s}
                    </span>
                  ))}
                </div>
              </div>
              <div className="text-right">
                <p className="font-bold text-green-600 text-lg">${p.budget}</p>
                <Button
                  variant="outline"
                  className="mt-2 text-sm"
                  onClick={() => showMatches(p)}
                >
                  <Search size={14} /> Find Freelancers
                </Button>
              </div>
            </Card>
          ))}
        </div>
      )}

      {view === "matches" && selectedProjectMatches && (
        <div>
          <div className="mb-6 flex items-center gap-4">
            <Button variant="secondary" onClick={() => setView("projects")}>
              Back
            </Button>
            <div>
              <h3 className="text-xl font-bold">
                Matches for "{selectedProjectMatches.title}"
              </h3>
              <p className="text-sm text-gray-500">
                Calculated Match Scores based on skills
              </p>
            </div>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {matches.map((match) => (
              <Card key={match.id} className="border-t-4 border-blue-500">
                <div className="flex justify-between items-start mb-2">
                  <h4 className="font-bold text-lg">{match.freelancerName}</h4>
                  <div className="bg-green-100 text-green-800 px-2 py-1 rounded text-xs font-bold">
                    {(match.matchScore * 100).toFixed(0)}% Match
                  </div>
                </div>
                <p className="text-gray-500 text-sm mb-4">Matched Skills:</p>
                <div className="flex flex-wrap gap-2 mb-4">
                  {match.matchingSkills.map((s) => (
                    <span
                      key={s}
                      className="bg-gray-100 text-gray-700 text-xs px-2 py-1 rounded"
                    >
                      {s}
                    </span>
                  ))}
                </div>
                <Button className="w-full text-sm" disabled={true}>
                  Accept Bid
                </Button>
              </Card>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default ClientDashboard;
