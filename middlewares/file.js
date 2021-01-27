const fs = require("fs");
const path = require("path");

exports.deleteFiles = async (files) => {
  const promises = files.map((file) => {
    const filePath = path.join(
      __dirname,
      `../images/menus/${file.originalname}`
    );
    return fs.unlink(filePath);
  });

  const output = await Promise.all(promises);
  console.log("Delete files output:", output);
};

exports.deleteFile = async (filename) => {
  fs.unlink(filename);
};
