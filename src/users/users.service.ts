import { Injectable, NotFoundException } from "@nestjs/common";
import { InjectModel } from "@nestjs/mongoose";
import { User } from "./user.model";
import {Collection, Model} from 'mongoose'

@Injectable()
export class UsersService{
    constructor(@InjectModel('User') private readonly userModel:Model<User>){}

    private async findUserbyPhone(phoneNum: string){   
        try{
            const user = await this.userModel.findOne({"phoneNum":phoneNum}).exec()
            return user
        } catch(error){
            throw new NotFoundException("no user")
        }
    }
    private async findUserById(id:string){
        try{
            return await this.userModel.findById(id).exec()
        }catch(error) {
            throw new NotFoundException('Could not find user.');
        } 
    }
    async getUserInfo(phoneNum:string){
        const user = await this.findUserbyPhone(phoneNum)
        return user.id
    }
    async getFriendsbyPhone(id:string){
        let i: number = 0
        const user = await this.userModel.findById(id)
        let friends : Array<User>
        while(i < user.phoneList.length){
            const friend = await this.findUserbyPhone(user.phoneList[i])
            friends = friends.concat(friend)
        }
        // user.phoneList = friends
        user.friendList = friends
    }

    async addUser(id:string,phoneNum: string){
        const newUser = new this.userModel({id,phoneNum})
        return await newUser.save();
    }
    
    async getUsers(){
        const users = await this.userModel.find().exec()
        return users.map(user =>({id:user.id, phoneNum: user.phoneNum}))
    }
}