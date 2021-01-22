const mongoose = require("mongoose");
const nano = require("nanoid");
const Schema = mongoose.Schema;

const menuSchema = new Schema({
  _id: { type: String, default: () => nano.nanoid(10) },
  name: { type: String, required: true },
  price: { type: Number, required: true },
  description: { type: String },
  size: { type: String },
});

module.exports = mongoose.model("menu", menuSchema);
