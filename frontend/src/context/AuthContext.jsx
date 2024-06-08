/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useEffect, useState } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState(() => {
    const storedAuthData = localStorage.getItem('authData');
    return storedAuthData ? JSON.parse(storedAuthData) : null;
  });

  useEffect(() => {
    const storedAuthData = JSON.parse(localStorage.getItem('authData'));

    if (storedAuthData && storedAuthData.expiration > new Date().getTime()) {
      setAuth(storedAuthData);
    } else {
      localStorage.removeItem('authData');
    }

  }, []);

  useEffect(() => {
    if (auth){
      const timeout = auth.expiration - new Date().getTime();

      const timer = setTimeout(() => {
        localStorage.removeItem('authData');
        setAuth(null);
        window.location.href = '/login';
      }, timeout);

      return () => clearTimeout(timer);
    }
  }, [auth]);

  return (
    <AuthContext.Provider value={{ auth, setAuth }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};
