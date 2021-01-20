import React, { useState } from 'react';
import { Theme, createStyles, makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import CloudUploadIcon from '@material-ui/icons/CloudUpload';
import Button from '@material-ui/core/Button';
import axios from 'axios';
import MultilineTextFields from '../components/App/MultilineText';
import DropzoneAreaExample from '../components/App/imageUp2';
import UserId from '../config/getUserId';

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root: {
			width: '50%',
			float: 'left',
		},
		root1: {
			display: 'flex',
			flexWrap: 'wrap',
			'& > *': {
				margin: theme.spacing(1),
				width: '100%',
				height: theme.spacing(10),
			},
		},
		root2: {
			display: 'flex',
			flexWrap: 'wrap',
			'& > *': {
				margin: theme.spacing(1),
				width: '100%',
				height: theme.spacing(50),
			},
		},
		root3: {
			width: '50%',
			float: 'right',
			marginTop: '0.5%',
		},
		button: {
			margin: theme.spacing(1),
		},
	})
);

export default function SimplePaper() {
	const classes = useStyles();
	const url = 'http://192.249.18.238:5000';
	let files: Blob[] = [];

	const [state, setState] = useState({
		title: '',
		content: '',
		// files: Blob[],
	});

	const writerId = UserId();

	const setTitleCallback = (title: string) => {
		setState({ ...state, title });
		console.log(state);
	};

	const setContentCallback = (content: string) => {
		setState({ ...state, content });
		console.log(state);
	};

	const setFileCallback = (_files: Blob[]) => {
		files = _files;
		console.log(state);
	};

	const createPost = () => {
		const formData = new FormData();
		console.log(files.length);
		files.forEach((f) => {
			formData.append('image', f);
		});
		formData.append('writerId', writerId);
		formData.append('title', state.title);
		formData.append('body', state.content);
		// axios.post(`${url}/post/createpost`, {
		// 	/* has to get userId */
		// 	writerId: '123',
		// 	title: state.title,
		// 	body: state.content,
		// });
		console.log(formData);
		axios.post(`${url}/post/createpost`, formData, {
			headers: {
				'Content-Type': 'multipart/form-data',
			},
		});
	};

	return (
		<div>
			<div className={classes.root}>
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
				<Button
					variant="contained"
					color="default"
					className={classes.button}
					startIcon={<CloudUploadIcon />}
					onClick={createPost}
				>
					Upload
				</Button>
			</div>
			<div className={classes.root3}>
				<DropzoneAreaExample callbackFunc={setFileCallback} />
			</div>
		</div>
	);
}
