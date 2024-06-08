import { Navigate, Route, Routes } from 'react-router-dom';
import Login from '../pages/login/Login';
import Signup from '../pages/signup/Signup';
import Supplier from '../pages/supplier/Supplier';
import { useAuth } from '../context/AuthContext';
import Home from '../pages/home/Home';
import Profile from '../pages/profile/Profile';
import Users from '../pages/users/Users';

const PrivateRoute = ({ element: Element, roles, ...rest }) => {
  const { auth } = useAuth();

  if (!auth) {
    return <Navigate to="/login" />;
  }

  if (roles && roles.length && !roles.some(role => auth.roles.includes(role))){
    return <Navigate to="/dashboard" />;
  }

  return <Element {...rest}/>;
};

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/suppliers" element={<PrivateRoute element={Supplier} roles={['ROLE_ADMIN', 'ROLE_EMPLOYEE']}/>} />
      <Route path="/home" element={<PrivateRoute element={Home} roles={['ROLE_ADMIN', 'ROLE_EMPLOYEE']}/>} />
      <Route path="/profile" element={<PrivateRoute element={Profile} roles={['ROLE_ADMIN', 'ROLE_EMPLOYEE']}/>} />
      <Route path="/users" element={<PrivateRoute element={Users} roles={['ROLE_ADMIN', 'ROLE_EMPLOYEE']}/>} />
      <Route path="/" element={<Navigate to="/login" />} />
    </Routes>
  );
};

export default AppRoutes;
