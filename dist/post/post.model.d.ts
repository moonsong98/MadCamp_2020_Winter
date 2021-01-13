import * as mongoose from 'mongoose';
export declare const PostSchema: mongoose.Schema<mongoose.Document<any>, mongoose.Model<mongoose.Document<any>>>;
export interface Post extends mongoose.Document {
    fileName: String;
}
export declare const Post: mongoose.Model<mongoose.Document<any>>;
