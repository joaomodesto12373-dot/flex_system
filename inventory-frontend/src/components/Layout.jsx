import React from 'react';
import { LayoutDashboard, Package, Beaker, Menu } from 'lucide-react';

const Layout = ({ children, activeTab, setActiveTab }) => {
  const menuItems = [
    { id: 'dashboard', label: 'Sugestão de Produção', icon: <LayoutDashboard size={20} /> },
    { id: 'products', label: 'Produtos', icon: <Package size={20} /> },
    { id: 'materials', label: 'Matérias-Primas', icon: <Beaker size={20} /> },
  ];

  return (
    <div className="flex h-screen bg-pro-gray">
      {/* Sidebar */}
      <div className="w-64 bg-pro-blue text-white flex flex-col">
        <div className="p-6 text-2xl font-bold border-b border-blue-900 flex items-center gap-2">
          <div className="w-8 h-8 bg-pro-orange rounded-md"></div>
          Inventory
        </div>
        <nav className="flex-1 p-4 space-y-2">
          {menuItems.map((item) => (
            <button
              key={item.id}
              onClick={() => setActiveTab(item.id)}
              className={`w-full flex items-center gap-3 p-3 rounded-lg transition-colors ${
                activeTab === item.id 
                  ? 'bg-pro-orange text-white' 
                  : 'hover:bg-blue-800 text-blue-100'
              }`}
            >
              {item.icon}
              <span className="font-medium">{item.label}</span>
            </button>
          ))}
        </nav>
        <div className="p-4 text-xs text-blue-300 border-t border-blue-900 text-center">
          Desafio Técnico - Projedata
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        <header className="bg-white shadow-sm p-4 flex justify-between items-center">
          <h2 className="text-xl font-semibold text-pro-blue">
            {menuItems.find(i => i.id === activeTab)?.label}
          </h2>
          <div className="text-sm text-gray-500">Usuário Junior</div>
        </header>
        <main className="flex-1 overflow-y-auto p-6">
          {children}
        </main>
      </div>
    </div>
  );
};

export default Layout;
