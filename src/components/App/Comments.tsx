import React, { useState } from 'react';
import { Link } from 'react-router-dom';

// interface commentsProps {
// 	commentList: Comment[];
// }

type Comment = { authName: string; content: string };

const dummyComments: Comment[] = [
	{ authName: 'a', content: 'a' },
	{ authName: 'b', content: 'b' },
	{ authName: 'c', content: 'c' },
	{ authName: 'd', content: 'd' },
];

function Comments() {
	const [comments, setComments] = useState(dummyComments);
	const [commentCont, setCommentCont] = useState({
		content: '',
	});

	const getValue = (e: React.ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setCommentCont({
			...commentCont,
			[name]: value,
		});
		console.log(commentCont);
	};
	return (
		<div>
			{comments.map((com) => (
				<div>
					{com.authName}
					{com.content}
				</div>
			))}
			<div className="form-wrapper">
				<input className="title-input" type="text" placeholder="리뷰 입력" onChange={getValue} name="content" />
			</div>
		</div>
	);
}

export default Comments;
