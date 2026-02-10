import React, { useState, useEffect } from 'react';
import Layout from './components/Layout';
import Materials from './components/Materials'; // Importando o componente Materials
import Products from './components/Products';   // Importando o componente Products
import { optimizationService } from './services/api';

function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [suggestion, setSuggestion] = useState(null);
  const [loading, setLoading] = useState(false);

  // Função para buscar a sugestão do Simplex no Backend
  const fetchSuggestion = async () => {
    setLoading(true);
    try {
      const response = await optimizationService.getSuggestion();
      setSuggestion(response.data);
    } catch (error) {
      console.error("Erro ao buscar sugestão:", error);
      // Adicionado para exibir o erro no frontend também
      alert("Erro ao buscar sugestão de produção. Verifique o console e o backend.");
    } finally {
      setLoading(false);
    }
  };

  // Recarrega a sugestão sempre que voltamos para a aba Dashboard
  useEffect(() => {
    if (activeTab === 'dashboard') {
      fetchSuggestion();
    }
  }, [activeTab]);

  return (
    <Layout activeTab={activeTab} setActiveTab={setActiveTab}>
      <div className="max-w-6xl mx-auto">
        
        {/* TELA: DASHBOARD (SUGESTÃO DE PRODUÇÃO) */}
        {activeTab === 'dashboard' && (
          <div className="bg-white p-6 rounded-xl shadow-md border border-gray-100">
            <h1 className="text-2xl font-bold text-pro-blue mb-4">Painel de Otimização</h1>
            <h2 className="text-lg font-medium text-gray-700 mb-4">Sugestão de Produção Ótima</h2>
            
            {loading ? (
              <p className="text-blue-500 animate-pulse font-medium">Calculando melhor estratégia com Simplex...</p>
            ) : (suggestion && Object.keys(suggestion.quantities).length > 0) ? (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {Object.entries(suggestion.quantities).map(([productId, quantity]) => (
                  <div key={productId} className="p-4 bg-pro-gray rounded-lg border-l-4 border-pro-orange shadow-sm">
                    <p className="text-xs uppercase tracking-wider text-gray-500 font-bold">Produto ID: {productId}</p>
                    <p className="text-3xl font-black text-pro-blue mt-1">{quantity} <span className="text-sm font-normal">unidades</span></p>
                  </div>
                ))}
                <div className="p-4 bg-green-100 rounded-lg border-l-4 border-green-500 shadow-sm">
                  <p className="text-xs uppercase tracking-wider text-green-700 font-bold">Valor Total da Venda</p>
                  <p className="text-3xl font-black text-green-800 mt-1">
                    R$ {suggestion.totalValue.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </p>
                </div>
              </div>
            ) : (
              <div className="p-8 text-center border-2 border-dashed border-gray-200 rounded-lg">
                <p className="text-gray-400">Nenhuma sugestão disponível no momento.</p>
                <p className="text-sm text-gray-400">Certifique-se de que há produtos, matérias-primas e associações cadastradas.</p>
              </div>
            )}
            
            <button 
              onClick={fetchSuggestion}
              className="mt-8 px-6 py-3 bg-pro-blue text-white rounded-lg hover:bg-blue-800 transition-all shadow-lg font-bold flex items-center gap-2"
            >
              Recalcular Produção
            </button>
          </div>
        )}

        {/* TELA: MATÉRIAS-PRIMAS */}
        {activeTab === 'materials' && <Materials />}

        {/* TELA: PRODUTOS */}
        {activeTab === 'products' && <Products />}

      </div>
    </Layout>
  );
}

export default App;
