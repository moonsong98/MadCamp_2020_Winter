import { Document } from 'mongoose';

interface Comment {
	name: string;
	body: string;
	time: Date;
}

export interface Post extends Document {
	readonly writerId: string;
	title: string;
	body: string;
	imageName: [string];
	comments: [Comment];
	created: Date;
}
