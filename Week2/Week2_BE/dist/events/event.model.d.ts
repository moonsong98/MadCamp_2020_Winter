import * as mongoose from 'mongoose';
import { User } from '../users/user.model';
export declare const EventSchema: mongoose.Schema<mongoose.Document<any>, mongoose.Model<mongoose.Document<any>>>;
export interface Event extends mongoose.Document {
    name: string;
    date: string;
    members: Array<User>;
    description: string;
}
