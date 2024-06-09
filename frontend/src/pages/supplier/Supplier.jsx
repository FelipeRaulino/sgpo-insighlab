import { useCallback, useEffect, useState } from 'react';
import axios from '../../config/axiosConfig';
import ReactInputMask from 'react-input-mask';
import { Button, Divider, Flex, Form, Input, Modal, Popconfirm, Space, Table, notification } from 'antd';
import './Supplier.css';
import MenuComponent from '../../components/menu/Menu';
import Title from 'antd/es/typography/Title';
import { useAuth } from '../../context/AuthContext';

const Supplier = () => {
  const [suppliers, setSuppliers] = useState([]);

  const [form] = Form.useForm();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingSupplier, setEdittingSupplier] = useState(null);

  const [api, contextHolder] = notification.useNotification();

  const [taxIdMask, setTaxIdMask] = useState('999.999.999-99');

  const { auth } = useAuth();

  const openSupplierNotification = (operation, supplierName) => {
    if (operation === 'create'){
      api['success']({
        message: 'Adicionado com sucesso!',
        description: `O fornecedor(a) ${supplierName} foi adicionado com sucesso!`
      });
    }

    if (operation === 'update'){
      api['success']({
        message: 'Atualizado com sucesso!',
        description: `O fornecedor(a) ${supplierName} foi atualizado com sucesso!`
      });
    }

    if (operation === 'delete'){
      api['success']({
        message: 'Deletado com sucesso!',
        description: `O fornecedor(a) ${supplierName} foi deletado com sucesso!`
      });
    }
  };

  const columns = [
    {
      title: 'Nome',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'CNPJ/CPF',
      dataIndex: 'taxId',
      key: 'taxId',
      width: 200
    },
    {
      title: 'Telefone',
      dataIndex: 'phone',
      key: 'phone',
      width: 200
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Ação',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          <a onClick={() => handleOnUpdate(record)}>Editar</a>
          {isAdmin && (
            <Popconfirm
              title="Excluir fornecedor"
              description="Você tem certeza que deseja excluir fornecedor ?"
              onConfirm={() => handleOnDelete(record)}
              okText="Sim"
              cancelText="Não"
            >
              <Button>Excluir</Button>
            </Popconfirm>
          )}
        </Space>
      ),
    },
  ];

  const transformedSuppliers = suppliers.map(supplier => {
    return {
      ...supplier,
      key: supplier.id
    };
  });

  const showModal = () => {
    setEdittingSupplier(null);
    setIsModalOpen(true);
  };

  const handleOnFinish = (values) => {
    if (editingSupplier) {
      const edittedSupplier = {...values, status: true};
      axios.put(`api/v1/suppliers/${editingSupplier.id}`, edittedSupplier)
        .then(response => {
          setSuppliers(prevSuppliers => prevSuppliers.map(supplier =>
            supplier.id === editingSupplier.id ? response.data : supplier
          ));
          form.resetFields();
          openSupplierNotification('update', response.data.name);
          setIsModalOpen(false);
        })
        .catch(error => console.log('Error: ', error));

    } else {
      const newSupplier = {...values, status: true};

      axios.post('/api/v1/suppliers', newSupplier)
        .then(response => {
          setSuppliers(prevSuppliers => [...prevSuppliers, response.data]);
          openSupplierNotification('create', response.data.name);
          form.resetFields();
        })
        .catch(error => console.log(error));

      setIsModalOpen(false);
    }

  };

  const handleOnDelete = (record) => {
    axios.delete(`/api/v1/suppliers/${record.id}`)
      .then(response => {
        if(response.status === 204){
          setSuppliers(prevSuppliers => prevSuppliers.filter(supplier => supplier.id !== record.id));
          openSupplierNotification('delete', record.name);
        }
      })
      .catch(errorInfo => console.log('Error: ', errorInfo));
  };

  const handleOnUpdate = (record) => {
    setEdittingSupplier(record);
    form.setFieldsValue(record);
    setIsModalOpen(true);
  };

  const handleTaxIdChange = (e) => {
    const value = e.target.value.replace(/\D/g, '');
    setTaxIdMask(value.length > 11 ? '99.999.999/9999-99' : '999.999.999-99');
  };

  const getAllSupliers = useCallback(() => {
    axios.get('/api/v1/suppliers')
      .then(response => setSuppliers(response.data))
      .catch(error => console.log(error));
  }, [setSuppliers]);

  const isAdmin = auth?.roles.includes('ROLE_ADMIN');

  useEffect(() => {
    getAllSupliers();
  }, [getAllSupliers]);

  return (
    <>
      <MenuComponent currentPage="suppliers"/>
      <div className="app-container">
        {contextHolder}
        <Title>Lista de Fornecedores</Title>
        <Divider />
        <Flex gap="middle" vertical>
          <Button type="primary" onClick={showModal}>Adicionar novo fornecedor</Button>
          <Table dataSource={suppliers && transformedSuppliers} columns={columns} />
        </Flex>
        <Modal
          title={editingSupplier ? 'Editar Fornecedor' : 'Adicionar Fornecedor'}
          open={isModalOpen}
          okText={editingSupplier ? 'Salvar' : 'Adicionar'}
          okButtonProps={{
            autoFocus: true,
            htmlType: 'submit'
          }}
          cancelText="Cancelar"
          onCancel={() => {
            setIsModalOpen(false);
            form.resetFields();
          }}
          destroyOnClose
          modalRender={(dom) => (
            <Form
              name="form_modal"
              layout="vertical"
              form={form}
              labelCol={{
                span: 4
              }}
              onFinish={(values) => handleOnFinish(values)}
              onFinishFailed={(errorInfo) => console.log('Failed: ', errorInfo)}
            >
              {dom}
            </Form>
          )}
        >
          <Form.Item
            label="Nome"
            name="name"
            rules={[
              {
                required: true,
                message: 'Por favor, insira o nome do fornecedor!'
              }
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="CPF/CNPJ"
            name="taxId"
            rules={[
              {
                required: true,
                message: 'Por favor, insira o CPF/CNPJ do fornecedor!'
              }
            ]}
          >
            <ReactInputMask
              mask={taxIdMask}
              maskChar={null}
              onChange={handleTaxIdChange}
            >
              {(inputProps) => <Input maxLength="18" {...inputProps}/>}
            </ReactInputMask>
          </Form.Item>
          <Form.Item
            label="Telefone"
            name="phone"
            rules={[
              {
                required: true,
                message: 'Por favor, insira o telefone do fornecedor!'
              }
            ]}
          >
            <ReactInputMask
              mask="(99) 99999-9999"
              maskChar={null}
            >
              {(inputprops) => <Input maxLength="15" {...inputprops} />}
            </ReactInputMask>
          </Form.Item>
          <Form.Item
            label="Email"
            name="email"
            rules={[
              {
                required: true,
                message: 'Por favor, insira o email do fornecedor!'
              }
            ]}
          >
            <Input />
          </Form.Item>
        </Modal>
      </div>
    </>
  );
};

export default Supplier;
