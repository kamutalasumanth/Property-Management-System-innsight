import { useState } from "react";
import { createProperty } from "../api/propertyApi";
import { useNavigate } from "react-router-dom";

export default function CreateProperty() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    title: "",
    description: "",
    price: "",
    city: "",
    listingType: "RENT",
    imageUrl: ""
  });

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await createProperty(form);
    navigate("/");
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Create Property</h2>

      <input
        name="title"
        placeholder="Title"
        value={form.title}
        onChange={handleChange}
        required
      />

      <textarea
        name="description"
        placeholder="Description"
        value={form.description}
        onChange={handleChange}
      />

      <input
        name="price"
        type="number"
        placeholder="Price"
        value={form.price}
        onChange={handleChange}
        required
      />

      <input
        name="city"
        placeholder="City"
        value={form.city}
        onChange={handleChange}
        required
      />

      <select
        name="listingType"
        value={form.listingType}
        onChange={handleChange}
      >
        <option value="RENT">Rent</option>
        <option value="SALE">Sale</option>
      </select>

      <input
        name="imageUrl"
        placeholder="Image URL (e.g. /images/sample.jpg)"
        value={form.imageUrl}
        onChange={handleChange}
      />

      <button type="submit">Create</button>
    </form>
  );
}
