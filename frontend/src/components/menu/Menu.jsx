import { useState } from 'react';
import { Menu } from 'antd';
import { HomeOutlined, UserOutlined, PartitionOutlined, UserAddOutlined, LogoutOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
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
        <a href={'/home'} rel="noopener noreferrer">
					Home
        </a>
      ),
      key: 'home',
      icon: <HomeOutlined />
    },
    {
      label: (
        <a href={'/users'} rel="noopener noreferrer">
					Usuários
        </a>
      ),
      key: 'users',
      icon: <UserAddOutlined />
    },
    {
      label: (
        <a href={'/suppliers'} rel="noopener noreferrer">
					Fornecedores
        </a>
      ),
      key: 'suppliers',
      icon: <PartitionOutlined />
    },
    {
      label: (
        <a href={'/profile'} rel="noopener noreferrer">
					Perfil
        </a>
      ),
      key: 'profile',
      icon: <UserOutlined />
    },
    {
      label: (
        <a onClick={logout} rel="noopener noreferrer">
					Logout
        </a>
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
