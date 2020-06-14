import React from 'react';
import {Row, Col} from 'reactstrap';
import PropTypes from 'prop-types';

import MainLayout from '../components/MainLayout';
import Calendar from '../components/Calendar';

const MyCalendar = ({user}) => {
  return (
    <div className="h-100">
      <MainLayout user={user} title='내 캘린더' />
      <div className="pt-4">
        <Row noGutters className="h-100">
          <Col xl="3" lg="3" md="2" sm="1" xs="0" />
          <Col className="d-flex align-items-center">
            <Calendar user={user} host={user.uid} mine={true} />
          </Col>
          <Col xl="3" lg="3" md="2" sm="1" xs="0" />
        </Row>
      </div>
    </div>
  );
};

MyCalendar.propTypes = {
  user: PropTypes.shape({
    uid: PropTypes.string,
    email: PropTypes.string,
    displayName: PropTypes.string,
  }).isRequired,
};

export default MyCalendar;
