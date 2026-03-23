import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

import {
  getPropertyById,
  deleteProperty,
  getMyPropertyById
} from "../api/propertyApi";

import {
  getMyFavorites,
  addFavorite,
  removeFavorite
} from "../api/favoriteApi";

import { 
  createOffer, 
  getMyOfferStatus,
  getOffersForProperty,
  acceptOffer,
  rejectOffer
} from "../api/offerApi";
import { isOwner, isCustomer } from "../utils/roles";
import "./PropertyDetails.css";

export default function PropertyDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [property, setProperty] = useState(null);
  const [favorites, setFavorites] = useState([]);
  const [offerStatus, setOfferStatus] = useState(null);
  const [propertyOffers, setPropertyOffers] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  // 🔹 Offer state
  const [showOfferModal, setShowOfferModal] = useState(false);
  const [offerAmount, setOfferAmount] = useState("");
  const [offerRemarks, setOfferRemarks] = useState("");
  const [submittingOffer, setSubmittingOffer] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const propertyRes = isOwner()
          ? await getMyPropertyById(id)
          : await getPropertyById(id);

        setProperty(propertyRes.data);

        if (isOwner()) {
          try {
            console.log(">> FETCHING PROPERTY OFFERS FOR ID:", id);
            const offersRes = await getOffersForProperty(id);
            console.log("<< RECEIVED PROPERTY OFFERS DATA:", offersRes.data);
            setPropertyOffers(offersRes.data || []);
          } catch (err) {
            console.error("Failed to load property-specific offers", err);
          }
        }

        if (isCustomer()) {
          const favRes = await getMyFavorites();
          setFavorites(favRes.data);
          
          try {
            const statusRes = await getMyOfferStatus(id);
            setOfferStatus(statusRes.data); 
          } catch (err) {
             // 204 No Content means no offer
            if (err.response?.status === 204) {
              setOfferStatus(null);
            }
          }
        }
      } catch (err) {
        alert("Property not found");
        navigate("/properties");
      } finally {
         setIsLoading(false);
      }
    };

    fetchData();
  }, [id, navigate]);

  if (isLoading) {
    return (
      <div className="details-page-container">
        <div className="loader-container">
           <div className="spinner"></div>
        </div>
      </div>
    );
  }

  if (!property) return null;

  const isFavorited = isCustomer() && favorites.some((f) => f.id === property.id);

  const handleDelete = async () => {
    if (window.confirm("Are you sure you want to completely delete this property? This action cannot be undone.")) {
      await deleteProperty(property.id);
      navigate("/properties");
    }
  };

  const handleFavoriteToggle = async () => {
    if (isFavorited) {
      await removeFavorite(property.id);
      setFavorites(favorites.filter(f => f.id !== property.id));
    } else {
      await addFavorite(property.id);
      setFavorites([...favorites, property]);
    }
  };

  const submitOffer = async () => {
    if(!offerAmount) return alert("Please enter an offer amount.");
    
    // Explicit API Request Payload Definition
    const payload = {
      propertyId: property.id,
      offeredAmount: offerAmount,
      remarks: offerRemarks
    };
    
    console.log(">> SENDING OFFER PAYLOAD:", payload);
    
    try {
      setSubmittingOffer(true);
      const response = await createOffer(payload);
      
      console.log("<< SUCCESSFUL RESPONSE:", response.status, response.data);
      if (response.status === 200) {
        alert("Your offer has been successfully updated!");
      } else {
        alert("Offer submitted successfully!");
      }
      
      setOfferStatus("PENDING");
      setShowOfferModal(false);
      setOfferAmount("");
      setOfferRemarks("");
    } catch (err) {
      console.error("<< OFFER SUBMISSION ERROR:", err.response?.data || err.message);
      
      alert(err.response?.data?.message || "Failed to process your offer. Please thoroughly check your details.");
    } finally {
      setSubmittingOffer(false);
    }
  };

  const handleOwnerDecision = async (offerId, action) => {
    try {
      if (action === "accept") await acceptOffer(offerId);
      if (action === "reject") await rejectOffer(offerId);

      setPropertyOffers(prev => prev.map(o => 
        o.offerId === offerId 
          ? { ...o, status: action === "accept" ? "ACCEPTED" : "REJECTED" }
          : o
      ));
    } catch {
      alert(`Failed to ${action} offer`);
    }
  };

  return (
    <div className="details-page-container">
      <div className="details-page-header">
         <button className="back-btn" onClick={() => navigate("/properties")}>
           &larr; Back to Properties
         </button>
      </div>

      <div className="details-card">
        <div className="details-image-section">
          {property.imageUrl ? (
            <img
              src={`http://localhost:8080${property.imageUrl}`}
              alt={property.title}
              className="details-image"
            />
          ) : (
            <div className="details-image-placeholder">No Image Available</div>
          )}
          <div className="details-badge">{property.listingType}</div>
        </div>

        <div className="details-content">
          <div className="details-content-header">
            <h1 className="details-title">{property.title}</h1>
            <p className="details-price">₹{property.price}</p>
          </div>
          
          <div className="details-meta-row">
            <p className="details-city">
              <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path></svg>
              {property.city}
            </p>
            <p className="details-date">
              <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>
              Listed on {property.createdOn ? new Date(property.createdOn).toLocaleDateString() : "N/A"}
            </p>
          </div>

          <div className="details-description">
            <h3>About this Property</h3>
            <p>{property.description}</p>
          </div>

          <div className="details-actions">
            
            {/* OWNER ACTIONS */}
            {isOwner() && (
              <div className="action-group">
                <button className="btn-edit" onClick={() => navigate(`/properties/${property.id}/edit`)}>
                  Edit Listing
                </button>
                <button className="btn-delete" onClick={handleDelete}>
                  Remove Listing
                </button>
              </div>
            )}

            {/* CUSTOMER ACTIONS */}
            {isCustomer() && (
              <div className="action-group">
                <button 
                  className={`btn-favorite ${isFavorited ? 'active' : ''}`} 
                  onClick={handleFavoriteToggle}
                >
                  {isFavorited ? "♥ Remove Favorite" : "♡ Save to Favorites"}
                </button>

                {offerStatus ? (
                  <div className={`offer-status-badge status-${offerStatus.toLowerCase()}`}>
                    Offer {offerStatus}
                  </div>
                ) : (
                  <button className="btn-offer" onClick={() => setShowOfferModal(true)}>
                    Make an Offer
                  </button>
                )}
              </div>
            )}
          </div>
        </div>
      </div>

      {/* OFFER MODAL */}
      {showOfferModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3 className="modal-title">Submit an Offer</h3>
            <p className="modal-subtitle">Propose your price for "{property.title}"</p>
            
            <div className="modal-form">
              <div className="form-group">
                <label>Offer Amount (₹)</label>
                <input
                  type="number"
                  placeholder="e.g. 15000"
                  value={offerAmount}
                  onChange={(e) => setOfferAmount(e.target.value)}
                />
              </div>

              <div className="form-group">
                <label>Remarks / Message (Optional)</label>
                <textarea
                  placeholder="Any conditions or notes for the owner?"
                  value={offerRemarks}
                  rows="3"
                  onChange={(e) => setOfferRemarks(e.target.value)}
                />
              </div>
            </div>

            <div className="modal-actions">
              <button className="btn-cancel" onClick={() => setShowOfferModal(false)} disabled={submittingOffer}>
                Cancel
              </button>
              <button className="btn-submit" onClick={submitOffer} disabled={submittingOffer}>
                {submittingOffer ? "Sending..." : "Submit Offer"}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* 🔹 OWNER EXCLUSIVE PROPERTY OFFERS SECTION */}
      {isOwner() && (
        <div className="property-offers-section">
          <h2>Offers on this Property</h2>
          
          {propertyOffers.length === 0 ? (
            <div className="empty-state">
              <h3>No Offers Yet</h3>
              <p>When customers propose pricing against this listing, their submitted negotiations will securely populate here.</p>
            </div>
          ) : (
            <div className="offers-grid">
              {propertyOffers.map((offer) => (
                <div key={offer.offerId} className="offer-card">
                   <div className="offer-card-header">
                     <div className="offer-top-row">
                       <h3 className="offer-customer">{offer.customerName}</h3>
                       <span className={`offer-badge status-${offer.status.toLowerCase()}`}>
                         {offer.status}
                       </span>
                     </div>
                     <p className="offer-date">
                       Submitted {new Date(offer.createdAt).toLocaleDateString()}
                     </p>
                   </div>
    
                   <div className="offer-card-body">
                     <div className="offer-detail-row">
                       <span className="label">Amount</span>
                       <span className="value highlight">₹{offer.offeredAmount}</span>
                     </div>
                     <div className="offer-remarks-box">
                       <span className="label">Message:</span>
                       <p className="remarks-text">{offer.remarks || "No additional comments"}</p>
                     </div>
                   </div>
    
                   <div className="offer-card-footer">
                      {offer.status === "PENDING" ? (
                        <div className="action-buttons">
                          <button className="btn-reject" onClick={() => handleOwnerDecision(offer.offerId, "reject")}>
                            Decline
                          </button>
                          <button className="btn-accept" onClick={() => handleOwnerDecision(offer.offerId, "accept")}>
                            Accept
                          </button>
                        </div>
                      ) : (
                         <div className="resolved-status">
                           Resolved: {offer.status}
                         </div>
                      )}
                   </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

    </div>
  );
}
