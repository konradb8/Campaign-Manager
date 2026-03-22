const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1';

export const api = {
    getCampaigns: (page = 0) =>
        fetch(`${API_URL}/campaigns?page=${page}&size=5`).then(res => res.json()),

    getBalance: () =>
        fetch(`${API_URL}/account-balance`).then(res => res.json()),

    createCampaign: (data) =>
        fetch(`${API_URL}/campaigns`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        }),

    updateCampaign: (id, data) =>
        fetch(`${API_URL}/campaigns/${id}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        }),

    deleteCampaign: (id) =>
        fetch(`${API_URL}/campaigns/${id}`, { method: 'DELETE' })
};