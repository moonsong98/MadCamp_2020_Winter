var express = require("express");
var router = express.Router();
const Restaurant = require("../models/restaurant");

/* CREATE new restaurant */
router.post("/", async (req, res, next) => {
  console.log("CREATE restaurant", req.body);
  try {
    const restaurant = new Restaurant(req.body);

    const output = await restaurant.save();
    res.status(200).json(output);
  } catch (error) {
    console.log(error);
    res.status(500).send({ message: error });
  }
});

module.exports = router;
