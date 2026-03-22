import React from 'react';

function CampaignTable({ campaigns, page, totalPages, setPage, onEdit, onDelete }) {
    return (
        <div className="card">
            <table width="100%" style={{ borderCollapse: 'collapse', fontSize: '14px' }}>
                <thead>
                <tr style={{ color: 'var(--text-secondary)', textTransform: 'uppercase', fontSize: '12px', letterSpacing: '1px' }}>
                    <th style={{ textAlign: 'left', padding: '12px', borderBottom: '1px solid var(--input-border)' }}>Name</th>
                    <th style={{ textAlign: 'left', padding: '12px', borderBottom: '1px solid var(--input-border)' }}>Fund (PLN)</th>
                    <th style={{ textAlign: 'left', padding: '12px', borderBottom: '1px solid var(--input-border)' }}>Status</th>
                    <th style={{ textAlign: 'left', padding: '12px', borderBottom: '1px solid var(--input-border)' }}>Town</th>
                    <th style={{ textAlign: 'right', padding: '12px', borderBottom: '1px solid var(--input-border)' }}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {campaigns.map(c => (
                    <tr key={c.id} className="table-row-hover" style={{ transition: 'background-color 0.2s' }}>
                        <td style={{ padding: '12px', fontWeight: 600 }}>{c.campaignName}</td>
                        <td style={{ padding: '12px', fontFamily: 'monospace' }}>{c.campaignFund.toFixed(2)}</td>
                        <td style={{ padding: '12px' }}>
                <span style={{ padding: '4px 8px', borderRadius: '4px', background: c.status === 'ON' ? 'rgba(16, 185, 129, 0.15)' : 'var(--input-bg)', color: c.status === 'ON' ? 'var(--emerald-light)' : 'var(--text-secondary)', fontWeight: 'bold', fontSize: '11px' }}>
                  {c.status}
                </span>
                        </td>
                        <td style={{ padding: '12px', color: 'var(--text-secondary)' }}>{c.town}</td>
                        <td style={{ padding: '12px', textAlign: 'right' }}>
                            <button onClick={() => onEdit(c)} style={{ color: '#60a5fa', background: 'none', border: 'none', cursor: 'pointer', marginRight: '10px', fontWeight: 'bold' }}>Edit</button>
                            <button onClick={() => onDelete(c.id)} style={{ color: 'var(--danger)', background: 'none', border: 'none', cursor: 'pointer', fontWeight: 'bold' }}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '15px', marginTop: '20px', paddingTop: '15px', borderTop: '1px solid var(--input-border)' }}>
                <button className="btn" disabled={page === 0} onClick={() => setPage(page - 1)} style={{ background: 'var(--input-bg)', color: 'white' }}>Prev</button>
                <span style={{ fontSize: '14px', color: 'var(--text-secondary)' }}>
          Page <b style={{color: 'white'}}>{page + 1}</b> of <b style={{color: 'white'}}>{totalPages || 1}</b>
        </span>
                <button className="btn" disabled={page >= totalPages - 1} onClick={() => setPage(page + 1)} style={{ background: 'var(--input-bg)', color: 'white' }}>Next</button>
            </div>
        </div>
    );
}
export default CampaignTable;