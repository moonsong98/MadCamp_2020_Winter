import { Button, Divider, List, ListItemText, TextField } from '@material-ui/core';
import axios from 'axios';
import React, { ReactComponentElement, useContext, useEffect, useState } from 'react';
import { RouteComponentProps, useHistory, useRouteMatch } from 'react-router-dom';
import { SERVER_URL } from '../config';
import UserContext from '../contexts/UserContext';
import { Restaurant } from '../types/RestaurantTypes';
import RestaurantPage from './RestaurantPage';

// interface Props {
// 	index: number;
// 	confirmedRestaurantList: Restaurant[];
// 	setConfirmedRestaurantList: (restaurant: Restaurant[]) => void;
// 	notConfirmedRestaurantList: Restaurant[];
// 	setNotConfirmedRestaurantList: (restaurant: Restaurant[]) => void;
// }

interface matchType {
	restaurantId: string;
}

function ConfirmRestaurantPage() {
	console.log('reached Here');
	const [restaurant, setRestaurant] = useState<Restaurant>({
		name: '',
		category: '',
		menus: [],
		telephone: '',
		description: '',
		openingHours: [
			{
				openTime: '',
				closeTime: '',
			},
			{
				openTime: '',
				closeTime: '',
			},
			{
				openTime: '',
				closeTime: '',
			},
			{
				openTime: '',
				closeTime: '',
			},
			{
				openTime: '',
				closeTime: '',
			},
			{
				openTime: '',
				closeTime: '',
			},
			{
				openTime: '',
				closeTime: '',
			},
		],
		location: {
			fullAddress: '',
			extraAddress: '',
		},
	});
	const match = useRouteMatch<matchType>();
	const url = `${SERVER_URL}/restaurant/${match.params.restaurantId}`;
	const history = useHistory();
	const { userStatus } = useContext(UserContext);
	useEffect(() => {
		axios({
			method: 'get',
			url,
		}).then((res) => {
			setRestaurant(res.data);
		});
	}, [url]);
	return (
		<div>
			<form
				onSubmit={(event) => {
					event.preventDefault();
					const formData = new FormData();
					formData.append('restaurant', JSON.stringify(restaurant));
					axios
						.put(url, formData, {
							headers: {
								'Content-Type': 'multipart/form-data',
								token: userStatus.accessToken,
							},
						})
						.then((res) => {
							history.goBack();
						})
						.catch((err) => {
							console.log(`err: ${err}`);
						});
				}}
			>
				<RestaurantPage restaurantId={match.params.restaurantId} />
				{/* {restaurant?.name && <TextField disabled label="?????????" value={restaurant.name} />}
				{restaurant?.category && <TextField disabled label="????????????" value={restaurant.category} />}
				{restaurant?.description && <TextField disabled label="?????? ??????" value={restaurant.description} />}
				{restaurant?.telephone && <TextField disabled label="????????????" value={restaurant.telephone} />}
				{restaurant?.openingHours && (
					<List>
						{restaurant.openingHours.map((e) => {
							return (
								<div>
									<ListItemText primary={`${e.openTime}`} />
									<ListItemText primary={`${e.closeTime}`} />
									<Divider />
								</div>
							);
						})}
					</List>
				)}
				{restaurant?.menus && (
					<List>
						{restaurant.menus.map((e) => {
							return (
								<div>
									<ListItemText primary={`${e.name}`} />
									<ListItemText primary={`${e.description}`} />
									{e.sizes &&
										e.sizes.map((el) => {
											<ListItemText primary={`${el.size} ${el.price}`} />;
										})}
								</div>
							);
						})}
					</List>
				)}
				{restaurant?.location && (
					<TextField
						disabled
						label="??????"
						value={`${restaurant.location.fullAddress} ${restaurant.location.extraAddress}`}
					/>
				)} */}
				<Button
					type="submit"
					onClick={() => {
						setRestaurant({ ...restaurant, confirmed: true });
					}}
				>
					??????
				</Button>
			</form>
		</div>
	);
}

export default ConfirmRestaurantPage;
