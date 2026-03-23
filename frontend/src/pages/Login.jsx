import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/authApi";
import { saveAuth } from "../utils/auth";
import "./Login.css";

export default function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("customer@gmail.com");
  const [password, setPassword] = useState("1234");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);
    try {
      const res = await login({ email, password });
      saveAuth(res.data.token, res.data.user);
      window.dispatchEvent(new Event("storage"));
      navigate("/properties");
    } catch (err) {
      if (err.response?.data?.message) {
         setError(err.response.data.message);
      } else if (typeof err.response?.data === 'string') {
         setError(err.response.data);
      } else {
         setError("Login failed. Please check your credentials.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h2>Welcome Back</h2>
          <p>Sign in to your account</p>
        </div>

        {error && (
            <div className="alert-error">
              <span>{error}</span>
            </div>
        )}

        <form onSubmit={handleLogin} className="auth-form">
          <div className="form-group">
            <label htmlFor="email">Email Address</label>
            <input 
              id="email" type="email" required
              value={email} onChange={e => setEmail(e.target.value)} 
              placeholder="you@example.com"
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input 
              id="password" type="password" required
              value={password} onChange={e => setPassword(e.target.value)} 
              placeholder="••••••••"
            />
          </div>

          <button 
            type="submit" 
            disabled={isLoading}
            className="auth-btn primary-btn"
          >
            {isLoading ? "Signing In..." : "Sign In"}
          </button>
        </form>

        <div className="auth-footer">
          <span>Don't have an account? </span>
          <button onClick={() => navigate("/register")} className="link-btn">
            Create account
          </button>
        </div>
      </div>
    </div>
  );
}
