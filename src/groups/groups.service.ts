import { Injectable, NotFoundException } from "@nestjs/common";
import { InjectModel } from "@nestjs/mongoose";
import { Group } from "./group.model";
import {Collection, Model} from 'mongoose'
import {UserSchema, User}  from '../users/user.model';


@Injectable()
export class GroupsService{
    constructor(
        @InjectModel('Group') readonly groupModel:Model<Group>,
        @InjectModel('User') readonly userModel:Model<User>
    ){}

    async createGroup(groupName:string, groupId:string, participants:Array<string>){
        const newGroup = await new this.groupModel({groupName, groupId ,participants})
        return await newGroup.save();
    }
    async getGroups(){
        const groups = await this.groupModel.find().exec()
        return groups
    }
}