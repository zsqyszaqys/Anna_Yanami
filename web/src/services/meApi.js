// 纯 JS 版 API 封装，兼容你的 ResponseResult { code, status, message, data }
const BASE = '/api/me';

async function request(url, init) {
    const res = await fetch(url, {
        credentials: 'include',
        headers: { 'Content-Type': 'application/json', ...(init && init.headers ? init.headers : {}) },
        ...(init || {}),
    });
    let json;
    try { json = await res.json(); } catch (e) {
        throw new Error(`网络错误 ${res.status}`);
    }
    if (!res.ok || json.code !== 200) {
        throw new Error(json.message || `请求失败(${res.status})`);
    }
    return json.data;
}

// Groups
export const getGroups = () => request(`${BASE}/groups`);
export const getGroup = (id) => request(`${BASE}/groups/${id}`);
export const createGroup = (payload) =>
    request(`${BASE}/groups`, { method: 'POST', body: JSON.stringify(payload) });
export const updateGroup = (id, payload) =>
    request(`${BASE}/groups/${id}`, { method: 'PATCH', body: JSON.stringify(payload) });
export const deleteGroup = (id) =>
    request(`${BASE}/groups/${id}`, { method: 'DELETE' });
export const restoreGroup = (id) =>
    request(`${BASE}/groups/${id}/restore`, { method: 'POST' });
export const reorderGroup = (id, orderIndex, pinned) =>
    request(`${BASE}/groups/${id}/reorder?orderIndex=${orderIndex}&pinned=${pinned}`, { method: 'PATCH' });

// Links
export const pageLinks = (groupId, q = '', pageNo = 1, pageSize = 24, sort = 'default') =>
    request(`${BASE}/groups/${groupId}/links?pageNo=${pageNo}&pageSize=${pageSize}&sort=${sort}&q=${encodeURIComponent(q)}`);

export const createLink = (payload) =>
    request(`${BASE}/links`, { method: 'POST', body: JSON.stringify(payload) });

export const updateLink = (id, payload) =>
    request(`${BASE}/links/${id}`, { method: 'PATCH', body: JSON.stringify(payload) });

export const deleteLink = (id) =>
    request(`${BASE}/links/${id}`, { method: 'DELETE' });

export const restoreLink = (id) =>
    request(`${BASE}/links/${id}/restore`, { method: 'POST' });

export const moveLink = (id, toGroupId) =>
    request(`${BASE}/links/${id}/move?toGroupId=${toGroupId}`, { method: 'POST' });

export const favoriteLink = (id, fav) =>
    request(`${BASE}/links/${id}/favorite?fav=${fav}`, { method: 'POST' });

export const clickLink = (id) =>
    request(`${BASE}/links/${id}/click`, { method: 'POST' });