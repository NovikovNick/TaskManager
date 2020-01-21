import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Store from "../store/ReduxActions";

import RunningList from "../container/RunningList";

import * as REST from "../rest/rest";
import {Container, Row, Col, Nav} from "react-bootstrap";

class Page extends Component {
    render() {

        const signout = function () {
            REST.signOut().then(() => window.location = "/signin");
        }

        return (
            <Container fluid>

                <Row>
                    <Col md={{span:2}}>
                        <Nav defaultActiveKey="/home" className="flex-column">

                            <Row>
                                <Col md={{span:4}}>
                                    <img src="https://avatarfiles.alphacoders.com/177/177127.jpg" style={{
                                        width: "40px",
                                        borderRadius: "100px"
                                    }}/>
                                </Col>
                                <Col md={{span:8}} className={"align-middle"}>Name</Col>
                            </Row>

                            <Nav.Item><Nav.Link onClick={REST.getUserProfile}>Load User Profile</Nav.Link></Nav.Item>
                            <Nav.Item><Nav.Link  onClick={signout}>Logout</Nav.Link></Nav.Item>
                            <Nav.Item><Nav.Link  onClick={() => alert("2")}>2</Nav.Link></Nav.Item>
                            <Nav.Item><Nav.Link  onClick={() => alert("3")}>3</Nav.Link></Nav.Item>
                        </Nav>
                    </Col>

                    <Col md={{span:10}}>
                        <div className="metalheart-wrapper">
                            <RunningList/>
                        </div>
                    </Col>
                </Row>
            </Container>

        );
    }
}

const mapStateToProps = state => ({
    taskList: state.task.taskList
});

const mapDispatchToProps = (dispatch) => ({
    actions: bindActionCreators(Store, dispatch)
})

export default connect(mapStateToProps, mapDispatchToProps)(Page);