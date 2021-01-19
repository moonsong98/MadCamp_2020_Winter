import { ApolloClient, InMemoryCache, ApolloLink, HttpLink } from '@apollo/client';
import useAuthToken from './auth';

const httpLink = new HttpLink({ uri: 'http://192.249.18.238:8000/graphql' });

const authMiddleware = (authToken) =>
	new ApolloLink((operation, forward) => {
		// add the authorization to the headers
		if (authToken) {
			operation.setContext({
				headers: {
					authorization: `Bearer ${authToken}`,
				},
			});
		}

		return forward(operation);
	});

const cache = new InMemoryCache({});

const useAppApolloClient = () => {
	const [authToken] = useAuthToken();
	return new ApolloClient({
		link: authMiddleware(authToken).concat(httpLink),
		cache,
	});
};

export default useAppApolloClient;
