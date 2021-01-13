"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Post = exports.PostSchema = void 0;
const mongoose = require("mongoose");
exports.PostSchema = new mongoose.Schema({
    fileName: { type: String, required: true }
});
exports.Post = mongoose.model('Post', exports.PostSchema);
//# sourceMappingURL=post.model.js.map