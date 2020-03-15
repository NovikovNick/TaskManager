import React from 'react';
import {connect} from 'react-redux';
import {Dropdown} from "react-bootstrap";
import PropTypes from "prop-types";
import "./styles.scss";
import Signout from "../../component/menu/Signout";
import Profile from "../../component/menu/Profile";

function Menu({user}) {

    return (
        <Dropdown className={"metalheart-menu w-100"}>

            <Dropdown.Toggle split id="dropdown-split-basic">{user.username}</Dropdown.Toggle>

            <Dropdown.Menu>
                <Profile />
                <Signout />
            </Dropdown.Menu>

        </Dropdown>
    )
}

Menu.propTypes = {
    user: PropTypes.shape({
        id: PropTypes.number,
        username: PropTypes.string,
    })
};

const mapStateToProps = state => ({
    user: state.task.user
});

export default connect(mapStateToProps)(Menu);