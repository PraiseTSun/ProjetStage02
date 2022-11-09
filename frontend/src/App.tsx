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
import SignerEntenteDeStageParCompagnie from "./pages/companyPages/SignerEntenteDeStageParCompagnie";
import CompanyOffersPage from "./pages/companyPages/CompanyOffersPage";
import StudentContractsPage from "./pages/studentPages/StudentContractsPage";
import ContractsPage from "./pages/gestionnairePages/ContractsPage";
import SignerEntenteDeStage from "./pages/gestionnairePages/SignerEntenteDeStage";

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
                alert("Votre session est expiré.")
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
        window.location.href = "/"
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
                        <Route path="*"
                               element={<h1 className="min-vh-100 text-center text-white display-1">404 - Page pas
                                   trouvé</h1>}/>
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
                        <Route path="*"
                               element={<h1 className="min-vh-100 text-center text-white display-1 min-vh-100">404 -
                                   Page pas trouvé</h1>}/>
                    </Routes>
                </BrowserRouter>
            </Container>
        );
    } else if (connectedUser.userType === "company") {
        return (
            <Container className="min-vh-100">
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<CompanyDashboard deconnexion={deconnexion} user={connectedUser}/>}/>
                        <Route path="/soumettreOffre" element={<OffreSoumissionPage user={connectedUser}/>}/>
                        <Route path="/myOffers" element={<CompanyOffersPage connectedUser={connectedUser}/>}/>
                        <Route path="*"
                               element={<h1 className="min-vh-100 text-center text-white display-1">404 - Page pas
                                   trouvé</h1>}/>
                        <Route path="/SignerEntenteDeStageParCompagnie" element={<SignerEntenteDeStageParCompagnie user={connectedUser}/>}/>
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
                        <Route path="*"
                               element={<h1 className="min-vh-100 text-center text-white display-1">404 - Page pas
                                   trouvé</h1>}/>
                        <Route path="/validerNouvelleOffre"
                               element={<ValiderNouvelleOffreStagePage connectedUser={connectedUser}/>}/>
                        <Route path="/signerententedestage"
                               element={<SignerEntenteDeStage connectedUser={connectedUser}/>}/>
                    </Routes>
                </BrowserRouter>
            </Container>
        );
    }

    return <h1 className="min-vh-100">Oops</h1>
}

export default App;