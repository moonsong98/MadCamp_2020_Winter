import { UsersService } from "./users.service";
import { Controller, Get, Post, Put, Delete, Body, Param, Req ,Res, HttpStatus } from '@nestjs/common';
import { Request } from 'express'
import { ok } from "assert";

@Controller('users')  
export class UsersController{
    constructor(private readonly usersService: UsersService){}

    @Post('postman')
    async addUser(@Req()req:Request){
        const userId = req.body.userId
        // const name = req.body.name
        const phoneNum = req.body.phoneNum
        const User = await this.usersService.addUser(userId, phoneNum)
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
        console.log(req.body)
        return me.friendList
    }
    @Get(':Id')
    async getUserInfo(@Param('Id') Id: string){
        const userId =  await this.usersService.getUserInfobyId(Id);
        console.log(userId)
        return userId
    }
}

