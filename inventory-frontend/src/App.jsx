import React, { useState, useEffect } from 'react';
import Layout from './components/Layout';
import { optimizationService } from './services/api';

function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [suggestion, setSuggestion] = useState(null);
  const [loading, setLoading] = useState(false);

  const fetchSuggestion = async () => {
    setLoading(true);
    try {
      const response = await optimizationService.getSuggestion();
      setSuggestion(response.data);
    } catch (error) {
      console.error("Erro ao buscar sugestão:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (activeTab === 'dashboard') {
      fetchSuggestion();
    }
  }, [activeTab]);

  return (
    <Layout activeTab={activeTab} setActiveTab={setActiveTab}>
      <div className="bg-white p-6 rounded-xl shadow-md border border-gray-100">
        <h1 className="text-2xl font-bold text-pro-blue mb-4">Painel de Controle</h1>
        
        {activeTab === 'dashboard' && (
          <div>
            <h2 className="text-lg font-medium text-gray-700 mb-4">Sugestão de Produção Ótima</h2>
            {loading ? (
              <p className="text-blue-500 animate-pulse">Calculando melhor estratégia...</p>
            ) : suggestion ? (
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {Object.entries(suggestion).map(([id, qty]) => (
                  <div key={id} className="p-4 bg-pro-gray rounded-lg border-l-4 border-pro-orange">
                    <p className="text-sm text-gray-500">Produto ID: {id}</p>
                    <p className="text-2xl font-bold text-pro-blue">{qty} unidades</p>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-gray-400">Nenhuma sugestão disponível. Verifique o estoque e os produtos.</p>
            )}
            <button 
              onClick={fetchSuggestion}
              className="mt-6 px-4 py-2 bg-pro-blue text-white rounded-md hover:bg-blue-800 transition-colors"
            >
              Recalcular
            </button>
          </div>
        )}

        {activeTab !== 'dashboard' && (
          <p className="text-gray-600">Área de {activeTab} em desenvolvimento...</p>
        )}
      </div>
    </Layout>
  );
}

export default App;
