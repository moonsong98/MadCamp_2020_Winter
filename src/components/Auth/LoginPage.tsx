import React, { useEffect, useState } from 'react';
import { useLazyQuery, gql } from '@apollo/client';
import AuthForm from './Auth.components';

const LOGIN = gql`
	query Login($username: String!, $password: String!) {
		login(input: { username: $username, password: $password })
	}
`;

function LoginPage(): React.ReactElement {
	const [{ username, password }, setCredentials] = useState({
		username: '',
		password: '',
	});

	const [loginError, setLoginError] = useState('');

	const [loggedIn, setLoggedIn] = useState(false);

	const [checkLogin, { loading, data }] = useLazyQuery(LOGIN);

	console.log(data);
	console.log(loggedIn);

	useEffect(() => {
		if (!data) {
			setLoginError('');
		} else if (data && data.login === true) {
			setLoggedIn(true);
			setLoginError('');
		} else {
			setLoggedIn(false);
			setLoginError('Login Information Is Wrong');
		}
	}, [data]);

	const login = async (event: React.FormEvent) => {
		event.preventDefault();
		checkLogin({ variables: { username, password } });
	};
	if (loggedIn) {
		return (
			<div>
				<p>Logged IN!!</p>
			</div>
		);
	}

	if (loading)
		return (
			<div>
				<p>Loading...</p>
			</div>
		);
	return (
		<AuthForm onSubmit={login}>
			<label htmlFor="username">
				Username
				<input
					placeholder="Username"
					value={username}
					onChange={(event) =>
						setCredentials({
							username: event.target.value,
							password,
						})
					}
				/>
			</label>
			<label htmlFor="password">
				Password
				<input
					placeholder="Password"
					type="password"
					value={password}
					onChange={(event) =>
						setCredentials({
							username,
							password: event.target.value,
						})
					}
				/>
			</label>
			<button type="submit">Login</button>
			{loginError.length > 0 && <p>{loginError}</p>}
		</AuthForm>
	);
}

export default LoginPage;
