import React, { useState, useEffect } from 'react';
import CreatableSelect from 'react-select/creatable';
import { api } from '../api/apiService';
import { KEYWORD_OPTIONS, CITY_OPTIONS, customSelectStyles } from '../utils/constans.js';

function CampaignForm({ onSuccess, editData, clearEdit, balance }) {
    const emptyForm = { campaignName: '', keywords: [], bidAmount: 10, campaignFund: 100, status: 'ON', town: 'Kraków', radius: 10 };

    const [formData, setFormData] = useState(emptyForm);
    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (editData) setFormData({ ...editData, keywords: editData.keywords || [] });
    }, [editData]);

    const handleCancel = () => {
        setFormData(emptyForm);
        setErrors({});
        clearEdit();
    };

    const submit = async (e) => {
        e.preventDefault();
        setErrors({});

        const isNewCampaign = !editData;
        if (isNewCampaign && balance <= 0) {
            alert("You cannot start a new campaign. Your account balance is 0 PLN.");
            return;
        }

        try {
            const res = editData
                ? await api.updateCampaign(editData.id, formData)
                : await api.createCampaign(formData);

            if (res.ok) {
                setFormData(emptyForm);
                if (editData) clearEdit();
                onSuccess();
            } else {
                const errorData = await res.json();
                setErrors(errorData);
            }
        } catch (error) {
            console.error("Error while sending form", error);
        }
    };

    const isSubmitDisabled = !editData && balance <= 0;

    return (
        <form onSubmit={submit} className="card" style={{ display: 'flex', flexDirection: 'column' }}>
            <h3 style={{ margin: '0 0 20px 0', textAlign: 'center', fontSize: '18px', color: editData ? 'var(--emerald-light)' : 'white' }}>
                {editData ? 'Edit Campaign Details' : 'Launch New Campaign'}
            </h3>

            <div className="form-group">
                <label className="form-label">Campaign Name</label>
                <input className="form-input" value={formData.campaignName}
                       onChange={e => setFormData({...formData, campaignName: e.target.value})} />
                {errors.campaignName && <small style={{ color: 'var(--danger)' }}>{errors.campaignName}</small>}
            </div>

            <div className="form-group">
                <label className="form-label">Keywords</label>
                <CreatableSelect
                    isMulti
                    options={KEYWORD_OPTIONS}
                    placeholder="Choose or write your own..."
                    styles={customSelectStyles}
                    value={formData.keywords.map(k => ({ value: k, label: k }))}
                    onChange={(selectedItems) => {
                        const newKeywords = selectedItems ? selectedItems.map(item => item.value) : [];
                        setFormData({ ...formData, keywords: newKeywords });
                    }}
                />
            </div>

            <div style={{ display: 'flex', gap: '15px' }}>
                <div className="form-group" style={{ flex: 1 }}>
                    <label className="form-label">Bid (PLN)</label>
                    <input type="number" className="form-input" value={formData.bidAmount}
                           onChange={e => setFormData({...formData, bidAmount: e.target.value})} />
                    {errors.bidAmount && <small style={{ color: 'var(--danger)' }}>{errors.bidAmount}</small>}
                </div>
                <div className="form-group" style={{ flex: 1 }}>
                    <label className="form-label">Fund (PLN)</label>
                    <input type="number" className="form-input" value={formData.campaignFund}
                           onChange={e => setFormData({...formData, campaignFund: e.target.value})} />
                    {errors.campaignFund && <small style={{ color: 'var(--danger)' }}>{errors.campaignFund}</small>}
                </div>
            </div>

            <div className="form-group">
                <label className="form-label">Status</label>
                <select className="form-input" style={{ cursor: 'pointer' }} value={formData.status}
                        onChange={e => setFormData({...formData, status: e.target.value})}>
                    <option value="ON">ON</option>
                    <option value="OFF">OFF</option>
                </select>
            </div>

            <div className="form-group">
                <label className="form-label">Town</label>
                <CreatableSelect
                    isClearable
                    options={CITY_OPTIONS}
                    placeholder="Choose Town..."
                    styles={customSelectStyles}
                    value={formData.town ? { value: formData.town, label: formData.town } : null}
                    onChange={(selected) => setFormData({ ...formData, town: selected ? selected.value : '' })}
                />
            </div>

            <div className="form-group">
                <label className="form-label">Radius (km)</label>
                <input type="number" className="form-input" value={formData.radius}
                       onChange={e => setFormData({...formData, radius: e.target.value})} />
                {errors.radius && <small style={{ color: 'var(--danger)' }}>{errors.radius}</small>}
            </div>

            <button
                type="submit"
                className="btn"
                disabled={isSubmitDisabled}
                style={{
                    background: isSubmitDisabled ? '#6b7280' : 'var(--emerald-primary)',
                    color: 'white',
                    marginTop: '10px',
                    cursor: isSubmitDisabled ? 'not-allowed' : 'pointer'
                }}>
                {editData ? 'Save Changes' : 'Launch Campaign'}
            </button>

            {editData && (
                <button type="button" className="btn" onClick={handleCancel}
                        style={{ background: '#4b5563', color: 'white', marginTop: '10px' }}>
                    Cancel
                </button>
            )}
        </form>
    );
}

export default CampaignForm;