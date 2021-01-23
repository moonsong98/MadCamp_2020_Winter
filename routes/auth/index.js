const express = require("express");
const router = express.Router();

const authCtrl = require("./auth.ctrl");
const verifyToken = require("../../middlewares/verifyToken");

router.post("/register", authCtrl.register);
router.post("/login", authCtrl.login);
router.post("/logout", authCtrl.logout);
router.post("/update-password", verifyToken, authCtrl.updatePassword);

module.exports = router;
