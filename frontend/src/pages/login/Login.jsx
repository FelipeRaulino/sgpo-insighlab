import axios from "../../config/axiosConfig";
import { Button, Flex, Form, Input, notification } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { useAuth } from '../../context/AuthContext';
import './Login.css';
import { useNavigate } from 'react-router-dom';
import Title from 'antd/es/typography/Title';
import { useEffect } from 'react';


const Login = () => {
  const { auth, setAuth } = useAuth();
  const navigate = useNavigate();

  const [api, contextHolder] = notification.useNotification();

  const openLoginNotification = async (operation, username) => {
    if (operation === 'authenticated'){
      api['success']({
        message: 'Autenticado com sucesso!',
        description: `O(a) usuário(a) ${username} foi autenticado com sucesso!`,
      });
    }

    if (operation === 'error'){
      api['error']({
        message: 'Erro ao se autenticar!',
        description: `Revise as credenciais do(a) ${username}!`,
      });
    }
  };

  useEffect(() => {
    if (auth){
      setTimeout(() => {
        navigate('/home');
      }, 1500);
    }
  }, [auth, navigate]);

  const onFinish = async (values) => {
    try {
      const response = await axios.post('/auth/signin', values);

      const { id, accessToken, roles, expiration, username } = response.data;
      const authData = {
        id: id,
        username: username,
        token: accessToken,
        roles: roles,
        expiration: new Date(expiration).getTime()
      };

      localStorage.setItem('authData', JSON.stringify(authData));
      setAuth(authData);

      await openLoginNotification('authenticated', values.username);

    } catch (error) {
      await 	openLoginNotification('error', values.username);
    }
  };

  return (
    <div className="login-page">
      {contextHolder}
      <div className="login-page__illustration" />
      <div className="login-page__form">
        <Form
          name="login-form"
          className="login-form"
          initialValues={{ remember: true }}
          onFinish={onFinish}
        >
          <Flex gap="0.5rem" align="center" justify="center" vertical>
            <UserOutlined style={{ fontSize: '3rem' }}  />
            <Title>Login</Title>
          </Flex>
          <Form.Item
            name="username"
            rules={[
              {
                required: true,
                message: 'Por favor, insira seu nome de usuário!'
              }
            ]}
          >
            <Input
              prefix={<UserOutlined className="site-form-item-icon" />}
              placeholder="Nome do Usuário"
            />
          </Form.Item>
          <Form.Item
            name="password"
            rules={[{ required: true, message: 'Por favor, insira sua senha!' }]}
          >
            <Input
              prefix={<LockOutlined className="site-form-item-icon" />}
              type="password"
              placeholder="Senha"
            />
          </Form.Item>
          <Form.Item className="form-button-wrapper">
            <Button size="large" type="primary" htmlType="submit">
            Entrar
            </Button>
          </Form.Item>
          <Form.Item className="form-signup-wrapper">
            <span>Não possui uma conta ? <a href="/signup">Registre-se agora!</a></span>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default Login;
