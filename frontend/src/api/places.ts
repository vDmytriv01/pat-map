import apiClient from './http';
import { Place, PlacePayload } from '../types';

export const fetchPlaces = async (categoryId?: number) => {
  const { data } = await apiClient.get<Place[]>('/places', {
    params: categoryId ? { categoryId } : undefined
  });
  return data;
};

export const createPlace = async (payload: PlacePayload) => {
  const { data } = await apiClient.post<Place>('/places', payload);
  return data;
};

export const updatePlace = async (id: number, payload: PlacePayload) => {
  const { data } = await apiClient.put<Place>(`/places/${id}`, payload);
  return data;
};

export const deletePlace = async (id: number) => {
  await apiClient.delete(`/places/${id}`);
};
