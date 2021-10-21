import { CreateCommentDTO, CreatePostDTO } from './DTO/post.dto';
import { Model } from 'mongoose';
import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Post } from './types/post';

@Injectable()
export class PostsService {
	constructor(@InjectModel('Post') private postModel: Model<Post>) {}

	/* Create */
	async createPost(createPostDTO: CreatePostDTO, imageName: string[]): Promise<Post> {
		console.log(createPostDTO);
		const createdPost = new this.postModel({ ...createPostDTO, imageName });
		return await createdPost.save();
	}

	async createComment(createCommentDTO: CreateCommentDTO): Promise<Post> {
		const existingPost = await this.postModel.findById(createCommentDTO._id);
		existingPost.comments.push({ name: createCommentDTO.name, body: createCommentDTO.body, time: new Date() });
		return existingPost.save();
	}

	/* Read */
	async findAll(): Promise<Post[]> {
		return this.postModel.find().exec();
	}

	async getPost(id: string): Promise<Post> {
		return this.postModel.findById(id);
	}

	/* Update */

	/* Delete */
	async deleteAllPosts(): Promise<number> {
		const res = await this.postModel.deleteMany({});
		return res.deletedCount;
	}
}
