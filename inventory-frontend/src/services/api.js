import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080', // Endereço do seu Quarkus
} );

export const productService = {
  getAll: () => api.get('/products'),
  create: (data) => api.post('/products', data),
};

export const materialService = {
  getAll: () => api.get('/raw-materials'),
  create: (data) => api.post('/raw-materials', data),
};

export const optimizationService = {
  getSuggestion: () => api.get('/optimization/suggest'),
};

export default api;
