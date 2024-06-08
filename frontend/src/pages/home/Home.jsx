import { Flex } from 'antd';
import Menu from '../../components/menu/Menu';
import Title from 'antd/es/typography/Title';
import './Home.css';
import Paragraph from 'antd/es/typography/Paragraph';

const Home = () => {
  return (
    <>
      <Menu currentPage="home" />
      <Flex className="home-container" vertical>
        <Title>Sobre o projeto</Title>
        <Paragraph className="home-paragraph">
					O Sistema de Gerenciamento de Fornecedores é uma aplicação desenvolvida como parte do desafio proposto pela Insight Lab. Este sistema oferece uma plataforma robusta e intuitiva para o gerenciamento completo do ciclo de vida dos fornecedores de uma organização.
        </Paragraph>

        <Title level={2}>Principais Recursos</Title>
        <ul className="home-list">
          <li>
            <Paragraph className="home-paragraph">
              <strong>Cadastro de Fornecedores:</strong> Os usuários podem adicionar novos fornecedores ao sistema, inserindo  detalhes como nome, CNPJ/CPF, telefone, e-mail, entre outros dados relevantes.
            </Paragraph>
          </li>
          <li>
            <Paragraph className="home-paragraph">
              <strong>Visualização e Edição de Fornecedores:</strong> O sistema permite visualizar uma lista completa de todos os fornecedores  cadastrados, além de oferecer a funcionalidade de editar as informações de cada fornecedor necessário.
            </Paragraph>
          </li>
          <li>
            <Paragraph className="home-paragraph">
              <strong>Exclusão de Fornecedores:</strong> Os usuários têm a capacidade de excluir fornecedores do sistema, garantindo uma gestão eficiente da base de dados e a remoção de fornecedores não mais relevantes para a organização.
            </Paragraph>
          </li>
          <li>
            <Paragraph className="home-paragraph">
              <strong>Autenticação e Autorização:</strong>  A aplicação oferece recursos avançados de autenticação e autorização, garantindo que apenas usuários autorizados tenham acesso às funcionalidades de gerenciamento de fornecedores.
            </Paragraph>
          </li>
        </ul>

      </Flex>
    </>
  );
};

export default Home;
