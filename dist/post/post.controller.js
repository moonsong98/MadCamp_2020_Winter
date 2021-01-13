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
exports.PostController = void 0;
const common_1 = require("@nestjs/common");
const platform_express_1 = require("@nestjs/platform-express");
const post_service_1 = require("./post.service");
const multer_1 = require("multer");
const path_1 = require("path");
let PostController = class PostController {
    constructor(postService) {
        this.postService = postService;
    }
    async getPhoto(file) {
        console.log(file.originalname);
    }
    async test2(param, res) {
        console.dir(param);
        const filePath = path_1.join(__dirname, '..', 'images', param.filename);
        res.sendFile(filePath);
    }
};
__decorate([
    common_1.Post('upload'),
    common_1.UseInterceptors(platform_express_1.FileInterceptor('image', {
        storage: multer_1.diskStorage({
            destination: path_1.join(__dirname, '../images'),
            filename: (req, file, cb) => { console.log(file.originalname); return cb(null, file.originalname); }
        })
    })),
    __param(0, common_1.UploadedFile()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], PostController.prototype, "getPhoto", null);
__decorate([
    common_1.Get(':filename'),
    __param(0, common_1.Param()), __param(1, common_1.Res()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, Object]),
    __metadata("design:returntype", Promise)
], PostController.prototype, "test2", null);
PostController = __decorate([
    common_1.Controller('post'),
    __metadata("design:paramtypes", [post_service_1.PostService])
], PostController);
exports.PostController = PostController;
//# sourceMappingURL=post.controller.js.map