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
exports.UsersService = void 0;
const common_1 = require("@nestjs/common");
const mongoose_1 = require("@nestjs/mongoose");
const mongoose_2 = require("mongoose");
let UsersService = class UsersService {
    constructor(userModel) {
        this.userModel = userModel;
    }
    async getUserInfobyPhone(phoneNum) {
        const user = await this.findUserbyPhone(phoneNum);
        return user;
    }
    async getUserInfobyId(userId) {
        const user = await this.findUserById(userId);
        return user === null ? -1 : user.userId;
    }
    async getFriends(userId, phoneList) {
        let i = 0;
        const user = await this.findUserById(userId);
        let friends = user.friendList;
        while (i < phoneList.length) {
            const friend = await this.findUserbyPhone(phoneList[i]);
            if (friend)
                friends = friends.concat(friend);
            i = i + 1;
        }
        user.friendList = friends;
        return user;
    }
    async addUser(userId, phoneNum) {
        const newUser = await new this.userModel({ userId, phoneNum });
        return await newUser.save();
    }
    async getUsers() {
        const users = await this.userModel.find().exec();
        return users.map(user => ({ id: user.userId, name: user.name, phoneNum: user.phoneNum }));
    }
    async findUserbyPhone(phoneNum) {
        try {
            const user = await this.userModel.findOne({ "phoneNum": phoneNum }).exec();
            return user;
        }
        catch (error) {
            throw new common_1.NotFoundException("no user");
        }
    }
    async findUserById(userId) {
        try {
            const user = await this.userModel.findOne({ "userId": userId }).exec();
            return user;
        }
        catch (error) {
            return null;
        }
    }
};
UsersService = __decorate([
    common_1.Injectable(),
    __param(0, mongoose_1.InjectModel('User')),
    __metadata("design:paramtypes", [mongoose_2.Model])
], UsersService);
exports.UsersService = UsersService;
//# sourceMappingURL=users.service.js.map