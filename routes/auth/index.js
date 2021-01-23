const express = require("express");
const router = express.Router();

const authCtrl = require("./auth.ctrl");

router.post("/register", authCtrl.register);
router.post("/login", authCtrl.login);
router.post("/logout", authCtrl.logout);

module.exports = router;
