import React from 'react';
import ReactDOM from 'react-dom';
import './design/index.css';
import { ApolloClient, ApolloProvider, HttpLink, InMemoryCache } from '@apollo/client';
import { CookiesProvider } from 'react-cookie';
import Root from './Root';

const client = new ApolloClient({
	cache: new InMemoryCache(),
	link: new HttpLink({
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
