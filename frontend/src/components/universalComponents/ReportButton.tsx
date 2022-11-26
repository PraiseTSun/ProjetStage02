import {Link} from "react-router-dom";
import React from "react";

const ReportButton = () => {
    return (
        <div className="row d-flex justify-content-center">
            <Link to="/report" className="my-3 btn btn-warning col-4">Un problème?</Link>
        </div>
    )
}
export default ReportButton;