import React from 'react';
import PropTypes from 'prop-types';

import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import '@fullcalendar/core/main.css';
import '@fullcalendar/daygrid/main.css';

import DayDialog from './DayDialog';

import firebaseHandler from '../modules/firebaseHandler';
const formatDate = (date) => {
  const d = new Date(date);
  let month = '' + (d.getMonth() + 1);
  let day = '' + d.getDate();
  const year = d.getFullYear();
  if (month.length < 2) {
    month = '0' + month;
  }
  if (day.length < 2) {
    day = '0' + day;
  }
  return [year, month, day].join('-');
};

const Calendar = ({host, mine}) => {
  const [showDayDialog, setShowDayDialog] = React.useState(false);
  const [showingDate, setShowingDate] = React.useState(new Date(Date.now()));
  const [clickedDate, setClickedDate] = React.useState();
  const [diaryLi, setDiaryLi] = React.useState([]);
  const [selfieLi, setSelfieLi] = React.useState([]);
  const [photoLi, setPhotoLi] = React.useState([]);
  const [scheLi, setScheLi] = React.useState([]);
  const calendar = React.useRef();

  const fetchEventList = async (date) => {
    const diaryList = await firebaseHandler.getDiaryByMonth(host, date);
    const selfieList = await firebaseHandler.getSelfieByMonth(host, date);
    const photoList = await firebaseHandler.getPhotosByMonth(host, date);
    const scheList = await firebaseHandler.getScheduleByMonth(host, date);
    setDiaryLi(diaryList);
    setSelfieLi(selfieList);
    setPhotoLi(photoList);
    setScheLi(scheList);
  };
  const clickDate = (arg) => {
    const date = new Date(arg.date);
    setClickedDate(date);
    setShowDayDialog(true);
  };
  const addMarkToDate = (date, list) => {
    const color = {
      diary: 'red',
      selfie: 'orange',
      photo: 'cyan',
      schedule: 'blue',
    };
    const dayElement = document.querySelectorAll(`[data-date='${date}']`)[0];
    const div = document.createElement('div');
    div.style.width = '100%';
    div.style.height = '100%';
    div.style.display = 'flex';
    div.style.flexDirection = 'column';
    div.style.justifyContent = 'flex-end';
    div.style.alignItems = 'flex-start';
    div.style.pointerEvents = 'none';
    div.style.padding = '10px';
    list.forEach((li) => {
      const dot = document.createElement('div');
      dot.style.width = '6px';
      dot.style.height = '6px';
      dot.style.borderRadius ='50%';
      dot.style.backgroundColor = color[li];
      dot.style.marginBottom = '5px';
      div.append(dot);
    });
    dayElement.append(div);
  };
  const addMarkToCalendar = async () => {
    const dataPerCategory = {
      diary: diaryLi,
      selfie: selfieLi,
      photo: photoLi,
      schedule: scheLi,
    };
    const dataPerDate = {};

    for (const key in dataPerCategory) {
      if (dataPerCategory.hasOwnProperty(key)) {
        dataPerCategory[key].forEach((element) => {
          const date = new Date(element.date.toDate());
          const dateString = formatDate(date);
          if (dataPerDate[dateString]) {
            if (!dataPerDate[dateString].includes(key)) {
              dataPerDate[dateString].push(key);
            }
          } else {
            dataPerDate[dateString] = [key];
          }
        });
      }
    }
    for (const key in dataPerDate) {
      if (dataPerDate.hasOwnProperty(key)) {
        const element = document.querySelectorAll(`[data-date='${key}']`)[0];
        if (element) {
          element.innerHTML = '';
          addMarkToDate(key, dataPerDate[key]);
        }
      }
    }
  };
  React.useEffect(() => {
    addMarkToCalendar();
    const prevButton = document.getElementsByClassName('fc-prev-button')[0];
    prevButton.addEventListener('click', () => {
      setShowingDate(calendar.current.calendar.view.currentStart);
    });
    const nextButton = document.getElementsByClassName('fc-next-button')[0];
    nextButton.addEventListener('click', () => {
      setShowingDate(calendar.current.calendar.view.currentStart);
    });
  });
  React.useEffect(() => {
    setDiaryLi([]);
    setSelfieLi([]);
    setPhotoLi([]);
    setScheLi([]);
  }, [host, showingDate]);
  React.useEffect(() => {
    if (showDayDialog === false) {
      fetchEventList(showingDate);
    }
  }, [host, showingDate, showDayDialog]);
  return (
    <div>
      <DayDialog
        toggle={() => setShowDayDialog(!showDayDialog)}
        open={showDayDialog}
        date={clickedDate}
        host={host}
        mine={mine}
      />
      <FullCalendar
        ref={calendar}
        defaultView="dayGridMonth"
        plugins={[dayGridPlugin, interactionPlugin]}
        aspectRatio="1.3"
        dateClick={clickDate}
      />
    </div>
  );
};

Calendar.propTypes = {
  host: PropTypes.string.isRequired,
  mine: PropTypes.bool.isRequired,
};

export default Calendar;
