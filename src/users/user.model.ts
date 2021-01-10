import * as mongoose from 'mongoose'

export const UserSchema = new mongoose.Schema({
    id: {type: String, required: true},
    phoneNum: {type: Number, required: true},
})

export interface User extends mongoose.Document {
    id: string;
    phoneNum : string;
}