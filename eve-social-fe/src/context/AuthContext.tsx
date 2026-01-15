import React, { createContext, useContext, useEffect, useState } from 'react';
import { apiService } from '../services/api';
import type { AuthRequest, PostNewUser } from '../types/api';

interface AuthContextType {
  isAuthenticated: boolean;
  login: (credentials: AuthRequest) => Promise<void>;
  register: (userData: PostNewUser) => Promise<void>;
  logout: () => void;
  loading: boolean;
  error: string | null;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    // Check if user is already authenticated on app load
    setIsAuthenticated(apiService.isAuthenticated());
  }, []);

  const login = async (credentials: AuthRequest) => {
    setLoading(true);
    setError(null);
    try {
      await apiService.login(credentials);
      setIsAuthenticated(true);
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Login failed';
      setError(errorMessage);
      setIsAuthenticated(false);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const register = async (userData: PostNewUser) => {
    setLoading(true);
    setError(null);
    try {
      await apiService.register(userData);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Registration failed');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    apiService.logout();
    setIsAuthenticated(false);
    setError(null);
  };

  const value = {
    isAuthenticated,
    login,
    register,
    logout,
    loading,
    error,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};