"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.EventSchema = void 0;
const mongoose = require("mongoose");
exports.EventSchema = new mongoose.Schema({
    name: { type: String, required: true },
    date: { type: String, required: true },
    members: { type: Array },
    description: { type: String }
});
//# sourceMappingURL=event.model.js.map