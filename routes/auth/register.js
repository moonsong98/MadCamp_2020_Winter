const { User, RestaurantOwner, Customer } = require("../../models/user");
const bcrypt = require("bcrypt");
const { v4: uuidv4 } = require("uuid");
const auth = require("../../middlewares/auth");

exports.customerRegister = async (req, res) => {
  const { username, password, nickname, role } = req.body;

  const idExist = await User.findOne({ username: username });
  if (idExist) return res.status(400).send("ID already exists");

  const salt = await bcrypt.genSalt(10);
  const hashedPassword = await bcrypt.hash(password, salt);

  try {
    const user = new Customer({
      username: username,
      password: hashedPassword,
      role: role,

      nickname: nickname,
      confirmed: false,
      emailVerifyKey: uuidv4(),
    });

    const savedUser = await user.save();
    res.json({
      _id: savedUser._id,
      username: savedUser.username,
      role: savedUser.role,
    });

    auth.sendVerifyEmail(user);
  } catch (error) {
    console.log(error);
    res.status(400).json({ message: "Register failed" });
  }
};

exports.restrOwnerRegister = async (req, res) => {
  const { username, password, role } = req.body;

  const idExist = await User.findOne({ username: username });
  if (idExist) return res.status(400).send("ID already exists");

  const salt = await bcrypt.genSalt(10);
  const hashedPassword = await bcrypt.hash(password, salt);

  try {
    const user = new RestaurantOwner({
      username: username,
      password: hashedPassword,
      role: role,

      isInitialPassword: true,
    });

    const savedUser = await user.save();
    res.json({
      _id: savedUser._id,
      username: savedUser.username,
      role: savedUser.role,
    });
  } catch (error) {
    console.log(error);
    res.status(400).json({ message: "Register failed" });
  }
};
