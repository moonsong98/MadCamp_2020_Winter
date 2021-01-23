const jwt = require("jsonwebtoken");
const User = require("../models/user");

module.exports = async (req, res, next) => {
  console.log("!");
  const token = req.header("token");
  if (!token) return res.status(401).send("Access Denied");

  try {
    const verified = jwt.verify(token, process.env.TOKEN_SECRET);
    const user = User.findOne({ userId: verified.userId });
    if (!user) return res.status(400).json({ message: "Invalid user" });

    req.user = user;
    next();
  } catch (error) {
    console.log(error);
    res.status(400).json({ message: "Invalid token" });
  }
};
