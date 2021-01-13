import { Injectable } from "@nestjs/common"
import { InjectModel } from "@nestjs/mongoose";
import { Post } from "./post.model"
import { Model } from 'mongoose'

@Injectable()
export class PostService {
    constructor(@InjectModel('Post') readonly postModel:Model<Post>){}
}