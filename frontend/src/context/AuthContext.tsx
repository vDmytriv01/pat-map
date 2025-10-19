import { createContext, useCallback, useEffect, useMemo, useState, type ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';
import { login as loginRequest, register as registerRequest } from '../api/auth';
import { LoginCredentials, RegisterPayload } from '../types';

interface AuthContextValue {
  token: string | null;
  isAuthenticated: boolean;
  login: (credentials: LoginCredentials) => Promise<void>;
  register: (payload: RegisterPayload) => Promise<void>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem('patmap_token'));
  const navigate = useNavigate();

  useEffect(() => {
    if (token) {
      localStorage.setItem('patmap_token', token);
    } else {
      localStorage.removeItem('patmap_token');
    }
  }, [token]);

  const login = useCallback(async (credentials: LoginCredentials) => {
    const { token: receivedToken } = await loginRequest(credentials);
    setToken(receivedToken);
    navigate('/categories');
  }, [navigate]);

  const register = useCallback(async (payload: RegisterPayload) => {
    const { token: receivedToken } = await registerRequest(payload);
    setToken(receivedToken);
    navigate('/categories');
  }, [navigate]);

  const logout = useCallback(() => {
    setToken(null);
    navigate('/login');
  }, [navigate]);

  const value = useMemo(
    () => ({
      token,
      isAuthenticated: Boolean(token),
      login,
      register,
      logout
    }),
    [login, logout, register, token]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
