const express = require("express");
const router = express.Router();

const restrCtrl = require("./restaurant.ctrl");
const auth = require("../../middlewares/auth");

router.get("/", restrCtrl.getRestaurants);
router.get("/:restr_id", restrCtrl.getRestaurant);
router.get("/category/:category_id", restrCtrl.getRestaurantsInCategory);

router.post("/", auth.verifyToken, restrCtrl.createRestaurant);
router.put("/:restr_id", restrCtrl.updateRestaurant);
router.delete("/:restr_id", restrCtrl.deleteRestaurant);

module.exports = router;
