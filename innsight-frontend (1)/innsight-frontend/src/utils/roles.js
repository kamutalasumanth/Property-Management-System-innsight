import { getUser } from "./auth";

export const getRole = () => {
  const user = getUser();
  return user ? user.role : null;
};

export const isAdmin = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  return user?.role === "ROLE_ADMIN";
};
export const isOwner = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  return user?.role === "ROLE_OWNER";
};
export const isCustomer = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  return user?.role === "ROLE_CUSTOMER";
};
