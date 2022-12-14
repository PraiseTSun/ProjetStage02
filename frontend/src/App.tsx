import React, {useEffect, useState} from 'react';
import {Container} from 'react-bootstrap';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import ConfirmationPage from './pages/universalPages/ConfirmationPage';
import LoginPage from './pages/universalPages/LoginPage';
import IUser from './models/IUser';
import StudentDashboard from './pages/studentPages/StudentDashboardPage';
import CompanyDashboard from './pages/companyPages/CompanyDashboardPage';
import GestionnaireDashboard from './pages/gestionnairePages/GestionnaireDashboardPage';
import UserValidation from './pages/gestionnairePages/UserValidationPage';
import OffreSoumissionPage from './pages/companyPages/OffreSoumissionPage';
import StudentCvValidationPage from './pages/gestionnairePages/StudentCvValidationPage';
import ValiderNouvelleOffreStagePage from "./pages/gestionnairePages/ValiderNouvelleOffreStagePage";
import OffersListPage from './pages/studentPages/OffersListPage';
import StudentCvUploadPage from './pages/studentPages/StudentCvUploadPage';
import {putUserType} from "./services/universalServices/UniversalFetchService";
import OfferHistoryPage from "./pages/gestionnairePages/OfferHistoryPage";
import CompanyContractsPage from "./pages/companyPages/CompanyContractsPage";
import CompanyOffersPage from "./pages/companyPages/CompanyOffersPage";
import EvaluerLeMilieuDeStage from "./pages/gestionnairePages/EvaluerLeMilieuDeStage";
import StudentContractsPage from "./pages/studentPages/StudentContractsPage";
import ContractsPage from "./pages/gestionnairePages/ContractsPage";
import ConsulterEvaluationDeStages from "./pages/gestionnairePages/ConsulterEvaluationDeStages";
import ConsulterEvaluationDeEtudiants from "./pages/gestionnairePages/ConsulterEvaluationDeEtudiants";
import SignerEntenteDeStageParGestionnaire from "./pages/gestionnairePages/SignerEntenteDeStageParGestionnaire";
import ReportProblemPage from "./pages/universalPages/ReportProblemPage";
import NotFoundPage from "./pages/universalPages/NotFoundPage";
import ReportedProblemsPage from "./pages/gestionnairePages/ReportedProblemsPage";

export const LOCAL_STORAGE_KEY = "MASSI_BEST_PROGRAMMER_PROJET_STAGE_02_CURRENT_CONNECTED_USER"
export const emptyUser: IUser = {
    id: "",
    firstName: "",
    lastName: "",
    userType: "",
    cv: "",
    token: "",
    companyName: "",
    cvToValidate: ""
}

