import React from 'react';
import ReactDOM from 'react-dom';
import './design/index.css';
import { ApolloClient, ApolloProvider, InMemoryCache } from '@apollo/client';
import { createUploadLink } from 'apollo-upload-client';
import { CookiesProvider } from 'react-cookie';
import Root from './Root';

const client = new ApolloClient({
	cache: new InMemoryCache(),
	link: createUploadLink({
		uri: 'http://192.249.18.238:8000/graphql',
	}),
});

ReactDOM.render(
	<React.StrictMode>
		<CookiesProvider>
			<ApolloProvider client={client}>
				<Root />
			</ApolloProvider>
		</CookiesProvider>
	</React.StrictMode>,
	document.getElementById('root')
);
