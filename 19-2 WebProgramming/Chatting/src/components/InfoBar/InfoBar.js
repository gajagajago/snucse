import React from 'react';

import onlineIcon from '../../icons/onlineIcon.png';
import logoutIcon from '../../icons/logoutIcon.png';

import './InfoBar.css';

export default ({ token, name, handleInput, handleLogin, handleLogout }) => (
	<div className="infoBar p-1">
		<div className="leftInnerContainer">
			<img className="onlineIcon" src={onlineIcon} alt="online" />
			<h3>Chat</h3>
		</div>
		<div className="rightInnerContainer">
			{!token ? (
				<form onSubmit={handleLogin}>
					<div className="input-group p-1">
						<input
							type="text"
							value={name}
							className="form-control"
							placeholder="Type your name"
							aria-label="Type your name"
							aria-describedby="button-addon2"
							onChange={handleInput}
						/>
						<div className="input-group-append">
							<button className="btn btn-outline-light" type="submit" id="button-addon2">
								Login
							</button>
						</div>
					</div>
				</form>
			) : (
				<span className="logout" onClick={handleLogout} title="Logout">
					<img className="closeIcon" width={25} height={25} src={logoutIcon} alt="logout" />
				</span>
			)}
		</div>
	</div>
);
