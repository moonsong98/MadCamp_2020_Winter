import React from 'react';
import { makeStyles, createStyles, Theme } from '@material-ui/core/styles';
import { Route, useLocation } from 'react-router-dom';
import queryString from 'query-string';
import Paper from '@material-ui/core/Paper';
import BannerPrice from '../components/App/BannerPrice';
import NestedGrid from '../components/App/GridLayout';

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		banner: {
			position: 'relative',
		},
		gridCover: {
			position: 'relative',
			margin: '0 auto',
			width: '80%',
			marginTop: '-100px',
		},
		grid: {
			position: 'relative',
			marginLeft: '25px',
		},
		title: {
			margin: '0 auto',
			textAlign: 'center',
			marginBottom: '10px',
			fontSize: '20px',
		},
		content: {
			margin: '0 auto',
			textAlign: 'center',
			marginBottom: '10px',
			fontSize: '20px',
		},
	})
);

function PostPrices() {
	const classes = useStyles();
	const location = useLocation();
	const { search } = location;
	const queryObj = queryString.parse(search);
	const { imgPath } = queryObj;
	console.log(location);
	console.log(imgPath);
	let imgPathStr = '';
	if (imgPath != null) {
		imgPathStr = imgPath.toString();
	}
	return (
		<div>
			<div>
				<BannerPrice imgSrc={imgPathStr} />
			</div>
			<div className={classes.gridCover}>
				<Paper>
					<div className={classes.title}>
						<h1>제목</h1>
					</div>
					<div className={classes.content}>
						<h2>내용~~</h2>
					</div>
					<div className={classes.grid}>
						<NestedGrid />
					</div>
				</Paper>
			</div>
		</div>
	);
}

export default PostPrices;
