import React from 'react';

import { BrowserRouter as Router, Route, Redirect, Switch } from 'react-router-dom';

import Chat from './components/Chat/Chat';

const App = () => {
	const [ token, setToken ] = React.useState(false);

	React.useEffect(() => {
		if (localStorage.getItem('__key')) {
			setToken(true);
		}
	}, []);

	return (
		<Router>
			<Switch>
				<Route exact path="/" render={() => <Chat token={token} setToken={setToken} />} />
				<Redirect to="/" />
			</Switch>
		</Router>
	);
};

export default App;
