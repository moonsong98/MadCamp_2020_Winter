import { GroupsService } from "./groups.service";
import { Controller, Get, Post, Put, Delete, Body, Param, Req ,Res, HttpStatus } from '@nestjs/common';
import { Request } from 'express'
import { ok } from "assert";

@Controller('groups')  
export class GroupsController{
    constructor(private readonly groupsService: GroupsService){}
    @Post('postman')
    async createGroup(@Req()req:Request){
        (req.body)
        const groupName = req.body.groupName
        const groupId = req.body.groupId
        const participants = req.body.participants
        const Group = await this.groupsService.createGroup(groupName, groupId, participants)
        return Group
    }
    @Get('all')
    async getGroups(){
        const groups = await this.groupsService.getGroups()
        return groups
    }
    @Get('members/:groupName')
    async getGroupbyName(@Param('groupName') groupName:string){
        return await (await this.groupsService.getGroupbyName(groupName)).participants
    }
}