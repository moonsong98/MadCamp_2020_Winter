"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UserSchema = void 0;
const mongoose = require("mongoose");
exports.UserSchema = new mongoose.Schema({
    id: { type: String, required: true },
    phoneNum: { type: Number, required: true },
});
//# sourceMappingURL=user.model.js.map