import React from 'react';
import PropTypes from 'prop-types';

import {Button, Input, InputGroup, InputGroupAddon} from 'reactstrap';
import Checkbox from '@material-ui/core/Checkbox';

import firebaseHandler from '../modules/firebaseHandler';

import './css/Schedule.css';

const Schedule = ({date, host, mine}) => {
  const [schedule, setSchedule] = React.useState('');
  const [scheduleList, setScheduleList] = React.useState([]);
  const [show, setShow] = React.useState();

  const fetchSchedule = async () => {
    const data = await firebaseHandler.getScheduleByDate(host, date);
    if (data) {
      setScheduleList(data);
    } else {
      setScheduleList([]);
    }
  };
  const addSchedule = async () => {
    if (schedule) {
      await firebaseHandler.addSchedule(host, date, schedule);
      await fetchSchedule();
      setSchedule('');
    }
  };
  const deleteSchedule = async (docId) => {
    await firebaseHandler.deleteSchedule(docId);
    await fetchSchedule();
  };
  const doneChange = async (docId, done) => {
    if (done === false) {
      await firebaseHandler.doneSchedule(docId);
    } else {
      await firebaseHandler.undoSchedule(docId);
    } await fetchSchedule();
  };
  const handleKeyEvent = (e) => {
    if (window.event.keyCode === 13) {
      if (e.target.value.trim() === '') {
        return;
      }
      addSchedule();
    }
  };
  React.useEffect(() => {
    fetchSchedule();
  }, []);
  React.useEffect(() => {
    if (mine) {
      setShow('mySchedule');
    } else {
      if (scheduleList.length !== 0) {
        setShow('friendsSchedule');
      } else {
        setShow('noData');
      }
    }
  }, [mine, scheduleList]);
  return (
    <div id="schedule-container" className="h-100 px-3 py-3">
      {
        show === 'mySchedule' &&
      <div>
        <div className="d-flex align-items-center mb-3">
          <InputGroup>
            <Input id ="inputText" placeholder="add your task"
              className="w-70" onKeyPress={handleKeyEvent}
              type="text" onChange={(e) => setSchedule(e.target.value)}
              value={schedule}>
            </Input>
            <InputGroupAddon addonType="append">
              <Button color="blueGrey" onClick={addSchedule}>추가</Button>
            </InputGroupAddon>
          </InputGroup>
        </div>
        {
          scheduleList.map((element) => {
            const check = (element.done === true) ? 'true' : 'false';
            return (
              <div
                className="w-100 d-flex align-items-center
                  justify-content-between"
                key={element.id}
              >
                <div className= "d-flex align-items-center" >
                  <Checkbox
                    onChange={() => doneChange(element.id, element.done)}
                    checked={element.done} color="primary"
                  />
                  <div className={check}>
                    {element.content}
                  </div>
                </div>
                <Button color="yellow" size="sm"
                  className="text-light"
                  onClick={() => deleteSchedule(element.id)}
                >
                  삭제
                </Button>
              </div>
            );
          }
          )}
      </div>
      }
      {
        show === 'friendsSchedule' &&
        scheduleList.map((element) => {
          const check = (element.done === true) ? 'true' : 'false';
          return (
            <div className = "w-100 d-flex align-items-center" key={element.id}>
              <Checkbox
                checked={element.done} color="primary"
                inputProps={{'aria-label': 'secondary checkbox'}}/>
              <div id = {check}>
                {element.content}
              </div>
            </div>
          );
        })
      }
      {
        show === 'noData' &&
        <div className="h-100 w-100 d-flex flex-column
          justify-content-center align-items-center">
          <span className="h4"> 등록된 Schedule이 없습니다. </span>
        </div>
      }
    </div>
  );
};

Schedule.propTypes = {
  date: PropTypes.any,
  host: PropTypes.string.isRequired,
  mine: PropTypes.bool.isRequired,
};

export default Schedule;
