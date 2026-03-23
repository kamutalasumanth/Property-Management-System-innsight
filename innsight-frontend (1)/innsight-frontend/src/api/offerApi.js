import axios from "./axios";

/**
 * CUSTOMER: Create offer
 * POST /api/customer/offers
 */
export const createOffer = (data) => {
  return axios.post("/customer/offers", data);
};

/**
 * OWNER: Get offers for my properties
 * GET /api/owner/offers
 */
export const getOwnerOffers = () => {
  return axios.get("/owner/offers");
};

/**
 * OWNER: Get offers for a specific property
 */
export const getOffersForProperty = (propertyId) => {
  console.log(">> SENDING OWNER OFFERS REQUEST FOR:", propertyId);
  return axios.get(`/owner/offers/property/${propertyId}`);
};

/**
 * OWNER: Accept offer
 */
export const acceptOffer = (offerId) => {
  return axios.post(`/owner/offers/${offerId}/accept`);
};

/**
 * OWNER: Reject offer
 */
export const rejectOffer = (offerId) => {
  return axios.post(`/owner/offers/${offerId}/reject`);
};

export const getMyOfferStatus = (propertyId) =>
  axios.get(`/customer/offers/status/${propertyId}`);

