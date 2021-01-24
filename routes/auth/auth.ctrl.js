const { User, RestaurantOwner, Customer } = require("../../models/user");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const auth = require("../../middlewares/auth");
const { restrOwnerRegister, customerRegister } = require("./register");
require("dotenv").config();

exports.register = async (req, res) => {
  console.log(req.body);
  const { role } = req.body;

  switch (role) {
    case "restaurantOwner":
      restrOwnerRegister(req, res);
      break;
    case "user":
      customerRegister(req, res);
      break;

    case "admin":
      return res.status(400).send("Register as admin is not Allowed");
    default:
      return res.status(400).send("Invalid request");
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

    if (user.role === "user" && user.confirmed === false) {
      return res.status(401).json({
        message: "Your account is not verified. Please check your email",
      });
    }

    // assign token
    const token = auth.createAccessToken(user);
    const refreshToken = auth.createRefreshToken(user);

    console.log(token, refreshToken);
    res.status(200).json({
      nickname: user.nickname,
      role: user.role,
      isInitialPassword: user.isInitialPassword,

      token: token,
      refreshToken: refreshToken,
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
    const user = req.user;
    if (!user) {
      return res.status(400).json({ message: "User not found" });
    }
    if (user.role === "restaurantOwner" && user.isInitialPassword) {
      user.isInitialPassword = false;
    }

    user.password = hashedPassword;
    const savedUser = await user.save();

    console.log("After change Password: ", savedUser);
    res.status(200).json({ message: "Change password success" });
  } catch (error) {
    console.log(error);
    res.status(500).json({ message: "Change password failed" });
  }
};

exports.refreshToken = async (req, res) => {
  const refreshToken = req.headers["refresh-token"];
  if (!refreshToken) {
    return res.status(401).send("Token not provided");
  }

  let user = null;
  try {
    const verified = jwt.verify(refreshToken, process.env.TOKEN_SECRET);
    console.log("verified: ", verified);
    user = await User.findById(verified._id);
  } catch (error) {
    console.log(error);
    return res.status(401).send("Invalid token");
  }

  return res.status(200).json({
    token: auth.createRefreshToken(user),
    refreshToken: auth.createAccessToken(user),
  });
};

exports.verifyEmail = async (req, res) => {
  const verifiedUser = await User.findOne({
    emailVerifyKey: req.params.verify_key,
  });
  if (!verifiedUser) {
    return res.status(401).send("Invalid verify key");
  }

  try {
    verifiedUser.confirmed = true;
    await verifiedUser.save();
    res.status(200).send("Successfully verify email");
  } catch (error) {
    console.log(error);
    res.status(500).send("Failed to verify email");
  }
};
