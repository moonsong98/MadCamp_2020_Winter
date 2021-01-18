import React from 'react';
import { gql, useMutation } from '@apollo/client';
import { RouteComponentProps } from 'react-router-dom';

const CONFIRM_SIGNUP = gql`
	mutation ConfrimUser($redisId: String!) {
		confirmUser(redisId: $redisId)
	}
`;

function ConfirmPage({ match }: RouteComponentProps) {
	console.log(match.params.redisId);
	const [confirmSignup, { data }] = useMutation(CONFIRM_SIGNUP);
	confirmSignup({ variables: { redisId: match.params.redisId } });
	return (
		<div>
			<p>Confirm Page</p>
		</div>
	);
}

export default ConfirmPage;
