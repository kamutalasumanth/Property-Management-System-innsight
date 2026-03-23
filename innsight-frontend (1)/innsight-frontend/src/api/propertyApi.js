import api from "./axios";

export const getAllProperties = () =>
  api.get("/public/properties");

export const getPropertyById = (id) =>
  api.get(`/public/properties/${id}`);

export const createProperty = (propertyData) =>
  api.post("/properties", propertyData);

export const updateProperty = (id, data) =>
  api.put(`/properties/${id}`, data);

export const deleteProperty = (id) =>
  api.delete(`/properties/${id}`);

export const getMyPropertyById = (id) =>
  api.get(`/properties/${id}`);


