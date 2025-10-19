import apiClient from './http';
import { Category, CategoryPayload } from '../types';

export const fetchCategories = async () => {
  const { data } = await apiClient.get<Category[]>('/categories');
  return data;
};

export const createCategory = async (payload: CategoryPayload) => {
  const { data } = await apiClient.post<Category>('/categories', payload);
  return data;
};

export const updateCategory = async (id: number, payload: CategoryPayload) => {
  const { data } = await apiClient.put<Category>(`/categories/${id}`, payload);
  return data;
};

export const deleteCategory = async (id: number) => {
  await apiClient.delete(`/categories/${id}`);
};
