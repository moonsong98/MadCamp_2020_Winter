import * as mongoose from 'mongoose';
export declare const UserSchema: mongoose.Schema<mongoose.Document<any>, mongoose.Model<mongoose.Document<any>>>;
export interface User extends mongoose.Document {
    id: string;
    phoneNum: string;
    name: string;
    phoneList: Array<string>;
    friendList: Array<User>;
    groupList: Array<number>;
}
