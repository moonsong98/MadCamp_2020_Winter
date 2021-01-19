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
			top: '-50px',
		},
		grid: {
			marginLeft: '25px',
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
					<div className={classes.grid}>
						<NestedGrid />
					</div>
				</Paper>
			</div>
		</div>
	);
}

export default PostPrices;
