import { Navigate } from "react-router-dom";
import { isOwner } from "../utils/roles";

export default function OwnerRoute({ children }) {
  if (!isOwner()) {
    return <Navigate to="/" replace />;
  }

  return children;
}
