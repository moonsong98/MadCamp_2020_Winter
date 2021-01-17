import React, { useState } from 'react';
import { useMutation, gql } from '@apollo/client';
import { notify } from 'react-notify-toast';
import AuthForm from './Auth.components';

const CREATE_USER = gql`
	mutation CreateUser($username: String!, $password: String!, $name: String!, $email: String!) {
		createUser(input: { username: $username, password: $password, name: $name, email: $email }) {
			id
			username
			password
			name
			email
			confirmed
		}
	}
`;

function RegisterPage() {
	const [registerState, setRegisterData] = useState({
		username: '',
		password: '',
		repeatPassword: '',
		name: '',
		email: '',
	});

	const [sendingEmail, setSendingEmail] = useState(false);
	const [passwordError, setPasswordError] = useState('');
	const [createUser, { data }] = useMutation(CREATE_USER);

	const register = (event: React.FormEvent) => {
		event.preventDefault();
		if (registerState.password === registerState.repeatPassword) {
			setPasswordError('');
			createUser({ variables: { ...registerState } });
		} else {
			setPasswordError('password and repeat password should match');
		}
	};

	return (
		<AuthForm onSubmit={register}>
			<label htmlFor="username">
				username
				<input
					value={registerState.username}
					name="username"
					onChange={(event) =>
						setRegisterData({
							...registerState,
							username: event.target.value,
						})
					}
				/>
			</label>
			<label htmlFor="password">
				password
				<input
					value={registerState.password}
					name="password"
					type="password"
					onChange={(event) =>
						setRegisterData({
							...registerState,
							password: event.target.value,
						})
					}
				/>
			</label>
			<label htmlFor="repeatPassword">
				repeat password
				<input
					value={registerState.repeatPassword}
					name="repeatPassword"
					type="password"
					onChange={(event) =>
						setRegisterData({
							...registerState,
							repeatPassword: event.target.value,
						})
					}
				/>
			</label>
			<label htmlFor="name">
				name
				<input
					value={registerState.name}
					name="name"
					onChange={(event) =>
						setRegisterData({
							...registerState,
							name: event.target.value,
						})
					}
				/>
			</label>
			<label htmlFor="email">
				email
				<input
					value={registerState.email}
					name="email"
					onChange={(event) =>
						setRegisterData({
							...registerState,
							email: event.target.value,
						})
					}
				/>
			</label>
			<button type="submit">Register</button>
			{passwordError.length > 0 && <p>{passwordError}</p>}
		</AuthForm>
	);
}

export default RegisterPage;
