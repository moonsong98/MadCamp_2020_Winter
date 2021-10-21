import { Field, InputType } from '@nestjs/graphql';

@InputType()
export class CreatePostInput {
	@Field()
	readonly title: string;
	@Field()
	readonly body: string;
}
