import React from "react";
import IUser from '../models/IUser';
import { Row, ToggleButton, ToggleButtonGroup } from 'react-bootstrap';
import DropdownMenu from "react-bootstrap/esm/DropdownMenu";
import CompanyDashboard from '../components/CompanyDashboard';
import StudentDashboard from "../components/StudentDashboard";
import GestionnaireDashboard from '../components/GestionnaireDashboard';

const Dashboard = ({user}:{user:IUser}) => {

    return (<>
        <h1 className="p-5 text-center ">Bienvenue {user.firstName} {user.lastName}</h1>
        {
            user.userType == "company"? <CompanyDashboard />:
            user.userType == "gestionnaire"? <GestionnaireDashboard />:
            <StudentDashboard />
        }
    </>)
}
export default Dashboard