import React from 'react';
import Banner from '../components/App/Banner';
import RowInPost from '../components/App/RowInPost';

// interface PostProps{
//     title: string,
//     content: string,
//     comments?: string[],
//     images?: string[],
//     videos?: string[]
// }

function PostDetail() {
	return (
		<div>
			<Banner />
			<RowInPost title="About Us" />
		</div>
	);
}

export default PostDetail;
