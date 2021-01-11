import * as mongoose from 'mongoose'

export const UserSchema = new mongoose.Schema({
    userId: {type: String, required: true},
    name:{type:String, required: true},
    phoneNum: {type: String, required: true},
    friendList:{type:Array},
    groupList:{type:Array}
})

export interface User extends mongoose.Document {
    userId: string;
    name: string;
    phoneNum : string;
    friendList: Array<User>
    groupList: Array<number>;
}