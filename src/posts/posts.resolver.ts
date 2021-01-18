import { GqlAuthGuard } from './../users/guards/gql-auth.guard';
import { Args, Mutation, Query, Resolver } from '@nestjs/graphql';
import { PostType } from './dto/post.dto';
import { PostsService } from './posts.service';
import { CreatePostInput } from './inputs/post.input';
import { UseGuards } from '@nestjs/common';

/* Test */
import { GraphQLUpload } from 'apollo-server-express';
import { createWriteStream } from 'fs';

@Resolver(() => PostType)
export class PostsResolver {
	constructor(private readonly postsService: PostsService) {}
	/* Create */
	@UseGuards(GqlAuthGuard)
	@Mutation(() => PostType)
	async createPost(@Args('input') input: CreatePostInput) {
		return await this.postsService.createPost(input);
	}

	/* Read */
	@Query(() => [PostType])
	async readAllPosts() {
		return await this.postsService.readAllPosts();
	}

	/* Update */

	/* Delete */

	/* For Debug */
	@Mutation(() => Boolean)
	async deleteAllPosts() {
		return await this.postsService.deleteAllPosts();
	}

	@Mutation(() => Boolean)
	async uploadFile(
		@Args({ name: 'file', type: () => GraphQLUpload })
		{ createReadStream, filename }
	): Promise<boolean> {
		console.log(filename);
		const a = new Promise(async (resolve, reject) =>
			createReadStream().pipe(createWriteStream(`/home/ubuntu/images/${filename}`))
		)
			.then((data) => console.log(data))
			.catch((err) => console.error(`error is ${err}`));
		return true;
	}
}
