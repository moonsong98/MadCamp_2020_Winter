import { UsersService } from "./users.service";
import { Controller, Get, Post, Put, Delete, Body, Param, Req , HttpStatus } from '@nestjs/common';
import { Request } from 'express'
import { ok } from "assert";

@Controller('users')  
export class UsersController{
    constructor(private readonly usersService: UsersService){}

    @Post()
    async addUser(@Req()req: Request){
        const id = req.body.userId
        const phoneNum = req.body.userPhoneNum
        const User = await this.usersService.addUser(id, phoneNum)
        return{
            statusCode: HttpStatus.OK,
            message: 'user added succesfully'
        }
    }
}