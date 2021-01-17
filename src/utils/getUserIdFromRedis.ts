import { redis } from '../redis';

export const getUserIdFromRedis = (redisId: string) => {
	redis.get(redisId, function (err, result) {
		if (err) {
			console.error(err);
		} else {
			console.log(result);
		}
	});
};
