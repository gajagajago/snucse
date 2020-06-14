import React, {useState, useEffect} from 'react';
import {Link} from 'react-router-dom';
import {
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  Drawer,
  Divider,
  List,
  ListItem,
  ListItemText,
} from '@material-ui/core';
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  InputGroup,
  InputGroupAddon,
  Input,
  Card,
  CardText,
} from 'reactstrap';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import './css/MainLayout.css';
import firebaseHandler from '../modules/firebaseHandler';
import PropTypes from 'prop-types';

const Layout = ({user, title}) => {
  const [showDrawer, setShowDrawer] = useState(false);
  const [showAddFriendModal, setShowAddFriendModal] = useState(false);
  const [searchEmail, setSearchEmail] = useState('');
  const [searchedUser, setSearchedUser] = useState();
  const [followingList, setFollowingList] = useState([]);

  const fetchFollowingList = async () => {
    const data = await firebaseHandler.getFollowing(user.uid);
    if (data) {
      setFollowingList(data);
    } else {
      setFollowingList([]);
    }
  };
  const searchByEmail = async () => {
    const result = await firebaseHandler.searchUserByEmail(searchEmail);
    if (result) {
      setSearchedUser(result[0]);
    } else {
      setSearchedUser(-1);
    }
  };
  const addFollow = async (following) => {
    await firebaseHandler.addFollow(user.uid, following);
    await fetchFollowingList();
    setShowAddFriendModal(false);
    setShowDrawer(true);
  };
  const followExist = (uid) => {
    const find = followingList.find((element) => element.following.uid === uid);
    if (find) {
      return true;
    }
    return false;
  };
  useEffect(() => {
    fetchFollowingList();
  }, []);
  return (
    <div>
      <Modal isOpen={showAddFriendModal} toggle={() =>
        setShowAddFriendModal(!showAddFriendModal)}>
        <ModalHeader toggle={() => setShowAddFriendModal(!showAddFriendModal)}>
            팔로우</ModalHeader>
        <ModalBody>
          <div className="d-flex flex-column">
            <InputGroup>
              <Input type="text" value={searchEmail} onChange={(e) =>
                setSearchEmail(e.target.value)}
              placeholder="이메일로 검색"
              onKeyPress={(e) => {
                if (e.charCode === 13 ) {
                  searchByEmail();
                }
              }}
              />
              <InputGroupAddon addonType="append">
                <Button color="blueGrey" onClick={searchByEmail}>검색</Button>
              </InputGroupAddon>
            </InputGroup>
            {
              searchedUser && searchedUser !== -1 &&
              <Card body className="mt-2">
                <CardText>이름: {searchedUser.name}</CardText>
                <CardText>이메일: {searchedUser.email}</CardText>
                <Button color="blueGrey"
                  disabled={followExist(searchedUser.uid)}
                  onClick={() => addFollow(searchedUser)}>
                  {
                    followExist(searchedUser.uid) &&
                    '팔로우됨'
                  }
                  {
                    !followExist(searchedUser.uid) &&
                    '팔로우'
                  }
                </Button>
              </Card>
            }
            {
              searchedUser === -1 &&
              <Card body className="mt-2">
                <CardText>검색 결과가 없습니다.</CardText>
              </Card>
            }
          </div>
        </ModalBody>
      </Modal>
      <AppBar position="static" color="primary">
        <Toolbar variant="dense" color="primary">
          <IconButton
            edge="start"
            color="inherit"
            aria-label="menu"
            onClick={() => setShowDrawer(true)}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6">
            {title}
          </Typography>
        </Toolbar>
      </AppBar>
      <Drawer
        open={showDrawer}
        variant="persistent"
        anchor="left"
      >
        <div id="drawer-container">
          <div id="drawer-header" className="d-flex justify-content-end">
            <ListItem button component={Link}
              to="" onClick={() => setShowDrawer(false)}>
              <ListItemText primary={'홈'} />
            </ListItem>
            <IconButton onClick={() => setShowDrawer(false)}>
              <ChevronLeftIcon />
            </IconButton>
          </div>
          <Divider />
          <List>
            <ListItem button component={Link} to="/mycalendar"
              onClick={() => setShowDrawer(false)}
              selected={window.location.pathname === '/mycalendar'}>
              <ListItemText primary={'내 캘린더'} />
            </ListItem>
            <div className="d-flex flex-column w-100 mt-3">
              <div className="d-flex justify-content-between
                  align-items-center px-3">
                <div id="friend-list-title">팔로잉</div>
                <Button className="my-1" color="blueGrey"
                  size="sm" onClick={() => {
                    setSearchEmail('');
                    setSearchedUser();
                    setShowDrawer(false);
                    setShowAddFriendModal(true);
                  } }>팔로우 추가</Button>
              </div>
              {
                followingList.map((element) => {
                  return (
                    <ListItem
                      component={Link}
                      to={`/friendcalendar/${element.following.uid}`}
                      onClick={() => setShowDrawer(false)}
                      selected={
                        window.location.pathname.split('/')[1] ===
                        'friendcalendar' &&
                        window.location.pathname.split('/')[2] ===
                        element.following.uid
                      }
                      key={element.id} button className="pl-4"
                    >
                      <ListItemText
                        primary={`${element.following.email}
                        (${element.following.name})`}
                      />
                    </ListItem>
                  );
                })
              }
            </div>
          </List>
        </div>
      </Drawer>
    </div>
  );
};

Layout.propTypes = {
  user: PropTypes.shape({
    uid: PropTypes.string,
    email: PropTypes.string,
    displayName: PropTypes.string,
  }).isRequired,
  title: PropTypes.string.isRequired,
};

export default Layout;
