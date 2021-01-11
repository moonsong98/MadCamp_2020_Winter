import { Injectable, NotFoundException } from "@nestjs/common";
import { InjectModel } from "@nestjs/mongoose";
import { User } from "./user.model";
import {Collection, Model} from 'mongoose'

@Injectable()
export class UsersService{
    constructor(@InjectModel('User') private readonly userModel:Model<User>){}

    async getUserInfobyPhone(phoneNum:string){
        const user = await this.findUserbyPhone(phoneNum)
        return user
    }
    
    async getUserInfobyId(userId:string){
        const user = await this.findUserById(userId)
        return user === null ? -1 as unknown as string : user.id
    }

    async getFriends(userId:string, phoneList:Array<string>){
        let i: number = 0
        const user = await this.findUserById(userId)
        let friends : Array<User> = user.friendList
        while(i < phoneList.length){
            const friend = await this.findUserbyPhone(phoneList[i])
            if(friend) friends = friends.concat(friend)   
            i = i+1
        }
        user.friendList = friends
        return user
    }

    async addUser(userId:string, name:string, phoneNum: string){
        const newUser = await new this.userModel({userId, name ,phoneNum})
        return await newUser.save();
    }
    
    async getUsers(){
        const users = await this.userModel.find().exec()
        return users.map(user =>({id:user.userId, name:user.name, phoneNum: user.phoneNum}))
    }

    private async findUserbyPhone(phoneNum: string){   
        try{
            const user = await this.userModel.findOne({"phoneNum":phoneNum}).exec()
            return user
        } catch(error){
            throw new NotFoundException("no user")
        }
    }
    private async findUserById(userId:string){
        try{
            const user =  await this.userModel.findOne({"userId":userId}).exec()
            return user
        }catch(error) {
           return null
        } 
    }
}