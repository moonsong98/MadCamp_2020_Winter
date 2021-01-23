const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const userSchema = new Schema({
  userId: { type: String, required: true },
  password: { type: String, required: true },
  username: { type: String },
  role: { type: String, required: true },
  // admin, user, restaurantOwner
});

module.exports = mongoose.model("user", userSchema);
