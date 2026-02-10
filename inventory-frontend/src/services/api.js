import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080', 
} );

export const productService = {
  getAll: () => api.get('/products'),
  create: (data) => api.post('/products', data),
  update: (id, data) => api.put(`/products/${id}`, data), 
  remove: (id) => api.delete(`/products/${id}`),   
};

export const materialService = {
  getAll: () => api.get('/raw-materials'),
  create: (data) => api.post('/raw-materials', data),
  update: (id, data) => api.put(`/raw-materials/${id}`, data), 
  remove: (id) => api.delete(`/raw-materials/${id}`), 
};

export const productRawMaterialService = {
  getByProductId: (productId) => api.get(`/product-raw-materials/product/${productId}`),
  create: (data) => api.post('/product-raw-materials', data),
  remove: (id) => api.delete(`/product-raw-materials/${id}`),
};

export const optimizationService = {
  getSuggestion: () => api.get('/optimization/suggestion'),
};

export default api;
