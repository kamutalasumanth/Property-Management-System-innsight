import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  getOwnerOffers,
  acceptOffer,
  rejectOffer
} from "../api/offerApi";
import "./OwnerOffers.css";

export default function OwnerOffers() {
  const [offers, setOffers] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    getOwnerOffers()
      .then((res) => {
        // Ensure newest offers map correctly from data structure
        setOffers(res.data || []);
      })
      .catch(() => alert("Failed to safely load submitted offers"))
      .finally(() => setIsLoading(false));
  }, []);

  const handleDecision = async (offerId, action) => {
    try {
      if (action === "accept") {
        await acceptOffer(offerId);
      } else {
        await rejectOffer(offerId);
      }

      // Update UI locally preventing a full refresh dependency
      setOffers((prev) =>
        prev.map((o) =>
          o.offerId === offerId
            ? { ...o, status: action === "accept" ? "ACCEPTED" : "REJECTED" }
            : o
        )
      );
    } catch {
      alert(`Failed to ${action} offer`);
    }
  };

  return (
    <div className="offers-container">
      <div className="offers-header">
        <div className="title-block">
          <button className="back-btn" onClick={() => navigate("/properties")}>
             &larr; Back to Properties
          </button>
          <h1>Received Offers</h1>
          <p>Review customer proposals and handle tenant negotiations actively.</p>
        </div>
      </div>

      {isLoading ? (
        <div className="loader-container">
           <div className="spinner"></div>
        </div>
      ) : offers.length === 0 ? (
        <div className="empty-state">
           <h3>No Offers Placed Yet</h3>
           <p>Your property listings are active. Offers will organically appear here once submitted.</p>
        </div>
      ) : (
        <div className="offers-grid">
          {offers.map((offer) => (
            <div key={offer.offerId} className="offer-card">
               
               <div className="offer-card-header">
                 <div className="offer-top-row">
                   <h3 className="offer-customer">{offer.customerName}</h3>
                   <span className={`offer-badge status-${offer.status.toLowerCase()}`}>
                     {offer.status}
                   </span>
                 </div>
                 <p className="offer-date">
                   Submitted on {new Date(offer.createdAt).toLocaleDateString()}
                 </p>
               </div>

               <div className="offer-card-body">
                 <div className="offer-detail-row">
                   <span className="label">Suggested Price</span>
                   <span className="value highlight">₹{offer.offeredAmount}</span>
                 </div>
                 <div className="offer-detail-row">
                   <span className="label">Target Property ID</span>
                   <span className="value">#{offer.propertyId}</span>
                 </div>
                 
                 <div className="offer-remarks-box">
                   <span className="label">Customer Remarks:</span>
                   <p className="remarks-text">{offer.remarks || "No additional comments provided."}</p>
                 </div>
               </div>

               <div className="offer-card-footer">
                  {offer.status === "PENDING" ? (
                    <div className="action-buttons">
                      <button className="btn-reject" onClick={() => handleDecision(offer.offerId, "reject")}>
                        Decline
                      </button>
                      <button className="btn-accept" onClick={() => handleDecision(offer.offerId, "accept")}>
                        Accept Offer
                      </button>
                    </div>
                  ) : (
                     <div className="resolved-status">
                       Decision conclusively handled.
                     </div>
                  )}
               </div>

            </div>
          ))}
        </div>
      )}
    </div>
  );
}
