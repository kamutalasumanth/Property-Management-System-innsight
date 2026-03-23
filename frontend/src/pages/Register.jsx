import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../api/authApi";
import "./Login.css";

export default function Register() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ name: "", email: "", password: "", role: "CUSTOMER" });
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleRegister = async (e) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);
    try {
      await register(formData);
      navigate("/login");
    } catch (err) {
      if (err.response?.data?.message) {
         setError(err.response.data.message);
      } else if (typeof err.response?.data === 'string') {
         setError(err.response.data);
      } else {
         setError("Registration failed. Please properly check your inputs.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h2>Create Account</h2>
          <p>Join Innsight today</p>
        </div>

        {error && (
            <div className="alert-error">
              <span>{error}</span>
            </div>
        )}

        <form onSubmit={handleRegister} className="auth-form">
          <div className="form-group">
            <label>Full Name</label>
            <input name="name" placeholder="John Doe" required onChange={handleChange} />
          </div>

          <div className="form-group">
            <label>Email Address</label>
            <input name="email" type="email" placeholder="you@example.com" required onChange={handleChange} />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input name="password" type="password" placeholder="••••••••" required onChange={handleChange} />
          </div>

          <div className="form-group">
            <label>Account Type</label>
            <select name="role" onChange={handleChange}>
              <option value="CUSTOMER">Customer (Rent Properties)</option>
              <option value="OWNER">Property Owner (Host Properties)</option>
            </select>
          </div>

          <button 
            type="submit" 
            disabled={isLoading}
            className="auth-btn success-btn"
          >
            {isLoading ? "Creating..." : "Register Account"}
          </button>
        </form>

        <div className="auth-footer">
          <span>Already have an account? </span>
          <button onClick={() => navigate("/login")} className="link-btn" style={{color: "var(--success)"}}>
            Sign In
          </button>
        </div>
      </div>
    </div>
  );
}
