import React from "react";

export default function HeaderCell({value}) {
    return (
        <div className={"running-list-header-cell text-center"}>
            <div>{value}</div>
        </div>
    );
}
