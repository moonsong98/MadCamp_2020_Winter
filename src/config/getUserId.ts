import useAuthToken from './auth';

const parseJwt = (token) => {
	try {
		return JSON.parse(atob(token.split('.')[1]));
	} catch (e) {
		return null;
	}
};

const UserId = () => {
	const [authToken] = useAuthToken();
	let decoded;
	if (authToken) {
		decoded = parseJwt(authToken);
		return decoded.userId;
	}
	return null;
};
export default UserId;
