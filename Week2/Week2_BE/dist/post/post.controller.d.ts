import { PostService } from './post.service';
import { Response } from 'express';
export declare class PostController {
    private readonly postService;
    constructor(postService: PostService);
    getPhoto(file: any): Promise<void>;
    test2(param: {
        filename: string;
    }, res: Response): Promise<void>;
}
