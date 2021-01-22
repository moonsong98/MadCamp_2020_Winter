const express = require("express");
const router = express.Router();

const restrCtrl = require("./restaurant.ctrl");

router.post("/", restrCtrl.createRestaurant);
router.get("/:restr_id", restrCtrl.getRestaurant);
router.put("/:restr_id", restrCtrl.updateRestaurant);
router.delete("/:restr_id", restrCtrl.deleteRestaurant);

router.get("/", restrCtrl.getRestaurantsInCategory);

module.exports = router;
