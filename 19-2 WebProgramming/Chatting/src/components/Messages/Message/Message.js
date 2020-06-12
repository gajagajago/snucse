import React from 'react';
import dayjs from 'dayjs';

import './Message.css';

import ReactEmoji from 'react-emoji';

const Message = ({ message: { _id, message, userName, createdAt }, name }) => {
	const user = userName;
	let isSentByCurrentUser = false;

	const trimmedName = name.trim().toLowerCase();

	if (user === trimmedName) {
		isSentByCurrentUser = true;
	}

	return isSentByCurrentUser ? (
		<div className="messageContainer justifyEnd">
			<p className="sentText pr-10">{trimmedName}</p>
			<div
				className="messageBox backgroundBlue"
				data-toggle="tooltip"
				data-placement="top"
				title={dayjs(createdAt).format('YYYY-MM-DD')}
			>
				<p className="messageText colorWhite" style={{ whiteSpace: 'pre-wrap' }}>
					{ReactEmoji.emojify(message)}
				</p>
			</div>
		</div>
	) : (
		<div className="messageContainer justifyStart">
			<div
				className="messageBox backgroundLight"
				data-toggle="tooltip"
				data-placement="top"
				title={dayjs(createdAt).format('YYYY-MM-DD')}
			>
				<p className="messageText colorDark" style={{ whiteSpace: 'pre-wrap' }}>
					{ReactEmoji.emojify(message)}
				</p>
			</div>
			<p className="sentText pl-10 ">{userName.substring(0, 50)}</p>
		</div>
	);
};

export default Message;
