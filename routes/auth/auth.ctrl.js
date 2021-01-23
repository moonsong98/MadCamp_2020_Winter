const User = require("../../models/user");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");

const userRegister = async (req, res) => {};

exports.register = async (req, res) => {
  console.log(req.body);
  const { username, password, nickname, role } = req.body;
  if (role !== "admin" && role !== "restaurantOwner" && role !== "user") {
    return res.status(400).send("Invalid request");
  }
  //   if (username === "admin" || role === "admin") {
  // return res.status(400).send("Register as admin is not Allowed");
  //   }
  const idExist = await User.findOne({ username: username });
  if (idExist) return res.status(400).send("ID already exists");

  const salt = await bcrypt.genSalt(10);
  const hashedPassword = await bcrypt.hash(password, salt);

  try {
    const user = new User({
      username: username,
      password: hashedPassword,
      nickname: nickname,
      role: role,
    });
    if (role === "restaurantOwner") user.isInitialPassword = true;

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

exports.login = async (req, res) => {
  const { username, password } = req.body;
  console.log(req.body);

  try {
    const user = await User.findOne({ username: username });
    if (!user)
      return res.status(400).json({ message: "ID or password is wrong" });
    console.log(user);
    const validPW = await bcrypt.compare(password, user.password);
    if (!validPW)
      return res.status(400).json({ message: "ID or password is wrong" });

    // assign token
    const token = jwt.sign({ _id: user._id }, process.env.TOKEN_SECRET);
    console.log(token);
    res.status(200).json({
      nickname: user.nickname,
      role: user.role,
      token: token,
      isInitialPassword: user.isInitialPassword,
    });
  } catch (error) {
    console.log(error);
    res.status(400).json({ message: "Invalid login request" });
  }
};

exports.logout = async (req, res) => {
  res.status(200).json({
    message: "Logout Success",
  });
};

exports.updatePassword = async (req, res) => {
  const { password } = req.body;
  const salt = await bcrypt.genSalt(10);
  const hashedPassword = await bcrypt.hash(password, salt);
  try {
    const user = await User.findById(req.user._id);
    if (!user) {
      return res.status(400).json({ message: "User not found" });
    }
    if (user.role === "restaurantOwner" && user.isInitialPassword) {
      user.isInitialPassword = false;
    }

    savedUser.password = hashedPassword;
    const savedUser = await user.save();

    console.log("After change Password: ", savedUser);
    res.status(200).json({ message: "Change password success" });
  } catch (error) {
    console.log(error);
    res.status(500).json({ message: "Change password failed" });
  }
};
