import React, { useEffect, useState } from 'react';
import { makeStyles, createStyles, Theme } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { ImageSearch } from '@material-ui/icons';

const itemList = [
	{ itemName: 'Jeans', itemSource: 'http://192.249.18.238:5000/post/getDummyImage/1.jpeg' },
	{ itemName: 'Black Jeans', itemSource: 'http://192.249.18.238:5000/post/getDummyImage/2.jpeg' },
	{ itemName: 'Jeans', itemSource: 'http://192.249.18.238:5000/post/getDummyImage/3.jpeg' },
	{ itemName: 'Ripped Jeans', itemSource: 'http://192.249.18.238:5000/post/getDummyImage/4.jpeg' },
	{ itemName: 'Shorts', itemSource: 'http://192.249.18.238:5000/post/getDummyImage/5.jpeg' },
	{ itemName: 'White Shorts', itemSource: 'http://192.249.18.238:5000/post/getDummyImage/6.jpeg' },
];

interface priceRowProps {
	rowIndex: number;
}

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root: {
			flexGrow: 1,
		},
		paper: {
			padding: theme.spacing(1),
			textAlign: 'center',
			color: theme.palette.text.secondary,
		},
		image: {
			width: '200px',
			height: 'auto',
		},
	})
);

export default function NestedGrid() {
	const classes = useStyles();
	const lis: JSX.Element[] = [];
	const rowNum = itemList.length / 3;

	const style = {
		flexDirection: 'column',
	};
	const [images, setImages] = useState([...itemList]);
	const url = 'http://192.249.18.238:5000';
	useEffect(() => {
		async function fetchData() {
			axios.get(`${url}/post/all`).then((response) => {
				if (images.length < 7) {
					setImages([
						...images,
						{
							itemName: response.data[0].title,
							itemSource: `${url}/post/getPostImage/${response.data[0].imageName[0]}`,
						},
					]);
				}
				console.log(images.length);
				// itemList.push({
				// 	itemName: response.data[0].title,
				// 	itemSource: `${url}/post/getPostImage/${response.data[0].imageName[0]}`,
				// });
			});
		}
		fetchData();
	}, [images]);

	function FormRow(props: priceRowProps) {
		const { rowIndex } = props;
		return (
			<>
				<Grid item xs={4}>
					<Paper className={classes.paper}>
						<div>
							<Link to="/pricedetail">
								<img className={classes.image} alt="이미지 준비중" src={images[rowIndex * 3].itemSource} />
							</Link>
						</div>
						<div>{images[rowIndex * 3].itemName}</div>
					</Paper>
				</Grid>

				{rowIndex * 3 + 1 < images.length && (
					<Grid item xs={4}>
						<Paper className={classes.paper}>
							<div>
								<Link to="/pricedetail">
									<img className={classes.image} alt="이미지 준비중" src={images[rowIndex * 3 + 1].itemSource} />
								</Link>
							</div>
							<div>{images[rowIndex * 3 + 1].itemName}</div>
						</Paper>
					</Grid>
				)}
				{rowIndex * 3 + 2 < images.length && (
					<Grid item xs={4}>
						<Paper className={classes.paper}>
							<div>
								<Link to="/pricedetail">
									<img className={classes.image} alt="이미지 준비중" src={images[rowIndex * 3 + 2].itemSource} />
								</Link>
							</div>
							<div>{images[rowIndex * 3 + 2].itemName}</div>
						</Paper>
					</Grid>
				)}
			</>
		);
	}
	let i: number;
	for (i = 0; i < images.length / 3; i += 1) {
		lis.push(
			<Grid container item xs={12} spacing={3}>
				<FormRow rowIndex={i} />
			</Grid>
		);
	}

	return (
		<div className={classes.root}>
			<Grid container spacing={1}>
				{lis}
			</Grid>
		</div>
	);
}
