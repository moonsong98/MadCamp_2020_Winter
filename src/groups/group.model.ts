import * as mongoose from 'mongoose'
import { User } from '../users/user.model'

export const GroupSchema = new mongoose.Schema({
    groupName:{type:String},
    groupId :{type:String},
    participants:{type:Array}
})

export interface Group extends mongoose.Document {
    groupName:string;
    groupId:string;
    participants:Array<User>
}