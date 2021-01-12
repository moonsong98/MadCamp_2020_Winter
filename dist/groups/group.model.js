"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.GroupSchema = void 0;
const mongoose = require("mongoose");
exports.GroupSchema = new mongoose.Schema({
    groupId: { type: String },
    participants: { type: Array }
});
//# sourceMappingURL=group.model.js.map