import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { Button, Flex, Form, Input, notification } from 'antd';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Signup.css';
import Title from 'antd/es/typography/Title';


const Signup = () => {
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
        description: `${username} já existe!`,
      });
    }
  };

  const onFinish = async (values) => {
    try {
      const response = await axios.post('http://localhost:8080/auth/signup', values);

      await openLoginNotification('authenticated', response.data.username);

      setTimeout(() => {
        navigate('/login');
      }, 1500);
    } catch (error) {
      await 	openLoginNotification('error', values.username);
    }
  };

  return (
    <div className="signup-page">
      {contextHolder}
      <div className="signup-page__illustration" />
      <div className="signup-page__form">
        <Form
          name="signup-form"
          className="signup-form"
          initialValues={{ remember: true }}
          onFinish={onFinish}
        >
          <Flex gap="0.5rem" align="center" justify="center" vertical>
            <UserOutlined style={{ fontSize: '3rem' }}  />
            <Title>Cadastro de Usuário</Title>
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
            Cadastrar
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default Signup;
