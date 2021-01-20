import { Theme, createStyles, makeStyles } from '@material-ui/core/styles';
import React, { useEffect, useState } from 'react';
import { Route } from 'react-router-dom';
import axios from 'axios';
import SimpleImageSlider from 'react-simple-image-slider';
import Paper from '@material-ui/core/Paper';
import Comments from '../components/App/Comments';

const imgs = [];

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
	const [images, setImages] = useState([{ url: '' }]);
	const url = 'http://192.249.18.238:5000';
	// useEffect(() => {
	// 	async function fetchData() {
	// 		axios.get(`${url}/post/all`).then((response) => {
	// 			console.log(response);
	// 			console.log(images.length);
	// 			if (images.length < 1) {
	// 				response.data[0].imageName.forEach((e) => {
	// 					console.log(`e: ${e}`);
	// 					setImages([
	// 						...images,
	// 						{
	// 							url: `${url}/post/getPostImage/${e}`,
	// 						},
	// 					]);
	// 					console.log([...images]);
	// 				});
	// 			}
	// 		});
	// 	}
	// 	fetchData();
	// }, [images]);
	const fetchPostUrl = 'http://192.249.18.238:5000/post/all';
	const [title, setTitle] = useState('');
	const [body, setBody] = useState('');
	async function fetchData() {
		const response = await axios.get(fetchPostUrl);
		setTitle(response.data[0].title);
		setBody(response.data[0].body);
		setImages(
			response.data[0].imageName.map((e) => {
				return {
					url: `${url}/post/getPostImage/${e}`,
				};
			})
		);
	}
	useEffect(() => {
		fetchData();
	}, [fetchPostUrl]);

	const classes = useStyles();
	return (
		<div>
			<Paper className={classes.paperCase} elevation={3} color="black">
				<div> {title} </div>
				<div className={classes.slider}>
					{images.length > 1 && <SimpleImageSlider width="30%" height={600} images={images} showBullets showNavs />}
				</div>
				<Paper className={classes.priceContent}>{body}</Paper>
			</Paper>
			<div className={classes.comments}>
				<Comments />
			</div>
		</div>
	);
}
/*
 */

export default PriceDetail;
