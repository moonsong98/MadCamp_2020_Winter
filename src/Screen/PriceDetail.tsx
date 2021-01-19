import { Theme, createStyles, makeStyles } from '@material-ui/core/styles';
import React from 'react';
import { Route } from 'react-router-dom';
import SimpleImageSlider from 'react-simple-image-slider';
import Paper from '@material-ui/core/Paper';
import Comments from '../components/App/Comments';

const imgs = [
	{
		url:
			'https://mobiinsidecontent.s3.ap-northeast-2.amazonaws.com/kr/wp-content/uploads/2020/02/10160353/GettyImages-1131776334-scaled.jpg',
	},
	{ url: 'https://www.lcinternational.it/wp-content/uploads/Start_up.jpg' },
];

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		paperCase: {
			display: 'flex',
			margin: '0 auto',
			width: '70%',
			height: theme.spacing(80),
		},
		slider: {
			marginTop: '1%',
			marginLeft: '3%',
			float: 'left',
		},
		priceContent: {
			width: '40%',
			height: '95%',
			float: 'left',
			marginTop: '1%',
			marginLeft: '50%',
		},
		comments: {
			margin: '0 auto',
			width: '70%',
		},
	})
);

function PriceDetail() {
	const classes = useStyles();
	return (
		<div>
			<Paper className={classes.paperCase} elevation={3} color="black">
				<div> asdf </div>
				<div className={classes.slider}>
					<SimpleImageSlider width="30%" height={600} images={imgs} showBullets showNavs />
				</div>
				<Paper className={classes.priceContent}>asdasdfsdfsfsadfsad</Paper>
			</Paper>
			<div className={classes.comments}>
				<Comments />
			</div>
		</div>
	);
}

export default PriceDetail;
