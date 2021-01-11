import * as mongoose from 'mongoose'

export const UserSchema = new mongoose.Schema({
    id: {type: String, required: true},
    phoneNum: {type: String, required: true},
    name:{type:String},
    phoneList:{type: Array},
    friendList:{type:Array},
    groupList:{type:Array}
})

export interface User extends mongoose.Document {
    id: string;
    phoneNum : string;
    name: string;
    phoneList: Array<string>;
    friendList: Array<User>
    groupList: Array<number>;
}