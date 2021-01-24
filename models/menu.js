const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const sizeSchema = new Schema({
  size: String,
  price: { type: Number, required: true },
});

const menuSchema = new Schema({
  name: { type: String, required: true },
  description: { type: String },
  sizes: [
    {
      type: sizeSchema,
      required: true,
    },
  ],
});

module.exports = mongoose.model("Menu", menuSchema);
