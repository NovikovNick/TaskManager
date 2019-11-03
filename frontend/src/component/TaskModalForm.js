import React from 'react';

import Form from "react-jsonschema-form";

const schema = {
    title: "Create new task",
    type: "object",
    required: ["title"],
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


export default function TaskModalForm({id, formData, onSubmit}) {


    return (

        <div id={id}
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
                        <Form schema={schema}
                              uiSchema={uiSchema}
                              formData={formData}
                              onSubmit={onSubmit}
                              onError={log("errors")}/>

                    </div>
                </div>
            </div>
        </div>
    );
}