function App() {
    const [connectedUser, setConnectedUser] = useState(emptyUser)
    const [verifiedLoginFromLocalStorage, setVerifiedLoginFromLocalStorage] = useState(false)
    const [count, setCount] = useState(0)

    useEffect(() => {
        const timer = setTimeout(() => setCount(count + 1), 10000)

        const verifyToken = async () => {
            if (connectedUser === emptyUser) {
                return
            }
            const userRes = await putUserType(connectedUser.userType, connectedUser.token)
            if (!userRes.ok) {
                alert("Votre session est expir??.")
                deconnexion()
            }
        }

        const validateToken = async () => {
            await verifyToken()
        }
        validateToken()
        return () => clearTimeout(timer)
    }, [count, connectedUser])

    const deconnexion = () => {
        setConnectedUser(emptyUser)
        localStorage.removeItem(LOCAL_STORAGE_KEY)
    }

    const loginFromLocalStorage = async () => {
        if (localStorage.getItem(LOCAL_STORAGE_KEY) != null && connectedUser === emptyUser) {
            let user: IUser = JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY)!);
            const userRes = await putUserType(user.userType, user.token)
            if (!userRes.ok) {
                deconnexion()
            } else {
                setConnectedUser(JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY)!))
            }
        }

    }
    const checkIfUserExistsInLocalStorage = async () => {
        if (!verifiedLoginFromLocalStorage) {
            setVerifiedLoginFromLocalStorage(true)
            await loginFromLocalStorage()
        }
    }


    if (connectedUser === emptyUser) {
        checkIfUserExistsInLocalStorage()
        return (
            <Container className="min-vh-100">
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<LoginPage setUser={setConnectedUser}/>}/>
                        <Route path="/confirmEmail/:id" element={<ConfirmationPage/>}/>
                        <Route path="*" element={<NotFoundPage/>}/>
                        <Route path="/report"
                               element={<ReportProblemPage/>}/>
                    </Routes>
                </BrowserRouter>
            </Container>
        );
    }

    if (connectedUser.userType === "student") {
        return (
            <Container>
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<StudentDashboard deconnexion={deconnexion} user={connectedUser}/>}/>
                        <Route path="/offres" element={<OffersListPage connectedUser={connectedUser}/>}/>
                        <Route path="/uploaderCV" element={<StudentCvUploadPage connectedUser={connectedUser}/>}/>
                        <Route path="/myContracts" element={<StudentContractsPage connectedUser={connectedUser}/>}/>
                        <Route path="*" element={<NotFoundPage/>}/>
                        <Route path="/report"
                               element={<ReportProblemPage/>}/>
                    </Routes>
                </BrowserRouter>
            </Container>
        );
    } else if (connectedUser.userType === "company") {
        return (
            <Container>
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<CompanyDashboard deconnexion={deconnexion} user={connectedUser}/>}/>
                        <Route path="/soumettreOffre" element={<OffreSoumissionPage user={connectedUser}/>}/>
                        <Route path="/myOffers" element={<CompanyOffersPage connectedUser={connectedUser}/>}/>
                        <Route path="/CompanyContractsPage"
                               element={<CompanyContractsPage connectedUser={connectedUser}/>}/>
                        <Route path="*" element={<NotFoundPage/>}/>
                        <Route path="/report"
                               element={<ReportProblemPage/>}/>
                    </Routes>
                </BrowserRouter>
            </Container>
        );
    } else if (connectedUser.userType === "gestionnaire") {
        return (
            <Container>
                <BrowserRouter>
                    <Routes>
                        <Route path="/"
                               element={<GestionnaireDashboard deconnexion={deconnexion} user={connectedUser}/>}/>
                        <Route path="/userValidation" element={<UserValidation connectedUser={connectedUser}/>}/>
                        <Route path="/acceptationsValidation"
                               element={<ContractsPage connectedUser={connectedUser}/>}/>
                        <Route path="/cvValidation"
                               element={<StudentCvValidationPage connectedUser={connectedUser}/>}/>
                        <Route path="/offerHistory"
                               element={<OfferHistoryPage connectedUser={connectedUser}/>}/>
                        <Route path="/validerNouvelleOffre"
                               element={<ValiderNouvelleOffreStagePage connectedUser={connectedUser}/>}/>
                        <Route path="/evaluerLeMilieuDeStage"
                               element={<EvaluerLeMilieuDeStage user={connectedUser}/>}/>
                        <Route path="/consulterEvaluation"
                               element={<ConsulterEvaluationDeStages connectedUser={connectedUser}/>}></Route>
                        <Route path="/consulterEvaluationParEntreprise"
                               element={<ConsulterEvaluationDeEtudiants connectedUser={connectedUser}/>}></Route>
                        <Route path="/signerententedestage"
                               element={<SignerEntenteDeStageParGestionnaire connectedUser={connectedUser}/>}/>
                        <Route path="/reportedProblems"
                               element={<ReportedProblemsPage connectedUser={connectedUser}/>}/>
                        <Route path="*" element={<NotFoundPage/>}/>
                    </Routes>
                </BrowserRouter>
            </Container>
        );
    }

    return <h1 className="min-vh-100">Oops</h1>
}

export default App;
