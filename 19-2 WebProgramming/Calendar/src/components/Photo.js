import React from 'react';
import PropTypes from 'prop-types';

import {Button, Input, Spinner} from 'reactstrap';

import firebaseHandler from '../modules/firebaseHandler';

import './css/Photo.css';
const Photo = ({date, host, mine}) => {
  const [photoDatas, setPhotoDatas] = React.useState([]);
  const [uploading, setUploading] = React.useState(false);
  const [show, setShow] = React.useState();

  const fetchPhotoDatas = async () => {
    const data = await firebaseHandler.getPhotosByDate(host, date);
    if (data) {
      setPhotoDatas(data);
    } else {
      setPhotoDatas([]);
    }
  };
  const deletePhoto = async (name, docId) => {
    await firebaseHandler.deletePhoto(host, name, docId);
    await fetchPhotoDatas();
  };
  const handleChange = async (e) => {
    setUploading(true);
    for (const file of e.target.files) {
      await firebaseHandler.addPhoto(host, date, file);
    }
    await fetchPhotoDatas();
    setUploading(false);
  };

  React.useEffect(() => {
    if (mine) {
      setShow('myPhoto');
    } else {
      if (photoDatas.length !== 0) {
        setShow('friendsPhoto');
      } else {
        setShow('noData');
      }
    }
  }, [mine, photoDatas]);

  React.useEffect(() => {
    fetchPhotoDatas();
  }, []);
  return (
    <div className="h-100 d-flex px-3 py-3">
      {
        show === 'myPhoto' &&
        <div id="photo-area" className="h-100 w-100 d-flex
        flex-wrap align-content-start">
          <Input id="photo-input" type="file" disabled={uploading}
            multiple onChange={handleChange} />
          {
            photoDatas.map((photoData) => {
              const imageStyle= {
                height: '150px',
                width: '150px',
                backgroundImage: `url(${photoData.imageUrl})`,
                backgroundRepeat: 'no-repeat',
                backgroundSize: 'contain',
                backgroundPosition: 'center',
              };
              return (
                <div style = { imageStyle } key={photoData.id}
                  className="mx-1 my-1">
                  <div className="hover d-flex justify-content-center
                    align-items-center">
                    <Button color="red" onClick={() =>
                      deletePhoto(photoData.imageName, photoData.id)}>
                      삭제</Button>
                  </div>
                </div>
              );
            })
          }
          <label htmlFor="photo-input">
            <div id="add-box" className="border d-flex justify-content-center
              align-items-center display-4 mx-1 my-1 h4 text-blueGrey">
              {
                uploading ?
                <Spinner color="blueGrey" /> :
                '+'
              }
            </div>
          </label>
        </div>
      }
      {
        show === 'friendsPhoto' &&
        <div id="photo-area" className="h-100 w-100
          d-flex flex-wrap align-content-start">
          {
            photoDatas.map((photoData) => {
              const imageStyle= {
                height: '150px',
                width: '150px',
                backgroundImage: `url(${photoData.imageUrl})`,
                backgroundRepeat: 'no-repeat',
                backgroundSize: 'contain',
                backgroundPosition: 'center',
              };
              return (
                <div style = { imageStyle } key={photoData.id}
                  className="mx-1 my-1">
                </div>
              );
            })
          }
        </div>
      }
      {
        show === 'noData' &&
        <div id="photo-area" className="h-100 w-100 d-flex
          justify-content-center align-items-center h4">
          등록된 Photo가 없습니다.
        </div>
      }

    </div>
  );
};

Photo.propTypes = {
  date: PropTypes.any,
  host: PropTypes.string.isRequired,
  mine: PropTypes.bool.isRequired,
};

export default Photo;
