import * as mongoose from 'mongoose';

export const PostSchema = new mongoose.Schema({
	writerId: String,
	title: String,
	body: String,
	imageName: [String],
	comments: [
		{
			name: String,
			body: String,
			time: {
				type: Date,
				default: Date.now,
			},
		},
	],
	created: {
		type: Date,
		default: Date.now,
	},
});
