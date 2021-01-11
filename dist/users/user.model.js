"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UserSchema = void 0;
const mongoose = require("mongoose");
exports.UserSchema = new mongoose.Schema({
    userId: { type: String, required: true },
    name: { type: String },
    phoneNum: { type: String, required: true },
    friendList: { type: Array },
    groupList: { type: Array }
});
//# sourceMappingURL=user.model.js.map