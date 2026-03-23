import { useEffect, useState } from "react";
import { getMyFavorites, removeFavorite } from "../api/favoriteApi";
import { useNavigate } from "react-router-dom";

export default function Favorites() {
  const [favorites, setFavorites] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    getMyFavorites()
      .then(res => setFavorites(res.data))
      .catch(() => alert("Failed to load favorites"));
  }, []);

  const handleRemove = async (id) => {
    await removeFavorite(id);
    setFavorites(favorites.filter(p => p.id !== id));
  };

  return (
    <div>
      <h2>My Favorites</h2>

      {favorites.length === 0 && <p>No favorites yet.</p>}

      <ul>
        {favorites.map(p => (
          <li key={p.id} style={{ marginBottom: "20px" }}>
            <h3>{p.title}</h3>

            <button onClick={() => navigate(`/properties/${p.id}`)}>
              View
            </button>

            <button onClick={() => handleRemove(p.id)}>
              Remove
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
