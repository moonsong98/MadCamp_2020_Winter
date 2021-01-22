var express = require("express");
var router = express.Router();
const Restaurant = require("../models/restaurant");
const Menu = require("../models/menu");

/* CREATE new restaurant */
router.post("/", async (req, res, next) => {
  console.log("CREATE restaurant", req.body);
  try {
    const restaurant = new Restaurant(req.body);
    const menuList = req.body.menus;

    if (menuList) {
      const promises = menuList.map((element) => {
        const menu = new Menu(element);
        return menu.save();
      });

      const savedMenus = await Promise.all(promises);
      const menuIds = savedMenus.map((element) => element._id);
      restaurant.menus = menuIds;
    }

    console.log("Restaurant", restaurant);
    const output = await restaurant.save();
    res.status(200).json(output);
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: "Restaurant was ill-formed" });
  }
});

module.exports = router;
