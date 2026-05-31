export interface SignupRequest {
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface ApiResponse<T = void> {
  success: "SUCCESS" | "ERROR";
  data?: T;
  message?: string;
}
