import type { ApiResponse, LoginRequest, SignupRequest } from "../types/auth";

const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

async function request<T>(
  endpoint: string,
  body: object,
): Promise<ApiResponse<T>> {
  const response = await fetch(`${BASE_URL}${endpoint}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || "An error occurred");
  }

  return response.json();
}

export function signup(data: SignupRequest) {
  return request("/api/auth/signup", data);
}

export function login(data: LoginRequest) {
  return request("/api/auth/login", data);
}
