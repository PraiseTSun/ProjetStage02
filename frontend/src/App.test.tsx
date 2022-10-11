import IUser from "./models/IUser";
import {BrowserRouter} from "react-router-dom";
import CompanyDashboardPage from "./pages/CompanyDashboardPage";
import ConfirmationPage from "./pages/ConfirmationPage";
import FormulaireSoumissionPage from "./pages/FormulaireSoumissionPage";
import GestionnaireDashboardPage from "./pages/GestionnaireDashboardPage";
import StudentDashboardPage from "./pages/StudentDashboardPage";
import LoginPage from "./pages/LoginPage";
import StudentCvValidationPage from "./pages/StudentCvValidationPage";
import UserValidation from "./pages/UserValidationPage";

const MockCompanyDashboardPage = () => {
    return (
        <BrowserRouter>
            <CompanyDashboardPage  deconnexion={()=>null} user={company}/>
        </BrowserRouter>
    );
}
const MockConfirmationPage = () => {
    return (
        <BrowserRouter>
            <ConfirmationPage />
        </BrowserRouter>
    );
}
const MockFormulaireSoumissionPage = () => {
    return (
        <BrowserRouter>
            <FormulaireSoumissionPage user={gestionnaire}  />
        </BrowserRouter>
    );
}
const MockGestionnaireDashboardPage = () => {
    return (
        <BrowserRouter>
            <GestionnaireDashboardPage user={gestionnaire} deconnexion={()=>null}/>
        </BrowserRouter>
    );
}
const MockStudentDashboardPage = () => {
    return (
        <BrowserRouter>
            <StudentDashboardPage user={student} deconnexion={()=>null}/>
        </BrowserRouter>
    );
}
const MockLoginPage = () => {
    return (
        <BrowserRouter>
            <LoginPage setUser={()=>null}/>
        </BrowserRouter>
    );
}
const MockStudentCvValidationPage = () => {
    return (
        <BrowserRouter>
            <StudentCvValidationPage connectedUser={gestionnaire} deconnexion={()=>null}/>
        </BrowserRouter>
    );
}
const MockUserValidationPage = () => {
    return (
        <BrowserRouter>
            <UserValidation connectedUser={gestionnaire} />
        </BrowserRouter>
    );
}
const gestionnaire: IUser = {
    firstName: "Yan",
    lastName: "Zhou",
    userType: "gestionnaire",
    token: "32432"
}

const company: IUser = {
    firstName: "Bob",
    lastName: "Marley",
    userType: "company",
    token: "324324332"
}
const student: IUser = {
    firstName: "Bartholomew",
    lastName: "Kuma",
    userType: "student",
    token: "324324332"
}

const offres : object = [{
    nomDeCompagnie: "Desjardins",
    department: "Techniques de linformatique",
    position: "position",
    heureParSemaine: 40,
    adresse: "addresse",
    pdf: new FileReader(),
    token: gestionnaire.token
},
    {
        nomDeCompagnie: "Pirate",
        department: "Techniques de linformatique",
        position: "position1",
        heureParSemaine: 40,
        adresse: "addresse1",
        pdf: new FileReader(),
        token: gestionnaire.token
    }]
