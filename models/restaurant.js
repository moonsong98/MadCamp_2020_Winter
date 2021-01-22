const mongoose = require("mongoose");
const Menu = require("./menu");
const Comment = require("./comment");
const Schema = mongoose.Schema;

const restaurantSchema = new Schema({
  name: { type: String, required: true },
  category: { type: String, required: true },
  description: { type: String },
  telephone: { type: String },
  menu: [{ type: String, ref: "Menu" }],
  comment: [Comment.schema],
});

module.exports = mongoose.model("restaurant", restaurantSchema);
