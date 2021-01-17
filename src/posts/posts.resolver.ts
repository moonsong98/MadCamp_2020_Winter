import { Args, Mutation, Query, Resolver } from '@nestjs/graphql';
import { PostType } from './dto/post.dto';
import { PostsService } from './posts.service';
import { CreatePostInput } from './inputs/post.input';

@Resolver(() => PostType)
export class PostsResolver {
	constructor(private readonly postsService: PostsService) {}
	/* Create */
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
}
