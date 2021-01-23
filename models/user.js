const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const userSchema = new Schema({
  username: { type: String, required: true },
  password: { type: String, required: true },
  nickname: { type: String },
  role: { type: String, required: true },
  // admin, user, restaurantOwner
  isInitialPassword: { type: Boolean },
  ownedRestaurant: { type: Schema.Types.ObjectId, ref: "restaurant" },
});

module.exports = mongoose.model("user", userSchema);
