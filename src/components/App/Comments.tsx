import React, { useState } from 'react';
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
			maxWidth: 345,
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

function Comments() {
	const classes = useStyles();
	const [comments, setComments] = useState(dummyComments);
	const [commentCont, setCommentCont] = useState({
		content: '',
	});

	const getValue = (e: React.ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setCommentCont({
			...commentCont,
			[name]: value,
		});
		console.log(commentCont);
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
				/>
				<Button variant="contained" color="primary" className={classes.button} endIcon={<SendIcon />} />
			</div>
		</div>
	);
}

export default Comments;
