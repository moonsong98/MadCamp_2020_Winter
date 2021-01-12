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

    async createGroup(groupId:string, participants:Array<string>){
        const newGroup = await new this.groupModel({groupId ,participants})
        var i = 0
        while(i<participants.length){
            // console.log(User)
            // console.log()
            // const user = await User.findOne({"phoneNum":participants[i]})
            // console.log(user)
            i=i+1
        }
        return await newGroup.save();
    }
    async getGroups(){
        const groups = await this.groupModel.find().exec()
        return groups.map(group =>({id:group.groupId, participants: group.participants}))
    }
}