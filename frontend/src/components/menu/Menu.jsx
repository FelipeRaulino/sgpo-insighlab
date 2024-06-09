import { useState } from 'react';
import { Menu } from 'antd';
import { HomeOutlined, UserOutlined, PartitionOutlined, UserAddOutlined, LogoutOutlined } from '@ant-design/icons';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const MenuComponent = ({ currentPage }) => {
  const [current, setCurrent] = useState(currentPage);
  const navigate = useNavigate();
  const { setAuth } = useAuth();

  const logout = () => {
    localStorage.removeItem('authData');
    setAuth(null);
    navigate('/login');
  };

  const items = [
    {
      label: (
        <Link to='/home' rel="noopener noreferrer">
					Home
        </Link>
      ),
      key: 'home',
      icon: <HomeOutlined />
    },
    {
      label: (
        <Link to='/users' rel="noopener noreferrer">
					Usuários
        </Link>
      ),
      key: 'users',
      icon: <UserAddOutlined />
    },
    {
      label: (
        <Link to='/suppliers' rel="noopener noreferrer">
					Fornecedores
        </Link>
      ),
      key: 'suppliers',
      icon: <PartitionOutlined />
    },
    {
      label: (
        <Link to='/profile' rel="noopener noreferrer">
					Perfil
        </Link>
      ),
      key: 'profile',
      icon: <UserOutlined />
    },
    {
      label: (
        <Link onClick={logout} rel="noopener noreferrer">
					Logout
        </Link>
      ),
      key: 'logout',
      icon: <LogoutOutlined />
    }
  ];

  const onClick = (e) => {
    setCurrent(e.key);
  };

  return <Menu onClick={onClick} selectedKeys={[current]} mode="horizontal" items={items} />;
};

export default MenuComponent;
