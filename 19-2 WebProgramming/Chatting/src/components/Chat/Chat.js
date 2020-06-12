import React from 'react';
import { withRouter } from 'react-router-dom';

import './Chat.css';

import InfoBar from '../InfoBar/InfoBar';
import Input from '../Input/Input';
import Messages from '../Messages/Messages';

const API_ENDPOINT = 'https://snu-web-random-chat.herokuapp.com';
let INTERVAL_ID;

const Chat = ({ token, setToken }) => {
  const [name, setName] = React.useState('');
  const [message, setMessage] = React.useState('');
  const [messages, setMessages] = React.useState([]);
  const [lastEl, setLastEl] = React.useState({});
  const [loading, setLoading] = React.useState(false);

  React.useEffect(() => {
    if (localStorage.getItem('username')) {
      setName(localStorage.getItem('username'));
    }

    // window.addEventListener('scroll', listenToScroll, true);

    fetchData();

    startInterval();

    return () => {
      // window.removeEventListener('scroll', listenToScroll);
      clearInterval(INTERVAL_ID);
    };
  }, []);

  const fetchData = (action) => {
    fetch(`${API_ENDPOINT}/chats?order=desc&createdAtTo=${lastEl.createdAt || ''}`)
      .then((res) => res.json())
      .then((msgs) => {
        let updatedMsgs = [];
        if (action === 'more') {
          updatedMsgs = msgs.concat(messages);
          if (msgs.length === 1) setLastEl(null);
          else setLastEl(msgs[msgs.length - 1]);
        } else {
          updatedMsgs = messages.concat(msgs).reverse();
          setLastEl(updatedMsgs[0]);
        }

        setMessages(updatedMsgs);
        setLoading(false);
      })
      .catch((error) => {
        console.log(error);
        setLoading(false);
      });
  };

  const startInterval = (action) => {
    INTERVAL_ID = setInterval(() => {
      if (action === 'more') {
        fetch(`${API_ENDPOINT}/chats?createdAtFrom=${messages[0].createdAt || ''}`)
          .then((res) => res.json())
          .then((msgs) => {
            let updatedMsgs = [];
            updatedMsgs = messages.concat(msgs);

            setMessages(updatedMsgs);
          });
      } else {
        fetchData();
      }
    }, 3000);
  };

  const listenToScroll = (e) => {
    const element = document.querySelector('#loadButton');
    const position = element.getBoundingClientRect();

    // checking whether fully visible
    if (position.top >= 0 && position.bottom <= window.innerHeight) {
      // loadMessages();
    }
  };

  const onLogin = (e) => {
    e.preventDefault();

    fetch(`${API_ENDPOINT}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: `name=${name}`
    })
      .then((response) => response.json())
      .then(({ key }) => {
        if (key) {
          setToken(true);
          localStorage.setItem('__key', key);
          localStorage.setItem('username', name);
        }
      })
      .catch((err) => console.error(err));
  };

  const sendMessage = (e) => {
    e.preventDefault();

    // clearInterval(INTERVAL_ID);

    if (message) {
      fetch(`${API_ENDPOINT}/chats`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          Authorization: `Key ${localStorage.getItem('__key')}`
        },
        body: `message=${message}`
      })
        .then((response) => response.json())
        .then((res) => {
          const obj = {
            createdAt: res.createdAt,
            message: res.message,
            userName: res.user.name,
            _id: res.user._id
          };
          setMessages([ ...messages, obj ]);
        })
        .catch((err) => {
          console.log(err);
        });

      setMessage('');
    }
  };

  const logout = () => {
    localStorage.removeItem('__key');
    localStorage.removeItem('username');
    setToken(false);
  };

  const loadMessages = () => {
    setLoading(true);
    fetchData('more');
    clearInterval(INTERVAL_ID);
    // setTimeout(() => {
    // 	startInterval('more');
    // }, 3000);
  };

  return (
		<div className="outerContainer">
			<div className="innerContainer">
				<InfoBar
					name={name}
					token={token}
					handleInput={(e) => setName(e.target.value)}
					handleLogin={onLogin}
					handleLogout={logout}
				/>
				<Messages messages={messages} name={name} lastEl={lastEl} loadMore={loadMessages} loading={loading} />
				{token && <Input message={message} setMessage={setMessage} sendMessage={sendMessage} />}
			</div>
		</div>
  );
};

export default withRouter(Chat);
