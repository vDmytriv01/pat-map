import apiClient from './http';
import { AuthResponse, LoginCredentials, RegisterPayload } from '../types';

export const login = async (payload: LoginCredentials) => {
  const { data } = await apiClient.post<AuthResponse>('/auth/login', payload);
  return data;
};

export const register = async (payload: RegisterPayload) => {
  const { data } = await apiClient.post<AuthResponse>('/auth/register', payload);
  return data;
};
