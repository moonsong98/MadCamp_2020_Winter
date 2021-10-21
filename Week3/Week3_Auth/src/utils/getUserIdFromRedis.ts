import { redis } from '../redis';

export const getUserIdFromRedis = async (redisId: string): Promise<string> => {
	let userId = '';
	console.log(redisId);
	await redis.get(
		redisId,
		await function (err, result) {
			if (err) {
				console.error(err);
			} else {
				console.log(`result: ${result}`);
				userId = result;
			}
		}
	);
	console.log(userId);
	return userId;
};
