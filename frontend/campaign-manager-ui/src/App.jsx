import React, { useState, useEffect } from 'react';
import { api } from './api/apiService';
import CampaignForm from './components/CampaignForm';
import CampaignTable from './components/CampaignTable';
import './index.css';

function App() {
  const [campaigns, setCampaigns] = useState([]);
  const [balance, setBalance] = useState(0);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [editingCampaign, setEditingCampaign] = useState(null);

  const refreshData = async () => {
    try {
      const campData = await api.getCampaigns(page);
      setCampaigns(campData.content || []);
      setTotalPages(campData.totalPages || 0);

      const balRes = await api.getBalance();
      const finalBalance = typeof balRes === 'object' ? balRes.balance : balRes;
      setBalance(finalBalance || 0);
    } catch (err) {
      console.error("Error fetching data:", err);
    }
  };

  useEffect(() => { refreshData(); }, [page]);

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this campaign?")) {
      await api.deleteCampaign(id);
      refreshData();
    }
  };

  return (
      <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '30px' }}>
        <header className="card" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' }}>
          <h1 style={{ margin: 0, fontSize: '24px', fontWeight: 700 }}>
            Campaign<span style={{color: 'var(--emerald-primary)'}}>Manager</span>
          </h1>
          <div style={{ fontSize: '18px', display: 'flex', alignItems: 'center', gap: '10px' }}>
            <span style={{color: 'var(--text-secondary)'}}>Emerald Balance:</span>
            <span style={{ fontWeight: 700, color: 'var(--emerald-light)', fontFamily: 'monospace', fontSize: '20px' }}>
            {Number(balance).toFixed(2)} PLN
          </span>
          </div>
        </header>

        <div style={{ display: 'flex', gap: '30px', alignItems: 'flex-start' }}>
          <div style={{ flex: '0 0 350px' }}>
            <CampaignForm onSuccess={refreshData} editData={editingCampaign} clearEdit={() => setEditingCampaign(null)} balance={balance} />
          </div>
          <div style={{ flex: 1 }}>
            <CampaignTable campaigns={campaigns} page={page} totalPages={totalPages} setPage={setPage} onEdit={setEditingCampaign} onDelete={handleDelete} />
          </div>
        </div>
      </div>
  );
}
export default App;