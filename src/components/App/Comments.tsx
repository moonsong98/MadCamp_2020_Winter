import React, { useEffect, useState } from 'react';
import { makeStyles, Theme, createStyles } from '@material-ui/core/styles';
import Input from '@material-ui/core/Input';
import InputAdornment from '@material-ui/core/InputAdornment';
import AccountCircle from '@material-ui/icons/AccountCircle';
import { red } from '@material-ui/core/colors';
import Button from '@material-ui/core/Button';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Avatar from '@material-ui/core/Avatar';
import Typography from '@material-ui/core/Typography';
import SendIcon from '@material-ui/icons/Send';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { useQuery, gql } from '@apollo/client';
import UserName from '../../config/getUserName';
import UserId from '../../config/getUserId';

// interface commentsProps {
// 	commentList: Comment[];
// }

type Comment = { authName: string; content: string };

const dummyComments: Comment[] = [
	{ authName: 'a', content: 'a' },
	{ authName: 'b', content: 'b' },
	{ authName: 'c', content: 'c' },
	{ authName: 'd', content: 'd' },
];

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root: {
			marginTop: '1%',
			maxWidth: '100%',
			width: '100%',
		},
		media: {
			height: 0,
			paddingTop: '56.25%', // 16:9
		},
		expand: {
			transform: 'rotate(0deg)',
			marginLeft: 'auto',
			transition: theme.transitions.create('transform', {
				duration: theme.transitions.duration.shortest,
			}),
		},
		expandOpen: {
			transform: 'rotate(180deg)',
		},
		avatar: {
			backgroundColor: red[500],
		},
		titleInput: {
			width: '500px',
			height: '40px',
			margin: '10px',
		},
		inputLine: {
			margintTop: '1%',
			marginBottom: '1%',
		},
		button: {},
	})
);
const GET_USER_NAME = gql`
	query GetUser($id: String!) {
		getUser(id: $id) {
			name
		}
	}
`;

function Comments() {
	const fetchPostUrl = 'http://192.249.18.238:5000/post/all';
	const [postId, setPostId] = useState('');
	const [comments, setComments] = useState([{ authName: '', content: '' }]);
	const [writer, setWriter] = useState('');
	const userId = UserId();
	const { loading, error, data } = useQuery(GET_USER_NAME, {
		variables: { id: userId },
	});
	const [, updateState] = React.useState({});
	const forceUpdate = React.useCallback(() => updateState({}), []);
	async function fetchData() {
		const response = await axios.get(fetchPostUrl);
		// eslint-disable-next-line no-underscore-dangle
		setPostId(response.data[0]._id);
		setComments(
			response.data[0].comments.map((e) => {
				return {
					authName: e.name,
					content: e.body,
				};
			})
		);

		// setImages(
		// 	response.data[0].imageName.map((e) => {
		// 		return {
		// 			url: `${url}/post/getPostImage/${e}`,
		// 		};
		// 	})
		// );
	}
	useEffect(() => {
		if (!loading) {
			setWriter(data.getUser.name);
		}
	}, [loading, data]);
	useEffect(() => {
		fetchData();
		// setWriter(UserName(UserId()));
	}, [fetchPostUrl]);

	const classes = useStyles();
	const [commentCont, setCommentCont] = useState({
		content: '',
	});

	const getValue = (e: React.ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setCommentCont({
			...commentCont,
			content: value,
		});
		console.log(commentCont);
	};

	const handleButtonClick = () => {
		axios.post('http://192.249.18.238:5000/post/createcomment', {
			_id: postId,
			name: writer,
			body: commentCont.content,
		});
		forceUpdate();
		fetchData();
	};
	return (
		<div>
			{comments.map((com) => (
				<div>
					<Card className={classes.root}>
						<CardHeader
							avatar={
								<Avatar aria-label="recipe" className={classes.avatar}>
									{com.authName[0]}
								</Avatar>
							}
							title={com.authName}
							subheader="September 14, 2016"
						/>
						<CardContent>
							<Typography variant="body2" color="textSecondary" component="p">
								{com.content}
							</Typography>
						</CardContent>
					</Card>
				</div>
			))}
			<div className={classes.inputLine}>
				<Input
					id="input-with-icon-adornment"
					startAdornment={
						<InputAdornment position="start">
							<AccountCircle />
						</InputAdornment>
					}
					fullWidth
					onChange={getValue}
				/>
				<Button
					variant="contained"
					color="primary"
					className={classes.button}
					endIcon={<SendIcon />}
					onClick={handleButtonClick}
				/>
			</div>
		</div>
	);
}

export default Comments;
