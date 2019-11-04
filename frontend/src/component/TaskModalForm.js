import React from 'react';

import Form from "react-jsonschema-form";
import $ from "jquery";

const schema = {
    title: "Create new task",
    type: "object",
    properties: {
        title: {type: "string", title: "Title"},
        description: {type: "string", title: "Description"},
    }
};
const uiSchema = {
    "title": {
        "ui:widget": "text",
        "ui:title": "Task title",
    },
    "description": {
        "ui:widget": "textarea"
    }
}
const log = (type) => console.log.bind(console, type);

export default class TaskModalForm extends React.Component {
    constructor(props) {
        super(props);
        this.form = React.createRef();
        this.modal = React.createRef();
    }

    show = () => {
        $(this.modal.current).modal('show')
        this.form.current.setState({errorSchema: {}});
    }

    hide = () => {
        $(this.modal.current).modal('hide')
        this.form.current.setState({errorSchema: {}});
    }

    error = (errorSchema) => {
        this.form.current.setState({errorSchema: errorSchema});
    }

    render() {

        const {id, formData, onSubmit} = this.props;

        return (

            <div ref={this.modal}
                 id={id}
                 className="modal fade"
                 tabIndex="-1"
                 role="dialog"
                 aria-labelledby="myModalLabel"
                 aria-hidden="true">

                <div className="modal-dialog"
                     style={{maxWidth: "600px"}}
                     role="document">

                    <div className="modal-content">

                        <div className="modal-body">
                            <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                            <Form
                                ref={this.form}
                                schema={schema}
                                uiSchema={uiSchema}
                                formData={formData}
                                onSubmit={onSubmit}
                                onError={log("errors")}
                            />
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}