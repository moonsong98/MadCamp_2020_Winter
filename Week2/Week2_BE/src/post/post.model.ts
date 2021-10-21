import { kStringMaxLength } from 'buffer'
import * as mongoose from 'mongoose'

export const PostSchema = new mongoose.Schema({
     fileName: {type:String, required: true}
})

export interface Post extends mongoose.Document {
    fileName:String;
}

export const Post = mongoose.model('Post', PostSchema)