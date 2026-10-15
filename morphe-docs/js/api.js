const API_BASE_URL = "https://morphe-api.onrender.com/api/";

export async function runCodeApi(code) {
  const response = await fetch(`${API_BASE_URL}morphe/run`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ code }),
  });

  if (!response.ok) {
    throw new Error(`Server responded with status ${response.status}`);
  }
  return await response.json();
}

export async function sendInputApi(sessionId, input) {
  const response = await fetch(`${API_BASE_URL}morphe/run/${sessionId}/input`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ input }),
  });

  if (!response.ok) {
    throw new Error(`Server responded with status ${response.status}`);
  }

  return await response.json();
}
