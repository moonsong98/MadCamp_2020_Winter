import { Post } from "./post.model";
import { Model } from 'mongoose';
export declare class PostService {
    readonly postModel: Model<Post>;
    constructor(postModel: Model<Post>);
}
