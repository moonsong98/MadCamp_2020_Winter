import React from 'react';
import { makeStyles, createStyles, Theme } from '@material-ui/core/styles';
import Banner from '../components/App/Banner';
import RowInPost from '../components/App/RowInPost';

// interface PostProps{
//     title: string,
//     content: string,
//     comments?: string[],
//     images?: string[],
//     videos?: string[]
// }

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		aboutUsImg: {
			width: '100%',
		},
	})
);

function PostDetail() {
	const classes = useStyles();

	return (
		<div>
			<Banner />
			<div>
				<RowInPost title="About Us" />
				<img
					className={classes.aboutUsImg}
					src="https://www.vapulus.com/en/wp-content/uploads/2018/11/startup.png"
					alt="이미지 준비중"
				/>
			</div>
		</div>
	);
}

export default PostDetail;
