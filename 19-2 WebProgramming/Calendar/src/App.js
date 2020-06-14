import React, {useState} from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
} from 'react-router-dom';

import {createMuiTheme, ThemeProvider} from '@material-ui/core/styles';
import blueGrey from '@material-ui/core/colors/blueGrey';

import Home from './pages/Home';
import MyCalendar from './pages/MyCalendar';
import FriendCalendar from './pages/FriendCalendar';

import firebase from './modules/firebase';

import './custom.scss';
import './App.css';

const App = () => {
  const [user, setUser] = useState();
  const theme = createMuiTheme({
    palette: {
      primary: blueGrey,
    },
    status: {
      danger: 'orange',
    },
  });

  firebase.auth().onAuthStateChanged((user) => {
    if (user) {
      setUser(user);
    } else {
      setUser();
    }
  });

  return (
    <Router>
      <ThemeProvider theme={theme}>
        <div id="app">
          <Switch>
            <Route path="/mycalendar">
              {
                user &&
                <MyCalendar user={user} />
              }
            </Route>
            <Route path="/friendcalendar/:uid">
              {
                user &&
                <FriendCalendar user={user} />
              }
            </Route>
            <Route path="/">
              <Home user={user} setUser={setUser} />
            </Route>
          </Switch>
        </div>
      </ThemeProvider>
    </Router>
  );
};

export default App;
