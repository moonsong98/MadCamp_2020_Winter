import React, { useContext, useState } from 'react';
import { Button, Paper, TextField } from '@material-ui/core';
import { createStyles, fade, makeStyles, Theme } from '@material-ui/core/styles';
import { RestaurantOwnerRegister } from '../types/AuthTypes';
import axios from 'axios';
import UserContext from '../contexts/UserContext';
import { SERVER_URL } from '../config';

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		createAccount: {
			display: 'flex',
			flexDirection: 'column',
			width: '40%',
			padding: '1.5rem',
			// justifyContent: 'space-between',
			// marginLeft: '20%',
			// marginRight: '20%',
		},
	})
);

function AdminManagementPage() {
	const [restaurantOwnerRegisterInformation, setRestaurantOnwerRegisterInformation] = useState<RestaurantOwnerRegister>(
		{ registerNumber: '', password: '' }
	);
	const [password, setPassword] = useState('');
	const classes = useStyles();

	const { userStatus } = useContext(UserContext);

	const createRestaurantOwnerAccount = (event: React.FormEvent) => {
		event?.preventDefault();
		const createdPassword = Math.random().toString(36).slice(-8);
		setPassword(createdPassword);
		axios({
			method: 'post',
			url: `${SERVER_URL}/auth/register`,
			data: {
				username: restaurantOwnerRegisterInformation.registerNumber,
				password: createdPassword,
				role: 'restaurantOwner',
			},
		}).then((res) => console.log(createdPassword));
	};
	return (
		<form onSubmit={createRestaurantOwnerAccount}>
			<div>
				<Paper className={classes.createAccount}>
					<TextField
						label="사업자등록번호"
						placeholder="사업자등록번호를 입력하시오"
						value={restaurantOwnerRegisterInformation.registerNumber}
						onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
							setRestaurantOnwerRegisterInformation({
								...restaurantOwnerRegisterInformation,
								registerNumber: event.target.value,
							});
						}}
					/>
					<TextField disabled label="비밀번호" placeholder="생성된 비밀번호" value={password} />
					<Button type="submit">Create New Restaurant Owner's Account</Button>
				</Paper>
			</div>
		</form>
	);
}

export default AdminManagementPage;
