/* eslint-disable react/prop-types */
import { BrowserRouter as Router } from 'react-router-dom';

import AppRoutes from './routes/Routes';
import { AuthProvider } from './context/AuthContext';

const App = () => {
  return (
    <AuthProvider>
      <Router>
        <AppRoutes />
      </Router>
    </AuthProvider>
  );
};

export default App;
