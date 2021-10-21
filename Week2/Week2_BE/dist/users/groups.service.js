"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.GroupsService = void 0;
const common_1 = require("@nestjs/common");
const mongoose_1 = require("@nestjs/mongoose");
const mongoose_2 = require("mongoose");
const users_service_1 = require("./users.service");
const user_model_1 = require("./user.model");
let GroupsService = class GroupsService {
    constructor(groupModel, userModel) {
        this.groupModel = groupModel;
        this.userModel = userModel;
    }
    async createGroup(groupId, participants) {
        const newGroup = await new this.groupModel({ groupId, participants });
        let num;
        for (num in participants) {
            const user = await this.userModel.findOne({ "phoneNum": num });
            if (user.groupList) {
                user.groupList.push(groupId);
            }
            else {
                user.groupList = [groupId];
            }
        }
        return await newGroup.save();
    }
    async getGroups() {
        const groups = await this.groupModel.find().exec();
        return groups.map(group => ({ groupId: group.groupId, participants: group.participants }));
    }
};
GroupsService = __decorate([
    common_1.Injectable(),
    __param(0, mongoose_1.InjectModel('Group')), __param(1, mongoose_1.InjectModel('User')),
    __metadata("design:paramtypes", [mongoose_2.Model, mongoose_2.Model])
], GroupsService);
exports.GroupsService = GroupsService;
//# sourceMappingURL=groups.service.js.map