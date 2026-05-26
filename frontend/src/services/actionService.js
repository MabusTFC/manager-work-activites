const API_BASE_URL = '/api/intervals';

async function parseError(response) {
  const body = await response.json().catch(() => ({}));
  const error = new Error(body.message || 'Ошибка запроса');
  // Бэкенд возвращает 409 Conflict при пересечении интервалов
  error.isOverlap = response.status === 409;
  error.status = response.status;
  return error;
}

export const actionService = {
  async getAllActions() {
    const response = await fetch(API_BASE_URL);
    if (!response.ok) {
      throw await parseError(response);
    }
    return response.json();
  },

  async createAction({ type, startTime, endTime, description }) {
    const response = await fetch(API_BASE_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ type, startTime, endTime, description }),
    });
    if (!response.ok) {
      throw await parseError(response);
    }
    return response.json();
  },
};
