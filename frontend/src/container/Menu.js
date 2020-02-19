import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../store/ReduxActions";
import * as Service from "../service/service";
import {Dropdown} from "react-bootstrap";
import {useTranslation} from "react-i18next";

import ProfileModalForm from "../component/ProfileModalForm";
import {useHistory} from "react-router-dom";

class Menu extends Component {

    constructor(props) {
        super(props);
        this.state = {
            profileForm: {
                uiSchema: {active: false},
                formData: {tags: []},
                onSubmit: (Service.saveProfile),
                onSuccess: this.onProfileSuccessUpdate,
                closeForm: this.toggleProfileForm
            },
            ...props
        };
        Service.getUserProfile().then(props.actions.setUser);
    }

    onProfileSuccessUpdate = () => {

        const that = this;

        Service.getUserProfile().then(that.state.actions.setUser);

        Service.getTaskList()
            .then(runningList => {
                that.setState({runningList: runningList});
                that.state.actions.setRunningList(runningList);
            });

        this.toggleProfileForm();
    }

    toggleProfileForm = () => {
        const {profileForm} = this.state
        profileForm.uiSchema.active = !profileForm.uiSchema.active;
        this.setState({profileForm: profileForm})
    }

    render() {
        const {profileForm} = this.state;
        const {user, runningList} = this.props;

        profileForm.formData.tags = runningList.allTags;
        profileForm.formData.username = user.username;
        profileForm.formData.email = user.email;

        return (
            <Dropdown className={"metalheart-menu w-100"}>

                <Dropdown.Toggle split id="dropdown-split-basic">{user.username}</Dropdown.Toggle>

                <Dropdown.Menu>
                    <Profile onClick={this.toggleProfileForm}/>
                    <Signout/>
                </Dropdown.Menu>
                <ProfileModalForm schema={profileForm}/>
            </Dropdown>
        )
    }
}

function Signout() {
    const {t} = useTranslation();
    const history = useHistory();
    const signout = () => Service.signOut().then(() => history.push("/signin"))

    return (<Dropdown.Item onClick={signout}>{t("signout")}</Dropdown.Item>);
}

function Profile({onClick}) {
    const {t} = useTranslation();

    return (<Dropdown.Item onClick={onClick}>{t("Profile")}</Dropdown.Item>);
}

const mapStateToProps = state => ({
    user: state.task.user,
    runningList: state.task.runningList
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(Menu);