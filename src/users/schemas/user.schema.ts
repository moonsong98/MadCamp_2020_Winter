import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type UserDocument = User & Document;

@Schema()
export class User {
	@Prop()
	name: string;

	@Prop()
	username: string;

	@Prop()
	password: string;

	@Prop()
	email: string;

	@Prop()
	confirmed: boolean;
}

export const UserSchema = SchemaFactory.createForClass(User);
