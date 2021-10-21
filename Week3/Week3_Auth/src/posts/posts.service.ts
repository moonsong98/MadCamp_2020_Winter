import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Post, PostDocument } from './schemas/post.schema';
import { CreatePostInput } from './inputs/post.input';

@Injectable()
export class PostsService {
	constructor(@InjectModel(Post.name) private postModel: Model<PostDocument>) {}

	/* Create */
	async createPost(createPostInput: CreatePostInput): Promise<Post> {
		const createdPost = new this.postModel({
			title: createPostInput.title,
			body: createPostInput.body,
		});
		return await createdPost.save();
	}

	/* Read */
	async readAllPosts(): Promise<Post[]> {
		return this.postModel.find().exec();
	}

	/* Debug */
	async deleteAllPosts(): Promise<boolean> {
		return null != (await this.postModel.deleteMany({}));
	}
}
