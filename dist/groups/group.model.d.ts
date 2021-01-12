import * as mongoose from 'mongoose';
import { User } from '../users/user.model';
export declare const GroupSchema: mongoose.Schema<mongoose.Document<any>, mongoose.Model<mongoose.Document<any>>>;
export interface Group extends mongoose.Document {
    groupId: string;
    participants: Array<User>;
}
