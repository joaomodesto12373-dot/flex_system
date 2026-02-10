import React, { useState, useEffect } from 'react';
import api, { productService, materialService, productRawMaterialService } from '../services/api';
import { Plus, Trash2, Package, ShoppingCart, RefreshCw, Layers, Pencil } from 'lucide-react';

// 1. Imports do Toastify
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Products = () => {
  const [products, setProducts] = useState([]);
  const [materials, setMaterials] = useState([]);
  const [loading, setLoading] = useState(false);
  
  const [formData, setFormData] = useState({ name: '', value: '', items: [] });
  const [selectedMat, setSelectedMat] = useState({ id: '', quantity: '' });
  const [editingProductId, setEditingProductId] = useState(null); 

  const loadData = async () => {
    setLoading(true);
    try {
      const [pRes, mRes] = await Promise.all([
        productService.getAll(),
        materialService.getAll()
      ]);
      setProducts(pRes.data);
      setMaterials(mRes.data);
    } catch (error) {
      toast.error("Erro ao carregar dados do backend.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const addItemToRecipe = () => {
    if (!selectedMat.id || !selectedMat.quantity) {
      toast.warn("Selecione um insumo e a quantidade.");
      return;
    }

    const materialExists = formData.items.find(item => item.id === parseInt(selectedMat.id));
    if (materialExists) {
      toast.warn("Este insumo já está na receita.");
      return;
    }

    const material = materials.find(m => m.id === parseInt(selectedMat.id));
    setFormData({
      ...formData,
      items: [...formData.items, { ...selectedMat, name: material.name, quantity: parseFloat(selectedMat.quantity) }]
    });
    
    setSelectedMat({ id: '', quantity: '' });
  };

  const removeItemFromRecipe = (index) => {
    const newItems = formData.items.filter((_, i) => i !== index);
    setFormData({ ...formData, items: newItems });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (formData.items.length === 0) {
      toast.warn("Um produto precisa de pelo menos um insumo na receita.");
      return;
    }

    try {
      setLoading(true);
      let productResponse;

      if (editingProductId) {
        productResponse = await productService.update(editingProductId, {
          name: formData.name,
          value: parseFloat(formData.value)
        });
        const existingAssociations = await productRawMaterialService.getByProductId(editingProductId);
        await Promise.all(existingAssociations.data.map(assoc => productRawMaterialService.remove(assoc.id)));

      } else {
        // novo produto
        productResponse = await productService.create({
          name: formData.name,
          value: parseFloat(formData.value)
        });
      }
      
      const currentProductId = editingProductId || productResponse.data.id;

      const associationPromises = formData.items.map(item => 
        productRawMaterialService.create({
          product: { id: currentProductId },
          rawMaterial: { id: parseInt(item.id) },
          quantityNeeded: parseFloat(item.quantity)
        })
      );

      await Promise.all(associationPromises);

      toast.success(editingProductId ? "Produto atualizado com sucesso!" : "Produto cadastrado com sucesso!");
      
      setFormData({ name: '', value: '', items: [] });
      setEditingProductId(null); 
      loadData();
    } catch (error) {
      console.error(error);
      toast.error("Erro ao salvar produto. Verifique o console.");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = async (product) => {
    setEditingProductId(product.id);
    setFormData({ name: product.name, value: product.value, items: [] });

    try {
      const associationsRes = await productRawMaterialService.getByProductId(product.id);
      const itemsForForm = associationsRes.data.map(assoc => ({
        id: assoc.rawMaterial.id,
        name: assoc.rawMaterial.name,
        quantity: assoc.quantityNeeded
      }));
      setFormData(prev => ({ ...prev, items: itemsForForm }));
    } catch (error) {
      console.error("Erro ao carregar associações para edição:", error);
      toast.error("Erro ao carregar insumos do produto para edição.");
    }
  };

  const handleDelete = async (id) => {
    // Mantive o window.confirm pois o toastify não é bloqueante por padrão
    if (window.confirm("Tem certeza que deseja excluir este produto e suas associações?")) {
      try {
        setLoading(true);
        const existingAssociations = await productRawMaterialService.getByProductId(id);
        await Promise.all(existingAssociations.data.map(assoc => productRawMaterialService.remove(assoc.id)));
        
        await productService.remove(id);
        toast.success("Produto excluído com sucesso!");
        loadData();
      } catch (error) {
        console.error(error);
        toast.error("Erro ao excluir produto. Verifique o console.");
      } finally {
        setLoading(false);
      }
    }
  };

  return (
    <div className="space-y-6">
      {/* FORMULÁRIO DE CADASTRO/EDIÇÃO */}
      <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
        <h3 className="text-lg font-bold text-pro-blue mb-4 flex items-center gap-2">
          {editingProductId ? <Pencil size={20} className="text-pro-orange" /> : <Package size={20} className="text-pro-orange" />}
          {editingProductId ? 'Editar Produto Final' : 'Novo Produto Final'}
        </h3>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <input
              type="text"
              placeholder="Nome do Produto (ex: Mesa de Aço)"
              className="p-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-pro-blue outline-none"
              value={formData.name}
              onChange={(e) => setFormData({...formData, name: e.target.value})}
              required
            />
            <input
              type="number"
              step="0.01"
              placeholder="Valor de Venda (R$)"
              className="p-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-pro-blue outline-none"
              value={formData.value}
              onChange={(e) => setFormData({...formData, value: e.target.value})}
              required
            />
          </div>

          {/* SEÇÃO DA RECEITA */}
          <div className="bg-gray-50 p-4 rounded-lg border border-dashed border-gray-300">
            <h4 className="text-sm font-bold text-pro-blue mb-3 flex items-center gap-2">
              <Layers size={16} /> Composição Técnica (Insumos)
            </h4>
            
            <div className="flex flex-col md:flex-row gap-2 mb-4">
              <select 
                className="flex-1 p-2 border border-gray-300 rounded-md outline-none"
                value={selectedMat.id}
                onChange={(e) => setSelectedMat({...selectedMat, id: e.target.value})}
              >
                <option value="">Selecione um Insumo...</option>
                {materials.map(m => (
                  <option key={m.id} value={m.id}>{m.name} (Disp: {m.quantityInStock})</option>
                ))}
              </select>
              
              <input
                type="number"
                placeholder="Qtd Necessária"
                className="w-full md:w-32 p-2 border border-gray-300 rounded-md outline-none"
                value={selectedMat.quantity}
                onChange={(e) => setSelectedMat({...selectedMat, quantity: e.target.value})}
              />
              
              <button 
                type="button"
                onClick={addItemToRecipe}
                className="bg-pro-blue text-white px-4 py-2 rounded-md hover:bg-blue-800 transition-colors"
              >
                <Plus size={20} />
              </button>
            </div>

            {/* LISTA DE INSUMOS ADICIONADOS */}
            <div className="space-y-2">
              {formData.items.length === 0 ? (
                <p className="text-xs text-gray-400 italic">Nenhum insumo adicionado à receita ainda.</p>
              ) : (
                formData.items.map((item, index) => (
                  <div key={index} className="flex justify-between items-center bg-white p-2 rounded border border-gray-100 shadow-sm">
                    <span className="text-sm text-gray-700">
                      <span className="font-bold text-pro-blue">{item.name}</span> — {item.quantity} unidades
                    </span>
                    <button 
                      type="button" 
                      onClick={() => removeItemFromRecipe(index)}
                      className="text-red-400 hover:text-red-600"
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>
                ))
              )}
            </div>
          </div>

          <div className="flex flex-col gap-2">
            <button 
                type="submit" 
                disabled={loading}
                className="w-full bg-pro-orange text-white py-3 rounded-md hover:bg-orange-600 transition-colors font-bold shadow-md disabled:bg-gray-300"
            >
                {editingProductId ? "Atualizar Produto" : "Finalizar Cadastro de Produto"}
            </button>
            
            {editingProductId && (
                <button 
                type="button" 
                onClick={() => { setEditingProductId(null); setFormData({ name: '', value: '', items: [] }); }}
                className="w-full bg-gray-300 text-gray-800 py-3 rounded-md hover:bg-gray-400 transition-colors font-bold shadow-md"
                >
                Cancelar Edição
                </button>
            )}
          </div>
        </form>
      </div>

      {/* LISTAGEM DE PRODUTOS */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div className="p-4 border-b border-gray-100 flex justify-between items-center bg-gray-50">
          <h3 className="font-bold text-pro-blue text-lg">Produtos Cadastrados</h3>
          <button onClick={loadData} className="text-pro-blue hover:rotate-180 transition-transform duration-500">
            <RefreshCw size={20} />
          </button>
        </div>
        
        {loading && products.length === 0 ? (
          <div className="p-10 text-center text-gray-400">Carregando catálogo...</div>
        ) : (
          <table className="w-full text-left">
            <thead className="bg-gray-50 text-pro-blue uppercase text-xs font-bold">
              <tr>
                <th className="p-4">ID</th>
                <th className="p-4">Nome</th>
                <th className="p-4">Valor</th>
                <th className="p-4 text-center">Ações</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {products.map((p) => (
                <tr key={p.id} className="hover:bg-blue-50/30 transition-colors">
                  <td className="p-4 text-gray-500 font-mono">#{p.id}</td>
                  <td className="p-4 font-medium text-gray-800">{p.name}</td>
                  <td className="p-4">
                    <span className="text-green-600 font-bold">
                      R$ {p.value.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                    </span>
                  </td>
                  <td className="p-4 text-center flex items-center justify-center gap-2">
                    <button 
                      onClick={() => handleEdit(p)}
                      className="text-blue-400 hover:text-blue-600 transition-colors"
                    >
                      <Pencil size={18} />
                    </button>
                    <button 
                      onClick={() => handleDelete(p.id)}
                      className="text-red-400 hover:text-red-600 transition-colors"
                    >
                      <Trash2 size={18} />
                    </button>
                  </td>
                </tr>
              ))}
              {products.length === 0 && (
                <tr>
                  <td colSpan="4" className="p-10 text-center text-gray-400 italic">
                    Nenhum produto cadastrado.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        )}
      </div>

      {/* 2. Container do Toastify adicionado no final */}
      <ToastContainer position="bottom-right" />
    </div>
  );
};

export default Products;