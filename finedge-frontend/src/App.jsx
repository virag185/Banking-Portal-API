import { useState } from "react";
import api from "./services/api";
import Dashboard from "./pages/Dashboard";

function App() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const [loggedIn, setLoggedIn] = useState(
    Boolean(localStorage.getItem("token"))
  );

  const handleLogin = async (event) => {
    event.preventDefault();

    setLoading(true);
    setMessage("");
    setError("");

    try {
      console.log("SIGN IN BUTTON CLICKED");

      const response = await api.post("/api/auth/login", {
        email: email,
        password: password,
      });

      console.log("Login response:", response.data);

      const token = response.data.token;

      if (!token) {
        throw new Error("No JWT token received from server.");
      }

      localStorage.setItem("token", token);

      setMessage("Login successful!");
      setLoggedIn(true);

    } catch (err) {
      console.error("========== LOGIN ERROR ==========");
      console.error("Error:", err);
      console.error("Message:", err.message);
      console.error("Code:", err.code);
      console.error("Response:", err.response);
      console.error("Request:", err.request);

      if (err.response?.data?.message) {
        setError(err.response.data.message);
      } else if (err.code === "ERR_NETWORK") {
        setError(
          "Cannot connect to FinEdge backend. Check that Spring Boot is running on port 8080."
        );
      } else {
        setError(
          err.message || "Login request failed."
        );
      }

    } finally {
      setLoading(false);
    }
  };

  // If logged in, show Dashboard
  if (loggedIn) {
    return <Dashboard />;
  }

  return (
    <div className="min-h-screen bg-slate-950 text-white flex items-center justify-center px-6">

      <div className="w-full max-w-md">

        {/* Logo */}

        <div className="text-center mb-8">

          <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-blue-600 shadow-lg shadow-blue-600/20">

            <span className="text-2xl font-bold">
              F
            </span>

          </div>

          <h1 className="text-3xl font-bold">
            FinEdge
          </h1>

          <p className="mt-2 text-slate-400">
            Secure digital banking
          </p>

        </div>


        {/* Login Card */}

        <div className="rounded-2xl border border-slate-800 bg-slate-900 p-8 shadow-2xl">

          <h2 className="text-2xl font-semibold">
            Welcome back
          </h2>

          <p className="mt-2 text-slate-400">
            Sign in to access your account
          </p>


          {/* Login Form */}

          <form
            onSubmit={handleLogin}
            className="mt-7 space-y-5"
          >

            {/* Email */}

            <div>

              <label
                htmlFor="email"
                className="mb-2 block text-sm font-medium text-slate-300"
              >
                Email
              </label>

              <input
                id="email"
                type="email"
                value={email}
                onChange={(event) =>
                  setEmail(event.target.value)
                }
                placeholder="you@example.com"
                required
                autoComplete="email"
                className="w-full rounded-xl border border-slate-700 bg-slate-800 px-4 py-3 text-white placeholder-slate-500 outline-none transition focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
              />

            </div>


            {/* Password */}

            <div>

              <label
                htmlFor="password"
                className="mb-2 block text-sm font-medium text-slate-300"
              >
                Password
              </label>

              <input
                id="password"
                type="password"
                value={password}
                onChange={(event) =>
                  setPassword(event.target.value)
                }
                placeholder="••••••••"
                required
                autoComplete="current-password"
                className="w-full rounded-xl border border-slate-700 bg-slate-800 px-4 py-3 text-white placeholder-slate-500 outline-none transition focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
              />

            </div>


            {/* Error */}

            {error && (

              <div className="rounded-xl border border-red-500/20 bg-red-500/10 px-4 py-3 text-sm text-red-400">

                {error}

              </div>

            )}


            {/* Success */}

            {message && (

              <div className="rounded-xl border border-green-500/20 bg-green-500/10 px-4 py-3 text-sm text-green-400">

                {message}

              </div>

            )}


            {/* Login Button */}

            <button
              type="submit"
              disabled={loading}
              className="w-full rounded-xl bg-blue-600 py-3 font-semibold transition hover:bg-blue-500 disabled:cursor-not-allowed disabled:opacity-60"
            >

              {loading
                ? "Signing in..."
                : "Sign In"
              }

            </button>

          </form>


          {/* Register */}

          <p className="mt-6 text-center text-sm text-slate-400">

            Don't have an account?{" "}

            <span className="cursor-pointer font-medium text-blue-400 hover:text-blue-300">
              Create one
            </span>

          </p>

        </div>


        {/* Footer */}

        <p className="mt-6 text-center text-xs text-slate-600">
          FinEdge • Secure Banking Platform
        </p>

      </div>

    </div>
  );
}

export default App;