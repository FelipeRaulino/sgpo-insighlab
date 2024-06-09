import axios from 'axios';

const instance = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
});

instance.interceptors.request.use(
  (config) => {
    const authData = JSON.parse(localStorage.getItem('authData'));

    if (authData && authData.token) {
      config.headers['Authorization'] = `Bearer ${authData.token}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default instance;
