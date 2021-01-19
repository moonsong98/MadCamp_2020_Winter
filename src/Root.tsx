import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import LoginPage from './components/Auth/LoginPage';
import RegisterPage from './components/Auth/RegisterPage';
import ConfirmPage from './components/Auth/ConfirmPage';
import App from './Screen/App';
import Upload from './Screen/TestPage';

function Root() {
	return (
		<Router>
			<Route exact path="/" component={App} />
			<Route path="/login" component={LoginPage} />
			<Route path="/register" component={RegisterPage} />
			<Route path="/signup/confirm/:redisId" component={ConfirmPage} />
			<Route path="/test" component={Upload} />
		</Router>
	);
}

export default Root;
