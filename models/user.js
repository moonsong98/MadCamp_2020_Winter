const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const userSchema = new Schema({
  username: { type: String, required: true },
  password: { type: String, required: true },
  nickname: { type: String },
  role: { type: String, required: true },
  // admin, user, restaurantOwner
  isInitialPassword: { type: Boolean },
  confirmed: { type: Boolean },
  emailVerifyKey: { type: String },
  ownedRestaurant: { type: Schema.Types.ObjectId, ref: "restaurant" },
  location: {
    lat: Number,
    lng: Number,
    address: String,
  },
  openingHours: {
    open: String,
    close: String,
  },
});

module.exports = mongoose.model("user", userSchema);
