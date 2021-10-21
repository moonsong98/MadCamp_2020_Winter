import { Field, ObjectType } from '@nestjs/graphql';

@ObjectType()
export class PostType {
	@Field()
	readonly id: string;
	@Field()
	readonly title: string;
	@Field()
	readonly body: string;
}
