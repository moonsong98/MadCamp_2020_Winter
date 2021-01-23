const express = require("express");
const router = express.Router();

const restrCtrl = require("./restaurant.ctrl");
const verifyToken = require("../../middlewares/verifyToken");

router.get("/", restrCtrl.getAllRestaurants);
router.get("/:restr_id", restrCtrl.getRestaurant);
router.get("/category/:category_id", restrCtrl.getRestaurantsInCategory);

router.post("/", restrCtrl.createRestaurant);
router.put("/:restr_id", restrCtrl.updateRestaurant);
router.delete("/:restr_id", restrCtrl.deleteRestaurant);

module.exports = router;
