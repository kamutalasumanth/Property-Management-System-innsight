import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { isOwner, isCustomer } from "../utils/roles";
import { getAllProperties } from "../api/propertyApi";
import "./PropertyList.css";

export default function PropertyList() {
  const [properties, setProperties] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    getAllProperties()
      .then((res) => setProperties(res.data))
      .catch(() => alert("Failed to load properties"))
      .finally(() => setIsLoading(false));
  }, []);

  return (
    <div className="list-container">
      
      <div className="list-header">
        <div className="list-title-block">
          <h1>Available Properties</h1>
          <p>
            {isOwner() ? "Manage your listings and keep track of incoming offers." : "Browse and reserve beautiful properties tailored exactly for your needs."}
          </p>
        </div>

        <div className="list-actions">
          {isOwner() && (
            <button
              onClick={() => navigate("/properties/create")}
              className="action-btn owner-btn"
            >
               Create Listing
            </button>
          )}

          {isCustomer() && (
             <button
              onClick={() => navigate("/favorites")}
              className="action-btn customer-btn"
            >
              My Favorites
            </button>
          )}
        </div>
      </div>

      {isLoading ? (
        <div className="loader-container">
          <div className="spinner"></div>
        </div>
      ) : properties.length === 0 ? (
        <div className="empty-state">
          <h3>No properties found</h3>
          <p>
            {isOwner() ? "Get started by creating a new listing today." : "Check back later for new arrivals and availability."}
          </p>
        </div>
      ) : (
        <div className="property-grid">
          {properties.map((p) => (
            <div key={p.id} className="property-card">
              
              <div className="property-image-wrapper">
                {p.imageUrl ? (
                  <img
                    src={`http://localhost:8080${p.imageUrl}`}
                    alt={p.title}
                    className="property-image"
                  />
                ) : (
                  <div className="property-image-placeholder">
                     No Image
                  </div>
                )}
                <div className="property-badge">
                  {p.listingType}
                </div>
              </div>

              <div className="property-body">
                <div className="property-info-header">
                  <h3 className="property-title">{p.title}</h3>
                  <p className="property-price">₹{p.price}</p>
                </div>
                
                <p className="property-city">
                   City: {p.city}
                </p>

                <div className="property-footer">
                   <button
                    onClick={() => navigate(`/properties/${p.id}`)}
                    className="details-btn"
                  >
                    View Details
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
