const express = require("express");
const router = express.Router();

const restrCtrl = require("./restaurant.ctrl");
const verifyToken = require("../../middlewares/verifyToken");

router.post("/", restrCtrl.createRestaurant);
router.get("/:restr_id", restrCtrl.getRestaurant);
router.put("/:restr_id", restrCtrl.updateRestaurant);
router.delete("/:restr_id", restrCtrl.deleteRestaurant);

router.get(
  "/category/:category_id",
  verifyToken,
  restrCtrl.getRestaurantsInCategory
);

module.exports = router;
