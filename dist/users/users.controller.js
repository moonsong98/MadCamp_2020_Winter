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
exports.UsersController = void 0;
const users_service_1 = require("./users.service");
const common_1 = require("@nestjs/common");
let UsersController = class UsersController {
    constructor(usersService) {
        this.usersService = usersService;
    }
    async addUser(req) {
        const userId = req.body.userId;
        const phoneNum = req.body.phoneNum;
        const User = await this.usersService.addUser(userId, phoneNum);
        return User;
    }
    async getUsers() {
        const users = await this.usersService.getUsers();
        return users;
    }
    async getFriends(req) {
        const userId = req.body.userId;
        const phoneList = req.body.phoneList;
        const me = await this.usersService.getFriends(userId, phoneList);
        return me.friendList;
    }
    async getUserInfo(Id) {
        const userId = await this.usersService.getUserInfobyId(Id);
        return userId;
    }
    async updateGroup(req) {
        const usersPhoneNumbers = req.body.usersPhoneNumbers;
        const groupName = req.body.groupName;
        await this.usersService.updateUsersGroupId(usersPhoneNumbers, groupName);
    }
    async getGroupList(Id) {
        const groupList = await this.usersService.getGroupListbyId(Id);
        return groupList;
    }
};
__decorate([
    common_1.Post('postman'),
    __param(0, common_1.Req()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], UsersController.prototype, "addUser", null);
__decorate([
    common_1.Get('all'),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], UsersController.prototype, "getUsers", null);
__decorate([
    common_1.Post('friends'),
    __param(0, common_1.Req()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], UsersController.prototype, "getFriends", null);
__decorate([
    common_1.Get(':Id'),
    __param(0, common_1.Param('Id')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], UsersController.prototype, "getUserInfo", null);
__decorate([
    common_1.Post('updategroup'),
    __param(0, common_1.Req()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], UsersController.prototype, "updateGroup", null);
__decorate([
    common_1.Get('getgrouplist/:Id'),
    __param(0, common_1.Param('Id')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], UsersController.prototype, "getGroupList", null);
UsersController = __decorate([
    common_1.Controller('users'),
    __metadata("design:paramtypes", [users_service_1.UsersService])
], UsersController);
exports.UsersController = UsersController;
//# sourceMappingURL=users.controller.js.map