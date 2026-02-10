import React, { useState, useEffect } from 'react';
import { materialService } from '../services/api';
import { Plus, Trash2, RefreshCw } from 'lucide-react';

// 1. Imports do Toastify adicionados
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Materials = () => {
  const [materials, setMaterials] = useState([]);
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({ name: '', quantityInStock: '' });

  // Função para buscar dados do Backend
  const loadMaterials = async () => {
    setLoading(true);
    try {
      const response = await materialService.getAll();
      setMaterials(response.data);
    } catch (error) {
      // Substituído alert por toast
      toast.error("Erro ao carregar matérias-primas. O backend está rodando?");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadMaterials();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Lógica de Criação
      await materialService.create({
        name: formData.name,
        quantityInStock: parseFloat(formData.quantityInStock)
      });
      
      setFormData({ name: '', quantityInStock: '' }); 
      loadMaterials(); 
      
      // 2. Toast de sucesso adicionado
      toast.success("Matéria-prima cadastrada com sucesso!");

    } catch (error) {
      // 3. Toast de erro adicionado
      console.error(error); // Bom para debug
      toast.error("Erro ao salvar matéria-prima.");
    }
  };

  return (
    <div className="space-y-6">
      {/* Formulário de Cadastro */}
      <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
        <h3 className="text-lg font-bold text-pro-blue mb-4 flex items-center gap-2">
          <Plus size={20} className="text-pro-orange" />
          Novo Insumo
        </h3>
        <form onSubmit={handleSubmit} className="flex flex-col md:flex-row gap-4">
          <input
            type="text"
            placeholder="Nome da Matéria-Prima (ex: Aço)"
            className="flex-1 p-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-pro-blue outline-none"
            value={formData.name}
            onChange={(e) => setFormData({...formData, name: e.target.value})}
            required
          />
          <input
            type="number"
            placeholder="Qtd em Estoque"
            className="w-full md:w-48 p-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-pro-blue outline-none"
            value={formData.quantityInStock}
            onChange={(e) => setFormData({...formData, quantityInStock: e.target.value})}
            required
          />
          <button type="submit" className="bg-pro-orange text-white px-6 py-2 rounded-md hover:bg-orange-600 transition-colors font-medium">
            Cadastrar
          </button>
        </form>
      </div>

      {/* Tabela de Listagem */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div className="p-4 border-b border-gray-100 flex justify-between items-center bg-gray-50">
          <h3 className="font-bold text-pro-blue text-lg">Estoque Atual</h3>
          <button onClick={loadMaterials} className="text-pro-blue hover:rotate-180 transition-transform duration-500">
            <RefreshCw size={20} />
          </button>
        </div>
        
        {loading ? (
          <div className="p-10 text-center text-gray-400">Carregando estoque...</div>
        ) : (
          <table className="w-full text-left">
            <thead className="bg-gray-50 text-pro-blue uppercase text-xs font-bold">
              <tr>
                <th className="p-4">ID</th>
                <th className="p-4">Nome</th>
                <th className="p-4">Quantidade</th>
                <th className="p-4 text-center">Ações</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {materials.map((m) => (
                <tr key={m.id} className="hover:bg-blue-50/30 transition-colors">
                  <td className="p-4 text-gray-500 font-mono">#{m.id}</td>
                  <td className="p-4 font-medium text-gray-800">{m.name}</td>
                  <td className="p-4">
                    <span className="bg-blue-100 text-pro-blue px-2 py-1 rounded text-sm font-bold">
                      {m.quantityInStock} un
                    </span>
                  </td>
                  <td className="p-4 text-center">
                    <button className="text-red-400 hover:text-red-600 transition-colors">
                      <Trash2 size={18} />
                    </button>
                  </td>
                </tr>
              ))}
              {materials.length === 0 && (
                <tr>
                  <td colSpan="4" className="p-10 text-center text-gray-400 italic">
                    Nenhum insumo cadastrado no banco de dados.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        )}
      </div>

      {/* 4. Container do Toastify adicionado aqui */}
      <ToastContainer position="bottom-right" />
    </div>
  );
};

export default Materials;