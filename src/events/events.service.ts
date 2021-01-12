import { Injectable, NotFoundException } from "@nestjs/common";
import { InjectModel } from "@nestjs/mongoose";
import { Event } from "./event.model";
import {Collection, Model} from 'mongoose'
import {UserSchema, User}  from '../users/user.model';


@Injectable()
export class EventsService{
    constructor(
        @InjectModel('Event') readonly eventModel:Model<Event>
    ){}

    async createEvent(name:string, date:string, members:Array<string>){
        const newEvent = await new this.eventModel({name, date ,members})
        return await newEvent.save();
    }
    async getAllEvents(){
        const events = await this.eventModel.find().exec()
        return events
    }
    async getEvents(userId:string){
        
    }

}