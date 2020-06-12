import React from 'react';
import ScrollToBottom from 'react-scroll-to-bottom';
import Message from './Message/Message';

import './Messages.css';

export default ({ messages, name, lastEl, loadMore, loading }) => {
	return (
		<ScrollToBottom className="messages">
			{lastEl && (
				<div className="d-flex justify-content-center p-4" id="loadButton">
					<button className="btn btn-light border" disabled={loading} onClick={loadMore}>
						Load more
					</button>
				</div>
			)}
			{messages.map((msg, index) => (
				<div key={index} id="messagesContainer">
					<Message message={msg} name={name} />
				</div>
			))}
		</ScrollToBottom>
	);
};
