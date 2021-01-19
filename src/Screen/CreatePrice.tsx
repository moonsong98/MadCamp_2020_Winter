import React, { useState } from 'react';
import { Theme, createStyles, makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import MultilineTextFields from '../components/App/MultilineText';
import DropzoneAreaExample from '../components/App/imageUp2';
import UploadButton from '../components/App/UploadButton';

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

	const [state, setState] = useState({
		title: '',
		content: '',
		files: [],
	});

	const setTitleCallback = (title: string) => {
		setState({ ...state, title });
		console.log(state);
	};

	const setContentCallback = (content: string) => {
		setState({ ...state, content });
		console.log(state);
	};

	const setFileCallback = (files: []) => {
		setState({ ...state, files });
		console.log(state);
	};

	return (
		<div>
			<div className={classes.root1}>
				<Paper elevation={3}>
					<MultilineTextFields labelTxt="상품명" rowNum={1} callBackFunc={setTitleCallback} />
				</Paper>
			</div>
			<div className={classes.root2}>
				<Paper elevation={3}>
					<MultilineTextFields labelTxt="상품설명" rowNum={17} callBackFunc={setContentCallback} />
				</Paper>
			</div>
			<DropzoneAreaExample callbackFunc={setFileCallback} />
			<UploadButton />
		</div>
	);
}
