import { useQuery, gql } from '@apollo/client';

const GET_USER_NAME = gql`
	query GetUser($id: String!) {
		getUser(id: $id) {
			name
		}
	}
`;

const UserName = (id: string) => {
	const { loading, error, data } = useQuery(GET_USER_NAME, {
		variables: { id },
	});
	if (!loading) {
		return data.getUser.name;
		/* */
	}
	if (error) {
		console.error(`@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@${error}`);
		return '';
	}
	return 'dm';
};
export default UserName;
