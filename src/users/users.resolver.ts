import { Args, Mutation, Query, Resolver } from '@nestjs/graphql';
import { UserType } from './dto/user.dto';
import { UserToken } from './dto/token.dto';
import { UsersService } from './users.service';
import { CreateUserInput, LoginInput } from './inputs/user.input';

@Resolver(() => UserType)
export class UsersResolver {
	constructor(private readonly usersService: UsersService) {}
	/* Create */
	@Mutation(() => UserType)
	async createUser(@Args('input') input: CreateUserInput) {
		return await this.usersService.createUser(input);
	}

	/* Read */
	@Query(() => [UserType])
	async readAllUsers() {
		return await this.usersService.readAllUsers();
	}

	@Query(() => String)
	async login(@Args('input') input: LoginInput) {
		return await this.usersService.login(input);
	}

	/* Update */
	@Mutation(() => Boolean)
	async confirmUser(@Args('redisId') redisId: string) {
		return await this.usersService.confirmUser(redisId);
	}
	/* Delete */
	@Mutation(() => Boolean)
	async deleteUser(@Args('id') id: string) {
		return await this.usersService.deleteUser(id);
	}

	/* For Debug */
	@Mutation(() => Boolean)
	async deleteAllUsers() {
		return await this.usersService.deleteAllUser();
	}
}
