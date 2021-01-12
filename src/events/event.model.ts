import { url } from 'inspector'
import * as mongoose from 'mongoose'
import { Url } from 'url'
import { User } from '../users/user.model'

export const EventSchema = new mongoose.Schema({
    name : {type: String, required: true},
    date : {type:String, required:true},
    members : {type:Array},
    description:{type:String}
})

export interface Event extends mongoose.Document {
    name:string;
    date:string;
    members:Array<User>;
    description:string;
}