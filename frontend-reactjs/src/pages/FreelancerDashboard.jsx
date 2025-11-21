import { useState, useEffect } from "react";
import Card from "../components/ui/Card";
import Button from "../components/ui/Button";
import Input from "../components/ui/Input";
import api from "../services/api";
import { useAuth } from "../context/AuthContext";

const FreelancerDashboard = () => {
  const [projects, setProjects] = useState([]);
  const [selectedProject, setSelectedProject] = useState(null);
  const [bidForm, setBidForm] = useState({ bidAmount: "", message: "" });
  const [bidStatus, setBidStatus] = useState(null);

  const { user } = useAuth();

  useEffect(() => {
    (async () => {
      const data = await api.getProjects();
      setProjects(data);
    })();
  }, []);

  const handleBid = async (e) => {
    e.preventDefault();
    await api.submitBid({
      ...bidForm,
      projectId: selectedProject.id,
      freelancerId: user.id,
    });
    setBidStatus("Bid sent successfully!");
    setTimeout(() => {
      setSelectedProject(null);
      setBidStatus(null);
      setBidForm({ bidAmount: "", message: "" });
    }, 2000);
  };

  return (
    <div className="container mx-auto p-6">
      <h2 className="text-3xl font-bold mb-2">Freelancer Portal</h2>
      <p className="text-gray-500 mb-8">Browse projects and place your bids.</p>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div className="space-y-6">
          <h3 className="font-bold text-lg text-gray-700">Open Projects</h3>
          {projects.map((p) => (
            <div
              key={p.id}
              onClick={() => setSelectedProject(p)}
              className={`p-4 rounded-lg border cursor-pointer transition-all hover:shadow-md ${
                selectedProject?.id === p.id
                  ? "border-blue-500 bg-blue-50"
                  : "border-gray-200 bg-white"
              }`}
            >
              <div className="flex justify-between mb-2">
                <h4 className="font-bold">{p.title}</h4>
                <span className="text-green-600 font-bold">₹{p.budget}</span>
              </div>
              <p className="text-sm text-gray-600 line-clamp-2">
                {p.description}
              </p>
              <div className="mt-2 flex gap-1">
                {p.skills.map((s) => (
                  <span
                    key={s}
                    className="text-xs bg-gray-200 px-2 py-0.5 rounded text-gray-600"
                  >
                    {s}
                  </span>
                ))}
              </div>
            </div>
          ))}
        </div>

        <div className="sticky top-6 space-y-6">
          {selectedProject ? (
            <Card>
              <h3 className="text-xl font-bold mb-2">
                {selectedProject.title}
              </h3>
              <p className="text-gray-600 mb-4">
                {selectedProject.description}
              </p>

              <div className="bg-gray-50 p-4 rounded-md mb-6">
                <h4 className="font-bold text-sm text-gray-500 uppercase mb-2">
                  Project Details
                </h4>
                <div className="flex justify-between mb-1">
                  <span>Client Budget:</span>
                  <span className="font-bold text-green-600">
                    ₹{selectedProject.budget}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span>Required Skills:</span>
                  <span className="text-right">
                    {selectedProject.skills.join(", ")}
                  </span>
                </div>
              </div>

              {bidStatus ? (
                <div className="bg-green-100 text-green-800 p-4 rounded text-center font-bold">
                  {bidStatus}
                </div>
              ) : (
                <form onSubmit={handleBid}>
                  <h4 className="font-bold mb-3">Submit Your Proposal</h4>
                  <Input
                    label="Bid Amount (₹)"
                    type="number"
                    value={bidForm.bidAmount}
                    onChange={(e) =>
                      setBidForm({ ...bidForm, bidAmount: e.target.value })
                    }
                    required
                  />
                  <div className="mb-4">
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Message To Client
                    </label>
                    <textarea
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      rows="4"
                      value={bidForm.message}
                      onChange={(e) =>
                        setBidForm({ ...bidForm, message: e.target.value })
                      }
                      required
                    />
                  </div>
                  <Button type="submit" className="w-full">
                    Submit Bid
                  </Button>
                </form>
              )}
            </Card>
          ) : (
            <div className="h-64 border-2 border-dashed border-gray-300 rounded-lg flex items-center justify-center text-gray-400">
              Select a project to view details and bid
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default FreelancerDashboard;
