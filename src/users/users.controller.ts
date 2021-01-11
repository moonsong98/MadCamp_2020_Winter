import { UsersService } from "./users.service";
import { Controller, Get, Post, Put, Delete, Body, Param, Req , HttpStatus } from '@nestjs/common';
import { Request } from 'express'
import { ok } from "assert";

@Controller('users')  
export class UsersController{
    constructor(private readonly usersService: UsersService){}

    @Post('postman')
    async addUser(@Req()req:Request){
        const userId = req.body.userId
        const name = req.body.name
        const phoneNum = req.body.phoneNum
        const User = await this.usersService.addUser(userId, name, phoneNum)
        return User
    }

    @Get('all')
    async getUsers(){
        const users = await this.usersService.getUsers();
        return users
    }
    @Post('friends')
    async getFriends(@Req()req:Request){
        const userId = req.body.userId
        const phoneList = req.body.phoneList
        const me = await this.usersService.getFriends(userId, phoneList)
        return me.friendList
    }
    @Get(':phoneNum')
    async getUserInfo(@Param('phoneNum') phoneNum: string){
        const userId =  await this.usersService.getUserInfobyPhone(phoneNum);
        return userId
    }
}