import { UsersService } from "./users.service";
import { Controller, Get, Post, Put, Delete, Body, Param, Req , HttpStatus } from '@nestjs/common';
import { Request } from 'express'
import { ok } from "assert";

@Controller('users')  
export class UsersController{
    constructor(private readonly usersService: UsersService){}

    @Post('postman')
    async addUser(@Req()req:Request){
        const id = req.body.userId
        const phoneNum = req.body.userPhoneNum
        const User = await this.usersService.addUser(id,phoneNum)
        console.log("saved")
        return User
    }
    @Get('all')
    async getUsers(){
        const users = await this.usersService.getUsers();
        return users
    }
    // @Post('friends')//user의 userId로 user member에 접근해서 
    // async getFriendsbyPhone(@Req()req:Request){
    //     const id = req.body.userId
    //     const friendList = await req.body.phoneList.getFriends(id)
    //     return friendList
    // }
    @Get(':id')
    async getUserInfo(@Param('id') id: string){
        const userId =  await this.usersService.getUserInfo(id);
        return userId
    }
}