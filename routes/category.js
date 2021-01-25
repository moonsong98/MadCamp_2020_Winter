var express = require("express");
var router = express.Router();
const Category = require("../models/category");

/* GET home page. */
router.get("/", async (req, res) => {
  try {
    const categories = await Category.find({}, "-__v").exec();
    res.status(200).json(categories);
  } catch (error) {
    res.status(400).json({ message: "Invalid request" });
  }
});

module.exports = router;
