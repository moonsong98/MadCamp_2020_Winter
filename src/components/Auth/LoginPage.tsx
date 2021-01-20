import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import { createStyles, makeStyles, Theme } from '@material-ui/core/styles';
import { useLazyQuery, gql } from '@apollo/client';
import useAuthToken from '../../config/auth';
import AuthForm from './Auth.components';

const LOGIN = gql`
	query Login($username: String!, $password: String!) {
		login(input: { username: $username, password: $password })
	}
`;

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root: {
			'& .MuiTextField-root': {
				width: '25ch',
				marginBottom: '2ch',
			},
			button: {
				width: '25ch',
				padding: '5ch',
			},
			justifyContent: 'space-around',
			alignItems: 'center',
			position: 'fixed',
			top: '45%',
			left: '45%',
		},
	})
);

function LoginPage(props): React.ReactElement {
	const [{ username, password }, setCredentials] = useState({
		username: '',
		password: '',
	});

	// const [cookies, setCookie] = useCookies(['token']);
	const history = useHistory();
	const [_, setAuthToken] = useAuthToken();

	const [loginError, setLoginError] = useState('');

	const [loggedIn, setLoggedIn] = useState(false);

	const [checkLogin, { loading, data }] = useLazyQuery(LOGIN, {
		onCompleted: (_data) => {
			if (_data && _data.login !== '') {
				setAuthToken(_data.login);
				setLoggedIn(true);
			} else {
				setLoginError('입력하신 계정 혹은 비밀번호가 잘못되었습니다.');
			}
		},
	});

	// useEffect(() => {
	// 	if (!data) {
	// 		setLoginError('');
	// 	} else if (data && data.login === '') {
	// 		setLoggedIn(false);
	// 		setLoginError('Login Information Is Wrong');
	// 	} else {
	// 		setLoggedIn(true);
	// 		setLoginError('');
	// 		setCookie('token', data.login);
	// 		console.log(cookies);
	// 	}
	// }, [cookies, data, setCookie]);

	const login = async (event: React.FormEvent) => {
		event.preventDefault();
		checkLogin({ variables: { username, password } });
	};
	const classes = useStyles();
	if (loggedIn) {
		history.push('/');
		return (
			<div>
				<p>Logged IN!!</p>
			</div>
		);
	}

	if (loading) {
		return (
			<div>
				<p>Loading...</p>
			</div>
		);
	}
	return (
		<div className={classes.root}>
			<form onSubmit={login}>
				<div>
					<TextField
						id="standard-required"
						label="Username"
						value={username}
						onChange={(event) =>
							setCredentials({
								username: event.target.value,
								password,
							})
						}
						placeholder="Username"
					/>
				</div>
				<div>
					<TextField
						id="standard-required"
						label="Password"
						value={password}
						onChange={(event) =>
							setCredentials({
								username,
								password: event.target.value,
							})
						}
						placeholder="Password"
					/>
				</div>
				<Button className="button" variant="outlined" type="submit">
					Login
				</Button>
			</form>
			{loginError.length > 0 && <p className="error">{loginError}</p>}
		</div>
	);
}

export default LoginPage;
