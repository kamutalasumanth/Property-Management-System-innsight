import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getPropertyById, updateProperty } from "../api/propertyApi";

export default function EditProperty() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState(null);

  useEffect(() => {
    getPropertyById(id).then(res => setForm(res.data));
  }, [id]);

  if (!form) return <p>Loading...</p>;

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = {
            title: form.title,
            description: form.description || "",
            price: Number(form.price),
            city: form.city,
            listingType: form.listingType,
            imageUrl: form.imageUrl || null
        };

        console.log("UPDATE PAYLOAD:", payload);

        try {
            await updateProperty(id, payload);
            navigate("/");
        } catch (err) {
            console.error("UPDATE FAILED:", err.response?.data || err);
            alert("Update failed. Check backend logs.");
        }
    };


  return (
    <form onSubmit={handleSubmit}>
      <h2>Edit Property</h2>

      <input name="title" value={form.title} onChange={handleChange} required />
      <textarea name="description" value={form.description} onChange={handleChange} />
      <input name="price" type="number" value={form.price} onChange={handleChange} required />
      <input name="city" value={form.city} onChange={handleChange} required />

      <select name="listingType" value={form.listingType} onChange={handleChange}>
        <option value="RENT">Rent</option>
        <option value="SALE">Sale</option>
      </select>

      <input
        name="imageUrl"
        value={form.imageUrl || ""}
        onChange={handleChange}
      />

      <button type="submit">Update</button>
    </form>
  );
}
