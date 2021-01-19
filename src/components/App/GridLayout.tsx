import React from 'react';
import { makeStyles, createStyles, Theme } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import { Link } from 'react-router-dom';

const itemList = [
	{ itemName: 'Jeans', itemPrice: '200,000' },
	{ itemName: 'Black Jeans', itemPrice: '200,000' },
	{ itemName: 'Jeans', itemPrice: '200,000' },
	{ itemName: 'Ripped Jeans', itemPrice: '200,000' },
	{ itemName: 'Shorts', itemPrice: '100,000' },
	{ itemName: 'White Shorts', itemPrice: '100,000' },
	{ itemName: 'Black Shorts', itemPrice: '100,000' },
	{ itemName: 'Cap', itemPrice: '50,000' },
	{ itemName: 'white Cap', itemPrice: '50,000' },
	{ itemName: 'Yellow Cap', itemPrice: '50,000' },
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
	})
);

export default function NestedGrid() {
	const classes = useStyles();
	const lis: JSX.Element[] = [];
	const rowNum = itemList.length / 3;

	const style = {
		flexDirection: 'column',
	};

	function FormRow(props: priceRowProps) {
		const { rowIndex } = props;
		return (
			<>
				<Grid item xs={4}>
					<Paper className={classes.paper}>
						<div>
							<Link to="/pricedetail">
								<img alt="이미지 준비중" />
							</Link>
						</div>
						<div>{itemList[rowIndex * 3].itemName}</div>
						<div>{itemList[rowIndex * 3].itemPrice}</div>
					</Paper>
				</Grid>

				{rowIndex * 3 + 1 < itemList.length && (
					<Grid item xs={4}>
						<Paper className={classes.paper}>
							<div>
								<Link to="/pricedetail">
									<img alt="이미지 준비중" />
								</Link>
							</div>
							<div>{itemList[rowIndex * 3 + 1].itemName}</div>
							<div>{itemList[rowIndex * 3 + 1].itemPrice}</div>
						</Paper>
					</Grid>
				)}
				{rowIndex * 3 + 2 < itemList.length && (
					<Grid item xs={4}>
						<Paper className={classes.paper}>
							<div>
								<Link to="/pricedetail">
									<img alt="이미지 준비중" />
								</Link>
							</div>
							<div>{itemList[rowIndex * 3 + 2].itemName}</div>
							<div>{itemList[rowIndex * 3 + 2].itemPrice}</div>
						</Paper>
					</Grid>
				)}
			</>
		);
	}
	let i: number;
	for (i = 0; i < rowNum; i += 1) {
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
