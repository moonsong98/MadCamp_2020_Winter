const User = require("../../models/user");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");

exports.register = async (req, res) => {
  const { userId, password, username, role } = req.body;
  const idExist = await User.findOne({ userId: userId });
  if (idExist) return res.status(400).send("ID already exists");

  const salt = await bcrypt.genSalt(10);
  const hashedPassword = await bcrypt.hash(password, salt);

  const user = new User({
    userId: userId,
    password: hashedPassword,
    username: username,
    role: role,
  });

  try {
    const savedUser = await user.save();
    res.json({
      _id: savedUser._id,
      userId: savedUser.userId,
      role: savedUser.role,
    });
  } catch (error) {
    console.log(error);
    res.status(400).json({ message: "Register failed" });
  }
};

exports.login = async (req, res) => {
  const { userId, password } = req.body;

  try {
    const user = await User.findOne({ userId: userId });
    if (!user)
      return res.status(400).json({ message: "ID or password is wrong" });
    console.log(user);
    const validPW = await bcrypt.compare(req.body.password, user.password);
    if (!validPW)
      return res.status(400).json({ message: "ID or password is wrong" });

    // assign token
    const token = jwt.sign({ userId: user.userId }, process.env.TOKEN_SECRET);
    res.status(200).json({
      _id: user._id,
      userId: user.userId,
      role: user.role,
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
