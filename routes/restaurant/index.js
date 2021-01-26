const express = require("express");
const router = express.Router();
const multer = require("multer");
const path = require("path");

const restrCtrl = require("./restaurant.ctrl");
const auth = require("../../middlewares/auth");

// handling file upload
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, path.join(__dirname, "../images/menus"));
  },
  filename: (req, file, cb) => {
    cb(null, file.originalname);
  }, // format: {fieldname}-{date} (with ext)
});

const fileFilter = (req, file, cb) => {
  if (
    file.mimetype === "image/jpg" ||
    file.mimetype === "image/jpeg" ||
    file.mimetype === "image/png"
  ) {
    cb(null, true);
  } else {
    req.fileValidationError = "Image uploaded is not of type jpg/jpeg or png";
    cb(null, false);
  }
};
let upload = multer({ storage: storage, fileFilter: fileFilter });

router.get("/", restrCtrl.getRestaurants);
router.get("/:restr_id", restrCtrl.getRestaurant);

router.post("/", auth.verifyToken, restrCtrl.createRestaurant);
router.put(
  "/:restr_id",
  auth.verifyToken,
  upload.array("images"),
  restrCtrl.updateRestaurant
);
router.delete("/:restr_id", restrCtrl.deleteRestaurant);

module.exports = router;
