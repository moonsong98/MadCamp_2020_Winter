"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.User = exports.UserSchema = void 0;
const mongoose = require("mongoose");
exports.UserSchema = new mongoose.Schema({
    userId: { type: String, required: true },
    name: { type: String },
    phoneNum: { type: String, required: true },
    friendList: { type: Array },
    groupList: { type: Array },
    eventList: { type: Array }
});
exports.User = mongoose.model('User', exports.UserSchema);
//# sourceMappingURL=user.model.js.map