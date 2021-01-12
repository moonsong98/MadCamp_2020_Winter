import * as mongoose from 'mongoose'

export const UserSchema = new mongoose.Schema({
    userId: {type: String, required: true},
    name:{type:String},
    phoneNum: {type: String, required: true},
    friendList:{type:Array},
    groupList:{type:Array},
    eventList:{type:Array}
})

export interface User extends mongoose.Document {
    userId: string;
    name: string;
    phoneNum : string;
    friendList: Array<User>
    groupList: Array<string>;
    eventList: Array<string>;
}

export const User = mongoose.model('User',UserSchema)



