import { redis } from '../redis';
import { v4 } from 'uuid';

const minute = 60;

export const confirmEmailLink = async (userId: string) => {
	const id = v4();
	await redis.set(id, userId, 'ex', 60 * minute);

	return `${process.env.BASE_URL}/user/confirm/${id}`;
};
