export interface CreatePostDTO {
	writerId: string;
	title: string;
	body: string;
}

export interface CreateCommentDTO {
	_id: string;
	name: string;
	body: string;
}
