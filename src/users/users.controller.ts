import { UsersService } from "./users.service";
import { Controller, Get, Post, Put, Delete, Body, Param, Req , HttpStatus } from '@nestjs/common';
import { Request } from 'express'
import { ok } from "assert";

@Controller('users')  
export class UsersController{
    constructor(private readonly usersService: UsersService){}

    // @Get()
    // async addUser(@Req()req: Request){
    //     console.log("adduser")
    //     const id: string = req.body.userId
    //     const phoneNum : string = req.body.userPhoneNum
    //     const User = await this.usersService.addUser(id, phoneNum)
    //     // User.save()
    //     const users = await this.usersService.getUsers();
    //     return users
    // }
    // @Post('post')
    // async addUser(@Req()req: Request){
    //     const id = req.body.userId
    //     const phoneNum = req.body.userPhoneNum
    //     const User = await this.usersService.addUser(id, phoneNum)
    //     await User.save()
    //     console.log("saved")
    //     return User
    //     // const newUser = await this.usersService.addUser(id, phoneNum)
    //     // return newUser
    // }
    // @Post('postman')
    // async addUser(@Body('userId') id:string, @Body('userPhoneNum') phoneNum:string){
    //     const User = await this.usersService.addUser(id,phoneNum)
    //     console.log("saved")
    // }
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

}