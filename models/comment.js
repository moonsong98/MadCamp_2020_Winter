const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const commentSchema = new Schema({
  userId: { type: Number, required: true },
  username: { type: String, required: true },
  body: { type: String, required: true },
  date: { type: Date, defualt: Date.now() },
});

module.exports = mongoose.model("comment", commentSchema);
