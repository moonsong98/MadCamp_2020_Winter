import React from 'react';
import { Theme, createStyles, makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import MultilineTextFields from '../components/App/MultilineText';
import DropzoneAreaExample from '../components/App/imageUp2';

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root1: {
			display: 'flex',
			flexWrap: 'wrap',
			'& > *': {
				margin: theme.spacing(1),
				width: theme.spacing(100),
				height: theme.spacing(10),
			},
		},
		root2: {
			display: 'flex',
			flexWrap: 'wrap',
			'& > *': {
				margin: theme.spacing(1),
				width: theme.spacing(100),
				height: theme.spacing(50),
			},
		},
	})
);

export default function SimplePaper() {
	const classes = useStyles();

	return (
		<div>
			<div className={classes.root1}>
				<Paper elevation={3}>
					<MultilineTextFields labelTxt="상품명" rowNum={1} />
				</Paper>
			</div>
			<div className={classes.root2}>
				<Paper elevation={3}>
					<MultilineTextFields labelTxt="상품설명" rowNum={17} />
				</Paper>
			</div>
			<DropzoneAreaExample />
		</div>
	);
}
