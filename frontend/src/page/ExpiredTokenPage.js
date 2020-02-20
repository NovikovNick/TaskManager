import React from 'react';
import {Container} from "react-bootstrap";
import ExpiredTokenPanel from "../component/panel/ExpiredTokenPanel";


export default function ExpiredTokenPage() {
    return (
        <Container className={"h-100 d-flex"}>
            <ExpiredTokenPanel/>
        </Container>
    );
}
