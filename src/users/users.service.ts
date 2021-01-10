import { Injectable } from "@nestjs/common";
import { InjectModel } from "@nestjs/mongoose";
import { User } from "./user.model";
import {Model} from 'mongoose'

@Injectable()
export class UsersService{
    constructor(@InjectModel('User') private readonly userModel:Model<User>){}

    userHello():string{
        return 'user Hello!';
    }

    async addUser(id:string,phoneNum: string){
        const newUser = new this.userModel({id,phoneNum})
        return await newUser.save();
    }
    async addUser1(id:string,phoneNum: string){
        const newUser = new this.userModel({id,phoneNum})
        return await newUser.save();
    }
    
    async getUsers(){
        const users = await this.userModel.find().exec()
        return users.map(user =>({id:user.id, phoneNum: user.phoneNum}))
    }
}