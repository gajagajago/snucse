import React from 'react';
import PropTypes from 'prop-types';

import {Modal, ModalHeader, ModalBody,
  TabContent, TabPane, Nav, NavItem, NavLink} from 'reactstrap';

import Diary from './Diary';
import Selfie from './Selfie';
import Photo from './Photo';
import Schedule from './Schedule';

import './css/DayDialog.css';

const DayDialog = ({date, host, mine, toggle, open}) => {
  const [activeTab, setActiveTab] = React.useState('diary');

  const toggleTab = (tab) => {
    if (activeTab !== tab) setActiveTab(tab);
  };
  return (
    <Modal toggle={toggle} isOpen={open}>
      <ModalHeader toggle={toggle}>
        {
          date ?
          `${date.getFullYear()}년 ${date.getMonth()+1}월 ${date.getDate()}일` :
          ''
        }
      </ModalHeader>
      <ModalBody>
        <Nav tabs>
          <NavItem>
            <NavLink
              className={activeTab === 'diary' ? 'active' : ''}
              onClick={() => {
                toggleTab('diary');
              }}
            >
              Diary
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink
              className={activeTab === 'selfie' ? 'active' : ''}
              onClick={() => {
                toggleTab('selfie');
              }}
            >
              Selfie
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink
              className={activeTab === 'photo' ? 'active' : ''}
              onClick={() => {
                toggleTab('photo');
              }}
            >
              Photo
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink
              className={activeTab === 'schedule' ? 'active' : ''}
              onClick={() => {
                toggleTab('schedule');
              }}
            >
              Schedule
            </NavLink>
          </NavItem>
        </Nav>
        <TabContent activeTab={activeTab}>
          <TabPane tabId="diary">
            <Diary date={date} host={host} mine={mine} />
          </TabPane>
          <TabPane tabId="selfie">
            <Selfie date={date} host={host} mine={mine} />
          </TabPane>
          <TabPane tabId="photo">
            <Photo date={date} host={host} mine={mine} />
          </TabPane>
          <TabPane tabId="schedule">
            <Schedule date={date} host={host} mine={mine} />
          </TabPane>
        </TabContent>
      </ModalBody>
    </Modal>
  );
};

DayDialog.propTypes = {
  date: PropTypes.any,
  host: PropTypes.string.isRequired,
  mine: PropTypes.bool.isRequired,
  toggle: PropTypes.func,
  open: PropTypes.bool,
};

export default DayDialog;
