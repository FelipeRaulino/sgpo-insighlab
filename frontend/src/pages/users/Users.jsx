import { useCallback, useEffect, useState } from 'react';
import axios from '../../config/axiosConfig';
import { Button, Divider, Flex, Form, Input, Modal, Popconfirm, Select, Space, Table, notification } from 'antd';
import './Users.css';
import MenuComponent from '../../components/menu/Menu';
import Title from 'antd/es/typography/Title';
import { useAuth } from '../../context/AuthContext';
import { StopOutlined } from '@ant-design/icons';


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

const reverseTransformRoles = (roles) => {
  return roles.map(role => reverseRoleMapping[role] || role);
};

const Users = () => {
  const [users, setUsers] = useState([]);

  const { auth } = useAuth();

  const [form] = Form.useForm();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingUser, setEditingUser] = useState(null);

  const [api, contextHolder] = notification.useNotification();

  const openUserNotification = (operation, username) => {
    if (operation === 'create'){
      api['success']({
        message: 'Adicionado com sucesso!',
        description: `O usuário(a) ${username} foi adicionado com sucesso!`
      });
    }

    if (operation === 'update'){
      api['success']({
        message: 'Atualizado com sucesso!',
        description: `O fornecedor(a) ${username} foi atualizado com sucesso!`
      });
    }

    if (operation === 'delete'){
      api['success']({
        message: 'Deletado com sucesso!',
        description: `O fornecedor(a) ${username} foi deletado com sucesso!`
      });
    }
  };

  const columns = [
    {
      title: 'Nome de usuário',
      dataIndex: 'username',
      key: 'name',
    },
    {
      title: 'Função(ões)',
      dataIndex: 'roles',
      key: 'roles',
      render: (roles) => transformRoles(roles).join(', ')
    },
    {
      title: 'Ação',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          <a onClick={() => handleOnUpdate(record)}>Editar</a>
          <Popconfirm
            title="Excluir fornecedor"
            description="Você tem certeza que deseja excluir fornecedor ?"
            onConfirm={() => handleOnDelete(record)}
            okText="Sim"
            cancelText="Não"
          >
            <Button>Excluir</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const transformedUsers = users.map(user => {
    return {
      ...user,
      key: user.id
    };
  });

  const showModal = () => {
    setEditingUser(null);
    setIsModalOpen(true);
  };

  const handleOnFinish = (values) => {
    if (editingUser) {
      const edittedUser = {username: editingUser.username, roles: reverseTransformRoles(values.roles)}
      axios.put(`/api/v1/users/${editingUser.id}`, edittedUser)
        .then(response => {
          setUsers(prevUsers => prevUsers.map(user =>
            user.id === editingUser.id ? response.data : user
          ));
          form.resetFields();
          openUserNotification('update', response.data.username);
          setIsModalOpen(false);
        })
        .catch(error => console.log('Error: ', error));

    } else {
      axios.post('/auth/signup', {...values, roles: reverseTransformRoles(values.roles)})
        .then(response => {
          setUsers(prevUsers => [...prevUsers, response.data]);
          openUserNotification('create', response.data.username);
          form.resetFields();
        })
        .catch(error => console.log(error));

      setIsModalOpen(false);
    }

  };

  const handleOnDelete = (record) => {
    axios.delete(`/api/v1/users/${record.id}`)
      .then(response => {
        if(response.status === 204){
          setUsers(prevUsers => prevUsers.filter(user => user.id !== record.id));
          openUserNotification('delete', record.username);
        }
      })
      .catch(errorInfo => console.log('Error: ', errorInfo));
  };

  const handleOnUpdate = (record) => {
    const transformedRecord = {
      ...record,
      roles: transformRoles(record.roles)
    }
    setEditingUser(transformedRecord);
    form.setFieldsValue(transformedRecord);
    setIsModalOpen(true);
  };

  const getAllUsers = useCallback(() => {
    axios.get('/api/v1/users')
      .then(response => setUsers(response.data))
      .catch(error => console.log(error));
  }, [setUsers]);

  const isAdmin = auth?.roles.includes('ROLE_ADMIN');

  useEffect(() => {
    getAllUsers();
  }, [getAllUsers]);

  return (
    <>
      <MenuComponent currentPage="users"/>
        {contextHolder}
        {isAdmin ? (
          <div className="app-container">
            <Title>Lista de Usuários</Title>
            <Divider />
            <Flex gap="middle" vertical>
              <Button type="primary" onClick={showModal}>Adicionar novo usuário</Button>
              <Table dataSource={users && transformedUsers} columns={columns} />
            </Flex>
          <Modal
            title={editingUser ? 'Editar Função(ões) do Usuário' : 'Adicionar Usuário'}
            open={isModalOpen}
            okText={editingUser ? 'Salvar' : 'Adicionar'}
            okButtonProps={{
              autoFocus: true,
              htmlType: 'submit'
            }}
            cancelText="Cancelar"
            onCancel={() => {
              setIsModalOpen(false);
              form.resetFields();
            } }
            destroyOnClose
            modalRender={(dom) => (
              <Form
                name="form_modal"
                layout="vertical"
                form={form}
                labelCol={{
                  span: 7
                }}
                onFinish={(values) => handleOnFinish(values)}
                onFinishFailed={(errorInfo) => console.log('Failed: ', errorInfo)}
              >
                {dom}
              </Form>
            )}
          >
            {
              !editingUser && (
                <><Form.Item
                  label="Nome de Usuário"
                  name="username"
                  rules={[
                    {
                      required: true,
                      message: 'Por favor, insira seu nome de usuário!'
                    }
                  ]}
                >
                  <Input />
                </Form.Item>
                <Form.Item
                  label="Senha"
                  name="password"
                  rules={[{ required: true, message: 'Por favor, insira sua senha!' }]}
                >
                    <Input type="password" />
                  </Form.Item></>
              )
            }
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
            </Modal></div>
        ) : (
          <div className='users-unauthorized-container'>
            <StopOutlined style={{ fontSize: '8rem' }} />
            <Title>Usuário não autorizado</Title>
          </div>
        )}
    </>
  );
}

export default Users;
