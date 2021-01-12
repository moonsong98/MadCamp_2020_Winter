import { GroupsService } from "./groups.service";
import { Controller, Get, Post, Put, Delete, Body, Param, Req ,Res, HttpStatus } from '@nestjs/common';
import { Request } from 'express'
import { ok } from "assert";

@Controller('groups')  
export class GroupsController{
    constructor(private readonly groupsService: GroupsService){}
    @Post('postman')
    async createGroup(@Req()req:Request){
        console.log(req.body)
        const groupId = req.body.groupId
        const participants = req.body.participants
        const Group = await this.groupsService.createGroup(groupId, participants)
        return Group
    }
    @Get('all')
    async getGroups(){
        const groups = await this.groupsService.getGroups()
        return groups
    }
}