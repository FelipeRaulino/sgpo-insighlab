import { Button, Divider, Flex, Form, Input, Select, notification } from 'antd';
import MenuComponent from '../../components/menu/Menu';
import './Profile.css';
import { UserOutlined } from '@ant-design/icons';
import Title from 'antd/es/typography/Title';
import { useAuth } from '../../context/AuthContext';
import { useEffect } from 'react';
import axios from '../../config/axiosConfig';

const roleMapping = {
  ROLE_ADMIN: 'Admin',
  ROLE_EMPLOYEE: 'Employee'
};

const reverseRoleMapping = {
  Admin: 'ROLE_ADMIN',
  Employee: 'ROLE_EMPLOYEE'
};

const transformRoles = (roles) => {
  return roles.map(role => roleMapping[role] || role);
};


const Profile = () => {
  const { auth, setAuth } = useAuth();
  const [form] = Form.useForm();

  const [api, contextHolder] = notification.useNotification();

  const openLoginNotification = async (operation, username) => {
    if (operation === 'updated'){
      api['success']({
        message: 'Atualizado com sucesso!',
        description: `O(a) usuário(a) ${username} foi atualizado com sucesso!`,
      });
    }

    if (operation === 'error'){
      api['error']({
        message: 'Erro ao atualizar!',
        description: `Erro ao atualizar ${username}, tente com outro username!`,
      });
    }
  };

  useEffect(() => {
    if (auth) {
      form.setFieldsValue({
        username: auth.username,
        roles: transformRoles(auth.roles)
      });
    }
  }, [auth, form]);

  const onFinish = async (values) => {
    const transformedValues = {
      ...values,
      roles: values.roles.map(role => reverseRoleMapping[role] || role)
    };

    try {
      const response = await axios.put(`/users/${auth.id}`, transformedValues);
      const { username, roles, token } = response.data;

      const newAuthData = {
        ...auth,
        username: username,
        roles: roles,
        token: token
      };

      localStorage.setItem('authData', JSON.stringify(newAuthData));
      setAuth(newAuthData);
      openLoginNotification('updated', username);

    } catch (error) {
      openLoginNotification('error', transformedValues.username);
    }
  };

  const isAdmin = auth?.roles.includes('ROLE_ADMIN');

  return (
    <div>
      {contextHolder}
      <MenuComponent currentPage="profile" />
      <Flex className="profile-container" vertical align="center">
        <Title>Personalização do usuário</Title>
        <Divider />
        <Form
          form={form}
          name="profile-form"
          className="profile-form"
          onFinish={onFinish}
        >
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
            name="roles"
            rules={[
              {
                required: true,
                message: 'Por favor, selecione a(s) função(ões)!'
              }
            ]}
          >
            <Select
              mode="multiple"
              placeholder="Função(ões)"
              style={{
                width: 120,
              }}
              options={[
                {
                  value: 'Admin',
                  label: 'Admin',
                },
                {
                  value: 'Employee',
                  label: 'Employee',
                }
              ]}
              disabled={!isAdmin}
            />
          </Form.Item>
          <Form.Item className="form-button-wrapper">
            <Button size="large" type="primary" htmlType="submit">
							Atualizar
            </Button>
          </Form.Item>
        </Form>
      </Flex>
    </div>
  );
};

export default Profile;
