import api from "./axios";

export const addFavorite = (propertyId) =>
  api.post(`/favorites/${propertyId}`);

export const removeFavorite = (propertyId) =>
  api.delete(`/favorites/${propertyId}`);

export const getMyFavorites = () =>
  api.get("/favorites");
