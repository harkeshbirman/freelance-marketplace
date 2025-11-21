import { useNavigate } from "react-router-dom";
import { User, CheckCircle, Briefcase } from "lucide-react";
import Button from "../components/ui/Button";
import Card from "../components/ui/Card";

const LandingPage = () => {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col items-center justify-center min-h-[80vh] text-center px-4">
      <h1 className="text-5xl font-bold text-gray-900 mb-6">
        Find the perfect <span className="text-blue-600">freelance</span>{" "}
        services
      </h1>
      <p className="text-xl text-gray-600 mb-8 max-w-2xl">
        Connect with top talent or find your next big project.
      </p>
      <div className="flex gap-4">
        <Button
          onClick={() => navigate("/register")}
          className="text-lg px-8 py-3"
        >
          Get Started
        </Button>
        <Button
          variant="outline"
          onClick={() => navigate("/login")}
          className="text-lg px-8 py-3"
        >
          Log In
        </Button>
      </div>

      <div className="mt-16 grid grid-cols-1 md:grid-cols-3 gap-8 max-w-5xl w-full">
        {[
          {
            icon: <User size={32} />,
            title: "Expert Talent",
            desc: "Access verified professionals.",
          },
          {
            icon: <CheckCircle size={32} />,
            title: "Curated Matches",
            desc: "Find the right fit quickly.",
          },
          {
            icon: <Briefcase size={32} />,
            title: "Quality Work",
            desc: "Get work done efficiently.",
          },
        ].map((item, i) => (
          <Card key={i} className="flex flex-col items-center">
            <div className="text-blue-600 mb-4">{item.icon}</div>
            <h3 className="text-lg font-bold mb-2">{item.title}</h3>
            <p className="text-gray-500 text-sm">{item.desc}</p>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default LandingPage;
