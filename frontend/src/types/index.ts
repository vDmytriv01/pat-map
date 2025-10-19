export interface AuthResponse {
  token: string;
}

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface RegisterPayload extends LoginCredentials {
  email: string;
}

export interface Category {
  id: number;
  name: string;
  description?: string;
}

export interface Place {
  id: number;
  name: string;
  description?: string;
  latitude: number;
  longitude: number;
  categoryId: number;
  categoryName?: string;
}

export interface PlacePayload {
  name: string;
  description?: string;
  latitude: number;
  longitude: number;
  categoryId: number;
}

export interface CategoryPayload {
  name: string;
  description?: string;
}
