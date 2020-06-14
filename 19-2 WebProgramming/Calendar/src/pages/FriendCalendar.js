import React from 'react';
import {useParams} from 'react-router-dom';
import PropTypes from 'prop-types';

import {Row, Col} from 'reactstrap';

import MainLayout from '../components/MainLayout';
import Calendar from '../components/Calendar';

import firebaseHandler from '../modules/firebaseHandler';

const FriendCalendar = ({user}) => {
  const {uid} = useParams();
  const [hostOfCalendar, setHostOfCalendar] = React.useState('');
  const fetchHost = async () => {
    const data = await firebaseHandler.getUser(uid);
    setHostOfCalendar(data[0].name);
  };
  React.useEffect(() => {
    fetchHost();
  }, [uid]);
  return (
    <div className="h-100">
      <MainLayout user={user} title={`${hostOfCalendar}님의 캘린더`} />
      <div className="pt-4">
        <Row noGutters className="h-100">
          <Col xl="3" lg="3" md="2" sm="1" xs="0" />
          <Col className="d-flex align-items-center">
            <Calendar user={user} host={uid} mine={false} />
          </Col>
          <Col xl="3" lg="3" md="2" sm="1" xs="0" />
        </Row>
      </div>
    </div>
  );
};

FriendCalendar.propTypes = {
  user: PropTypes.shape({
    uid: PropTypes.string,
    email: PropTypes.string,
    displayName: PropTypes.string,
  }).isRequired,
};

export default FriendCalendar;
