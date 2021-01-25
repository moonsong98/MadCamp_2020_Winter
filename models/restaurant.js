const mongoose = require("mongoose");
const Category = require("./category");
const Comment = require("./comment");
const Schema = mongoose.Schema;

const restaurantSchema = new Schema({
  name: { type: String, required: true },
  category: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "Category",
    required: true,
  },
  description: { type: String },
  telephone: { type: String },
  menus: [{ type: mongoose.Schema.Types.ObjectId, ref: "Menu" }],
  comment: [Comment.schema],
  confirmed: { type: Boolean, default: false },

  location: {
    fullAddress: String,
    extraAddress: String,
  },
  openingHours: [
    {
      openTime: String,
      closeTime: String,
    },
  ],
});

module.exports = mongoose.model("Restaurant", restaurantSchema);
