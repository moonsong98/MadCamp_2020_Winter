import * as mongoose from 'mongoose'
import { User } from '../users/user.model'

export const GroupSchema = new mongoose.Schema({
    groupId :{type:String},
    participants:{type:Array}
})

export interface Group extends mongoose.Document {
    groupId:string;
    participants:Array<User>
}