import { Field, ObjectType } from '@nestjs/graphql';

@ObjectType()
export class UserType {
	@Field()
	readonly id: string;
	@Field()
	readonly username: string;
	@Field()
	readonly password: string;
	@Field()
	readonly name: string;
	@Field()
	readonly email: string;
	@Field()
	confirmed: boolean;
}
