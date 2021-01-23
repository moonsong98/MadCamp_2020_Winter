const mongoose = require("mongoose");
const Category = require("./category");
const Comment = require("./comment");
const Schema = mongoose.Schema;

const restaurantSchema = new Schema({
  name: { type: String, required: true },
  category: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "category",
    required: true,
  },
  description: { type: String },
  telephone: { type: String },
  menus: [{ type: mongoose.Schema.Types.ObjectId, ref: "menu" }],
  comment: [Comment.schema],
});

module.exports = mongoose.model("restaurant", restaurantSchema);
