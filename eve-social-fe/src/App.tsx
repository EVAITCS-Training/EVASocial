import React from 'react';
import { Routes, Route, BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/navigation/Navbar';
import PostsFeed from './components/posts/PostsFeed';
import LoginForm from './components/auth/LoginForm';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

// Create a client for React Query
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <div className="App">
            <Navbar />
            <main className="py-4">
              <Routes>
                <Route path="/" element={<PostsFeed />} />
                <Route path="/posts" element={<PostsFeed />} />
                <Route path="/feed" element={<PostsFeed />} />
                <Route path="/login" element={<LoginForm />} />
              </Routes>
            </main>
          </div>
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
