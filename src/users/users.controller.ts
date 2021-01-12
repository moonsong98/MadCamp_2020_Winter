import { UsersService } from "./users.service";
import { Controller, Get, Post, Put, Delete, Body, Param, Req ,Res, HttpStatus } from '@nestjs/common';
import { Request } from 'express'
import { ok } from "assert";
import { eventNames } from "process";

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
    @Get(':Id')
    async getUserInfo(@Param('Id') Id: string){
        const userId =  await this.usersService.getUserInfobyId(Id);
        return userId
    }

    @Post('updategroup')
    async updateGroup(@Req()req:Request) {
        const usersPhoneNumbers:Array<string> = req.body.usersPhoneNumbers
        const groupName = req.body.groupName
        await this.usersService.updateUsersGroupId(usersPhoneNumbers, groupName)
    }

    @Post('updateEvent')
    async updateEvent(@Req()req:Request) {
        const usersPhoneNumbers:Array<string> = req.body.usersPhoneNumbers
        const eventName = req.body.eventName
        await this.usersService.updateUsersEvent(usersPhoneNumbers, eventName)
    }

    @Get('getgrouplist/:Id')
    async getGroupList(@Param('Id') Id:string) {
        const groupList = await this.usersService.getGroupListbyId(Id)
        return groupList
    }

    @Get('geteventlist/:Id')
    async getEventList(@Param('Id') Id:string) {
        const eventList = await this.usersService.getEventListbyId(Id)
        return eventList
    }
}

