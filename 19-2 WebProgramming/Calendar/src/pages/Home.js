import React from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';

import {Fade, Button} from 'reactstrap';

import firebase from '../modules/firebase';
import firebaseHandler from '../modules/firebaseHandler';

import './css/Home.css';

const Home = ({user}) => {
  const googleLogin = async () => {
    const provider = new firebase.auth.GoogleAuthProvider();
    firebase.auth().signInWithPopup(provider);
  };
  const logout = async () => {
    firebase.auth().signOut();
  };
  const checkUser = async () => {
    firebaseHandler.checkUser(
        user.uid,
        user.email,
        user.displayName
    );
  };
  return (
    <div id="home" className="d-flex flex-column
      justify-content-center align-items-end px-5">
      <div className="d-flex flex-column align-items-center
        text-light mr-5 mb-5 h4">
        <Fade in={true}>
        </Fade>
        {
          !user &&
          <Button outline color="light" className="mr-1" onClick={googleLogin}>
            <div className="d-flex align-items-center">
              <img src="/google_logo.png"
                alt="google_logo"
                id="google-logo" className="mr-2"
              />
              구글 아이디로 로그인
            </div>
          </Button>
        }
        {
          user &&
          <span className="mb-3">{user.displayName}님 안녕하세요</span>
        }
        {
          user &&
          <div className="d-flex">
            <Link to="/mycalendar">
              <Button color="blueGrey" className="mr-2" onClick={checkUser}>
                메인으로
              </Button>
            </Link>
            <Button color="dark" onClick={logout}>로그아웃</Button>
          </div>
        }
      </div>
    </div>
  );
};

Home.propTypes = {
  user: PropTypes.shape({
    uid: PropTypes.string,
    email: PropTypes.string,
    displayName: PropTypes.string,
  }),
};

export default Home;
