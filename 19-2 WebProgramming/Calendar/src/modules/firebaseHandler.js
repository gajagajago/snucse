import firebase from './firebase';

const db = firebase.firestore();

const checkUser = async (uid, email, name) => {
  const result = await db.collection('user')
      .where('uid', '==', uid)
      .get();
  if (!result.empty) {
    return;
  } else {
    await db.collection('user').add({
      uid,
      email,
      name,
    });
  }
};

const getUser = async (uid) => {
  const result = await db.collection('user')
      .where('uid', '==', uid)
      .get();
  if (!result.empty) {
    return result.docs.map((element) => element.data());
  } else {
    return null;
  }
};

const searchUserByEmail = async (email) => {
  const result = await db.collection('user')
      .where('email', '==', email)
      .get();
  if (!result.empty) {
    return result.docs.map((element) => {
      const data = element.data();
      data.id = element.id;
      return data;
    });
  } else {
    return null;
  }
};

const addFollow = async (follower, following) => {
  await db.collection('follow').add({
    follower,
    following,
  });
};

const getFollowing = async (uid) => {
  const result = await db.collection('follow')
      .where('follower', '==', uid)
      .get();
  if (!result.empty) {
    return result.docs.map((element) => {
      const data = element.data();
      data.id = element.id;
      return data;
    });
  }
  return null;
};

const getDiaryByDate = async (uid, date) => {
  const result = await db.collection('diary')
      .where('date', '==', date)
      .where('uid', '==', uid)
      .get();
  if (!result.empty) {
    return result.docs.map((element) => {
      const data = element.data();
      data.id = element.id;
      return data;
    })[0];
  }
  return null;
};

const getDiaryByMonth = async (uid, date) => {
  const start = new Date(`${date.getFullYear()}-${date.getMonth() + 1}-1`);
  const end = new Date(`${date.getFullYear()}-${date.getMonth() + 1}-31`);
  const result = await db.collection('diary')
      .where('date', '>=', start)
      .where('date', '<=', end)
      .where('uid', '==', uid)
      .get();
  if (!result.empty) {
    return result.docs.map((element) => {
      const data = element.data();
      data.id = element.id;
      return data;
    });
  }
  return [];
};

const addDiary = async (uid, date, content) => {
  await db.collection('diary').add({
    uid,
    date,
    content,
  });
};

const deleteDiary = async (docId) => {
  await db.collection('diary').doc(docId).delete();
};

const getScheduleByMonth = async (uid, date) => {
  const start = new Date(`${date.getFullYear()}-${date.getMonth() + 1}-1`);
  const end = new Date(`${date.getFullYear()}-${date.getMonth() + 1}-31`);
  const result = await db.collection('schedule')
      .where('date', '>=', start)
      .where('date', '<=', end)
      .where('uid', '==', uid)
      .get();
  if (!result.empty) {
    return result.docs.map((element) => {
      const data = element.data();
      data.id = element.id;
      return data;
    });
  }
  return [];
};

const getScheduleByDate = async (uid, date) => {
  const result = await db.collection('schedule')
      .where('date', '==', date)
      .where('uid', '==', uid)
      .get();
  if (!result.empty) {
    return result.docs.map((element) => {
      const data = element.data();
      data.id = element.id;
      return data;
    });
  }
  return null;
};

const addSchedule = async (uid, date, content) => {
  await db.collection('schedule').add({
    uid,
    date,
    content,
    done: false,
  });
};

const deleteSchedule = async (docId) => {
  await db.collection('schedule').doc(docId).delete();
};

const doneSchedule = async (docId) => {
  await db.collection('schedule').doc(docId).update({
    done: true,
  });
};

const undoSchedule = async (docId) => {
  await db.collection('schedule').doc(docId).update({
    done: false,
  });
};

const getSelfieByDate = async (uid, date) => {
  const result = await db.collection('selfie')
      .where('date', '==', date)
      .where('uid', '==', uid)
      .get();
  if (!result.empty) {
    return result.docs.map((element) => {
      const data = element.data();
      data.id = element.id;
      return data;
    })[0];
  }
  return null;
};

const getSelfieByMonth = async (uid, date) => {
  const start = new Date(`${date.getFullYear()}-${date.getMonth() + 1}-1`);
  const end = new Date(`${date.getFullYear()}-${date.getMonth() + 1}-31`);
  const result = await db.collection('selfie')
      .where('date', '>=', start)
      .where('date', '<=', end)
      .where('uid', '==', uid)
      .get();
  if (!result.empty) {
    return result.docs.map((element) => {
      const data = element.data();
      data.id = element.id;
      return data;
    });
  }
  return [];
};

const addSelfie = async (uid, date, file, face) => {
  const ref = firebase.storage().ref();
  const task = ref.child(`${uid}/selfie/${file.name}`)
      .put(file, {contentType: file.type});
  const snapshot = await task;
  const url = await snapshot.ref.getDownloadURL();
  await db.collection('selfie').add({
    uid,
    date,
    imageName: file.name,
    imageUrl: url,
    face,
  });
};

const deleteSelfie = async (uid, imageName, docId) => {
  const ref = firebase.storage().ref();
  const fileRef = ref.child(`${uid}/selfie/${imageName}`);
  fileRef.delete().then(async () => {
    await db.collection('selfie').doc(docId).delete();
  });
};

const getPhotosByDate = async (uid, date) => {
  const result = await db.collection('photo')
      .where('date', '==', date)
      .where('uid', '==', uid)
      .get();
  if (!result.empty) {
    return result.docs.map((element) => {
      const data = element.data();
      data.id = element.id;
      return data;
    });
  }
  return null;
};

const getPhotosByMonth = async (uid, date) => {
  const start = new Date(`${date.getFullYear()}-${date.getMonth() + 1}-1`);
  const end = new Date(`${date.getFullYear()}-${date.getMonth() + 1}-31`);
  const result = await db.collection('photo')
      .where('date', '>=', start)
      .where('date', '<=', end)
      .where('uid', '==', uid)
      .get();
  if (!result.empty) {
    return result.docs.map((element) => {
      const data = element.data();
      data.id = element.id;
      return data;
    });
  }
  return [];
};

const addPhoto = async (uid, date, file) => {
  const ref = firebase.storage().ref();
  const task = ref.child(`${uid}/photo/${file.name}`)
      .put(file, {contentType: file.type});
  const snapshot = await task;
  const url = await snapshot.ref.getDownloadURL();
  await db.collection('photo').add({
    uid,
    date,
    imageName: file.name,
    imageUrl: url,
  });
};

const deletePhoto = async (uid, imageName, docId) => {
  const ref = firebase.storage().ref();
  const fileRef = ref.child(`${uid}/photo/${imageName}`);
  try {
    await fileRef.delete();
  } catch (e) {
    console.log(e);
  }
  await db.collection('photo').doc(docId).delete();
};

export default {
  checkUser,
  getUser,
  searchUserByEmail,
  addFollow,
  getFollowing,

  getDiaryByDate,
  getDiaryByMonth,
  addDiary,
  deleteDiary,

  getScheduleByMonth,
  getScheduleByDate,
  addSchedule,
  deleteSchedule,
  doneSchedule,
  undoSchedule,

  getSelfieByDate,
  getSelfieByMonth,
  addSelfie,
  deleteSelfie,

  getPhotosByDate,
  getPhotosByMonth,
  addPhoto,
  deletePhoto,
};
