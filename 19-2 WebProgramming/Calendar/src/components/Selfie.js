import React from 'react';
import PropTypes from 'prop-types';

import {Spinner, Button, Input} from 'reactstrap';

import firebaseHandler from '../modules/firebaseHandler';
import naverAPIHandler from '../modules/naverAPIHandler';

import './css/Selfie.css';

const Selfie = ({date, host, mine}) => {
  const [selfieData, setSelfieData] = React.useState();
  const [uploading, setUploading] = React.useState(false);
  const [show, setShow] = React.useState();
  const fetchSelfieData = async () => {
    const data = await firebaseHandler.getSelfieByDate(host, date);
    if (data) {
      setSelfieData(data);
    }
  };
  const handleChange = async (e) => {
    const file = e.target.files[0];
    if (file) {
      setUploading(true);
      const face = await naverAPIHandler.analyzeFace(file);
      await firebaseHandler.addSelfie(host, date, file, face);
      await fetchSelfieData();
      setUploading(false);
    }
  };
  const deleteSelfie = async () => {
    await firebaseHandler.deleteSelfie(
        host, selfieData.imageName, selfieData.id);
    setSelfieData();
  };
  React.useEffect(() => {
    fetchSelfieData();
  }, []);
  React.useEffect(() => {
    if (mine) {
      if (selfieData) {
        setShow('mySelfie');
      } else {
        setShow('addSelfie');
      }
    } else {
      if (selfieData) {
        setShow('friendsSelfie');
      } else {
        setShow('noData');
      }
    }
  }, [mine, selfieData]);
  return (
    <div className="h-100 d-flex px-3 py-3">
      <Input id="selfie-input" disabled={uploading}
        type="file" onChange={handleChange}
      />
      {
        show === 'mySelfie' &&
        <div className="h-100 w-100 d-flex flex-column
          justify-content-center align-items-center">
          <img
            id="selfie-image"
            src={selfieData ? selfieData.imageUrl : ''}
            alt="selfie"
          />
          <span className="h4 my-2">
            {
              selfieData && selfieData.face ?
              selfieData.face.emotion.value :
              '얼굴 인식 불가'
            }
          </span>
          <Button color="red" onClick={deleteSelfie}>삭제</Button>
        </div>
      }
      {
        show === 'addSelfie' &&
        <div className="h-100 w-100 d-flex justify-content-center
          align-items-center">
          <label htmlFor="selfie-input">
            <div id="selfie-add-box"
              className="border d-flex justify-content-center
                align-items-center display-4 mx-1 my-1 h4 text-blueGrey"
            >
              {
                uploading ?
                <Spinner color="blueGrey" /> :
                '+'
              }
            </div>
            {/* <div
              id="selfie-add-button"
              className="bg-blueGrey text-light rounded px-3 py-1"
            >
              {
                uploading ?
                <Spinner size="sm" color="light" /> :
                '추가하기'
              }
            </div> */}
          </label>
        </div>
      }
      {
        show === 'friendsSelfie' &&
        <div className="h-100 w-100 d-flex flex-column
          justify-content-center align-items-center">
          <img id="selfie-image"
            src={selfieData ? selfieData.imageUrl : ''}
            alt="selfie"
          />
          <span className="h4 mb-3">
            {
              selfieData && selfieData.face ?
              selfieData.face.emotion.value :
              '얼굴 인식 불가'
            }
          </span>
        </div>
      }
      {
        show === 'noData' &&
        <div className="h-100 w-100 d-flex flex-column
          justify-content-center align-items-center">
          <span className="h4">등록된 Selfie가 없습니다.</span>
        </div>
      }
    </div>
  );
};

Selfie.propTypes = {
  date: PropTypes.any,
  host: PropTypes.string.isRequired,
  mine: PropTypes.bool.isRequired,
};

export default Selfie;
