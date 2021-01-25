const Restaurant = require("../../models/restaurant");
const Menu = require("../../models/menu");
const Category = require("../../models/category");

/* CREATE new restaurant */
exports.createRestaurant = async (req, res) => {
  console.log(req.user);
  const user = req.user;

  if (user.role !== "admin" && user.role !== "restaurantOwner") {
    return res.status(403).json({
      message: "Admin or Restaurant owner can create new restaurant info",
    });
  }

  console.log("CREATE restaurant:\n", req.body);
  const { category } = req.body;
  try {
    const categoryObject = await Category.findOne({ name: category });
    if (!categoryObject) {
      res.status(400);
      return res.json({ message: "Invalid category" });
    }

    const menuList = req.body.menus;
    let menuIds = [];
    if (menuList) {
      const promises = menuList.map((element) => {
        const menu = new Menu(element);
        return menu.save();
      });

      const savedMenus = await Promise.all(promises);
      menuIds = savedMenus.map((element) => element._id);
    }
    const restaurant = new Restaurant({
      ...req.body,
      category: categoryObject._id,
      menus: menuIds,
    });

    console.log("Restaurant", restaurant);
    const output = await restaurant.save();
    user.restaurant = output._id;
    console.log(user);
    user.save();

    res.status(200).json(output);
  } catch (error) {
    console.log(error);
    res.status(400).json({ message: "Restaurant was ill-formed" });
  }
};

// RETRIEVE single Restaurant info
exports.getRestaurant = async (req, res) => {
  const { restr_id } = req.params;
  console.log(restr_id);
  const restaurant = await Restaurant.findById(restr_id).populate("menus");

  console.log(restaurant);
  res.status(200).json(restaurant);
};

// RETRIEVE all restaurants info - ONLY allowed for admin
exports.getRestaurants = async (req, res) => {
  const user = req.user;
  const serachOption = {};
  if (req.query.category) {
    console.log("Category:", req.query.category);
    const categoryId = req.query.category;
    try {
      const category = await Category.findById(categoryId);
      console.log(category.name);
      serachOption.category = category._id;
    } catch (error) {
      console.log(error);
      return res.status(400).json("Category was ill-formed");
    }
  }

  const restaurants = await Restaurant.find(serachOption);
  console.log("Restaurants: ", restaurants.length);
  return res.status(200).json(restaurants);
};

exports.getRestaurantsInCategory = async (req, res) => {
  const categoryId = req.params.category_id;
  // category = category.substring(1, category.length - 1);
  console.log("Category:", categoryId);

  try {
    const category = await Category.findById(categoryId);
    console.log(category.name);
    const restaurants = await Restaurant.find(
      {
        category: category._id,
      },
      "-category"
    );

    console.log(restaurants.length);
    res.status(200).json(restaurants);
  } catch (error) {
    console.log(error);
    res.status(400).json("Category was ill-formed");
  }
};

// UPDATE restaurant info
exports.updateRestaurant = async (req, res) => {
  const { restr_id } = req.params;
  console.log("Update id:", restr_id);
  console.log("UPDATE restraurant:\n", req.body);

  try {
    const updated = await Restaurant.findByIdAndUpdate(restr_id, req.body, {
      new: true,
    }).exec();
    res.status(200).json(updated);
  } catch (error) {
    console.log(error);
    res.status(400).json({ message: "Update failed" });
  }
};

// DELETE restaurant info
exports.deleteRestaurant = async (req, res) => {
  const { restr_id } = req.params;
  console.log("Delete id:", restr_id);
  try {
    const restaurant = await Restaurant.findById(restr_id);
    if (!restaurant) {
      return res.status(404).json({ message: "Restaurant not found" });
    }
    const menus = restaurant.menus;

    await Menu.deleteMany().where("_id").in(menus).exec();
    await Restaurant.findByIdAndDelete(restr_id).exec();

    res.status(200).json({ message: "Delete sucess" });
  } catch (error) {
    console.log(error);
    res.status(400).json({ message: "Deleted failed" });
  }
};
