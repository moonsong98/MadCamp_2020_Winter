import * as mongoose from 'mongoose';
export declare const UserSchema: mongoose.Schema<mongoose.Document<any>, mongoose.Model<mongoose.Document<any>>>;
export interface User extends mongoose.Document {
    userId: string;
    name: string;
    phoneNum: string;
    friendList: Array<User>;
    groupList: Array<string>;
}
export declare const User: mongoose.Model<mongoose.Document<any>>;
