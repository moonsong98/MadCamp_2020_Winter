import { Field, InputType } from '@nestjs/graphql';

@InputType()
export class CreateUserInput {
	@Field()
	readonly username: string;
	@Field()
	readonly password: string;
	@Field()
	readonly name: string;
	@Field()
	readonly email: string;
}

@InputType()
export class LoginInput {
	@Field()
	readonly username: string;
	@Field()
	readonly password: string;
}
