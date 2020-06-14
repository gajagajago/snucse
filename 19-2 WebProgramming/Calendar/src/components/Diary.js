import React from 'react';
import PropTypes from 'prop-types';

import RecordRTC from 'recordrtc';

import {Button} from 'reactstrap';

import firebaseHandler from '../modules/firebaseHandler';
import naverAPIHandler from '../modules/naverAPIHandler';

import './css/Diary.css';

const Diary = ({host, date, mine}) => {
  const [recorder, setRecorder] = React.useState();
  const [diaryData, setDiaryData] = React.useState();
  const [text, setText] = React.useState();
  const [status, setStatus] = React.useState('');
  const [show, setShow] = React.useState();

  const fetchDiaryData = async () => {
    const data = await firebaseHandler.getDiaryByDate(host, date);
    if (data) {
      setDiaryData(data);
      setText(data.content);
    } else {
      setDiaryData();
    }
  };
  const startRecord = () => {
    const success = async (stream) => {
      // eslint-disable-next-line
      const tempRecorder = RecordRTC(stream, {
        type: 'video',
      });
      setStatus('녹음중...');
      tempRecorder.startRecording();
      setRecorder(tempRecorder);
    };
    const error = () => {
      alert('마이크가 연결되어있지 않습니다');
    };
    navigator.getUserMedia({audio: true}, success, error);
  };
  const stopRecord = () => {
    recorder.stopRecording(async () => {
      const blob = recorder.getBlob();
      setStatus('음성 해석중...');
      const text = await naverAPIHandler.stt(blob);
      setText(text);
      setStatus('');
    });
  };
  const addDiary = async () => {
    await firebaseHandler.addDiary(host, date, text);
    await fetchDiaryData();
  };

  const deleteDiary = async () => {
    await firebaseHandler.deleteDiary(diaryData.id);
    await fetchDiaryData();
  };

  React.useEffect(() => {
    fetchDiaryData();
  }, []);

  React.useEffect(() => {
    if (mine) {
      if (diaryData) {
        setShow('myDiary');
      } else {
        setShow('addDiary');
      }
    } else {
      if (diaryData) {
        setShow('friendsDiary');
      } else {
        setShow('noData');
      }
    }
  }, [mine, diaryData]);
  return (
    <div className="d-flex w-100 h-100 flex-column px-3 py-3">
      {
        show === 'myDiary' && diaryData &&
        <div className="w-100 h-100 d-flex flex-column">
          <div id="diary" className="border px-2 py-2 mb-2">
            {diaryData.content}
          </div>
          <div className="d-flex justify-content-center">
            <Button color="red" onClick={deleteDiary}>다시 작성하기</Button>
          </div>
        </div>
      }
      {
        show === 'addDiary' &&
        <div className="d-flex w-100 h-100 flex-column">
          {
            status === '' &&
            <textarea type="text" className="w-100" id="diary" value={text}
              onChange={(e) => setText(e.target.value)}
              onKeyPress={(e) => {
                if (e.charCode === 13 && !e.shiftKey) {
                  e.preventDefault(); addDiary();
                }
              }}
            />
          }
          {
            status !== '' &&
            <div className="w-100 d-flex justify-content-center
              align-items-center h4" id="status">{status}</div>
          }
          <div className="d-flex justify-content-end mt-2">
            <Button color="blue" className="mr-1" onClick={startRecord}>
              Start Recording
            </Button>
            <Button color="blue" className="mr-1" onClick={stopRecord}>
              Stop Recording
            </Button>
            <Button color="brown" onClick={addDiary}>
              Save
            </Button>
          </div>
        </div>
      }
      {
        show === 'friendsDiary' && diaryData &&
        <div className="w-100 h-100 d-flex flex-column">
          <div className="2-100 h-100 border px-2 py-2 mb-2">
            {diaryData.content}
          </div>
        </div>
      }
      {
        show === 'noData' &&
        <div className="h-100 w-100 d-flex flex-column
            justify-content-center align-items-center">
          <span className="h4">등록된 Diary가 없습니다.</span>
        </div>
      }
    </div>
  );
};

Diary.propTypes = {
  date: PropTypes.any,
  host: PropTypes.string.isRequired,
  mine: PropTypes.bool.isRequired,
};

export default Diary;
