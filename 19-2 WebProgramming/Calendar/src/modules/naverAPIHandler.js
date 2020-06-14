const stt = async (blob) => {
  const url = `https://cors-anywhere.herokuapp.com/https://naveropenapi.apigw.ntruss.com/recog/v1/stt?lang=Kor`;
  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/octet-stream',
      'X-NCP-APIGW-API-KEY-ID': process.env.REACT_APP_NAVER_CLIENT_ID2,
      'X-NCP-APIGW-API-KEY': process.env.REACT_APP_NAVER_CLIENT_SECRET2,
    },
    body: blob,
  });
  const data = await response.json();
  return data.text;
};

const analyzeFace = async (image) => {
  const formData = new FormData();
  formData.append('image', image);
  const url = 'https://cors-anywhere.herokuapp.com/https://openapi.naver.com/v1/vision/face';
  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'X-Naver-Client-Id': process.env.REACT_APP_NAVER_CLIENT_ID,
      'X-Naver-Client-Secret': process.env.REACT_APP_NAVER_CLIENT_SECRET,
    },
    body: formData,
  });
  const data = await response.json();
  let face;
  if (data.info.faceCount === 1) {
    face = data.faces[0];
  } else {
    face = null;
  }
  return face;
};

export default {
  stt,
  analyzeFace,
};
