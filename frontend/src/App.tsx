import React, {useEffect, useState} from 'react';
import {Container} from 'react-bootstrap';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import ConfirmationPage from './pages/ConfirmationPage';
import LoginPage from './pages/LoginPage';
import IUser from './models/IUser';
import StudentDashboard from './pages/StudentDashboardPage';
import CompanyDashboard from './pages/CompanyDashboardPage';
import GestionnaireDashboard from './pages/GestionnaireDashboardPage';
import UserValidation from './pages/UserValidationPage';
import FormulaireSoumissionPage from './pages/FormulaireSoumissionPage';
import StudentCvValidationPage from './pages/StudentCvValidationPage';
import ValiderNouvelleOffreStagePage from "./pages/ValiderNouvelleOffreStagePage";
import OffersListPage from './pages/OffersListPage';
import UploaderMonCV from './pages/UploaderMonCV';

export const LOCAL_STORAGE_KEY = "MASSI_BEST_PROGRAMMER_PROJET_STAGE_02_CURRENT_CONNECTED_USER"
export const emptyUser: IUser = {
  id: "",
  firstName: "",
  lastName: "",
  userType: "",
  token: ""
}

function App() {
  const [user, setUser] = useState(emptyUser)
  const [verifiedLoginFromLocalStorage, setVerifiedLoginFromLocalStorage] = useState(false)
  const [count, setCount] = useState(0)

  useEffect(() => {
    const timer = setTimeout(() => setCount(count + 1), 10000)

    const verifyToken = async () => {
      if (user === emptyUser) {
        return
      }
      const getTokenHeaders = {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ "token": user.token })
      };
      const userRes = await fetch("http://localhost:8080/" + user.userType, getTokenHeaders)
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
  }, [count, user])

  const deconnexion = () => {
    setUser(emptyUser)
    localStorage.removeItem(LOCAL_STORAGE_KEY)
    window.location.href = "/"

  }

  const loginFromLocalStorage = async () => {
    if (localStorage.getItem(LOCAL_STORAGE_KEY) != null && user === emptyUser) {
      let user: IUser = JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY)!);
      const getTokenHeaders = {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ "token": user.token })
      };
      const userRes = await fetch("http://localhost:8080/" + user.userType, getTokenHeaders)
      if (!userRes.ok) {
        deconnexion()
      } else {
        setUser(JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY)!))
      }
    }

  }
  const checkIfUserExistsInLocalStorage = async () => {
    if (!verifiedLoginFromLocalStorage) {
      setVerifiedLoginFromLocalStorage(true)
      await loginFromLocalStorage()
    }
  }


  if (user === emptyUser) {
    checkIfUserExistsInLocalStorage()
    return (
      <Container className="vh-100">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<LoginPage setUser={setUser} />} />
            <Route path="/confirmEmail/:id" element={<ConfirmationPage />} />
            <Route path="*" element={<h1 className="text-center text-white display-1">404 - Page pas trouvé</h1>} />
          </Routes>
        </BrowserRouter>
      </Container>
    );
  }

  if (user.userType === "student") {
    return (
      <Container>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<StudentDashboard deconnexion={deconnexion} user={user} />} />
            <Route path="/uploaderCV" element={<UploaderMonCV user={user}/>}/>
            <Route path="/offres" element={<OffersListPage connectedUser={user} deconnexion={deconnexion} />} />
            <Route path="*" element={<h1 className="text-center text-white display-1 vh-100">404 - Page pas trouvé</h1>} />
          </Routes>
        </BrowserRouter>
      </Container>
    );
  }

  else if (user.userType === "company") {
    return (
      <Container className="vh-100">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<CompanyDashboard deconnexion={deconnexion} user={user} />} />
            <Route path="/soumettreOffre" element={<FormulaireSoumissionPage user={user} />} />
            <Route path="*" element={<h1 className="text-center text-white display-1">404 - Page pas trouvé</h1>} />
          </Routes>
        </BrowserRouter>
      </Container>
    );
  }

  else if (user.userType === "gestionnaire") {
    return (
      <Container>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<GestionnaireDashboard deconnexion={deconnexion} user={user} />} />
            <Route path="/userValidation" element={<UserValidation connectedUser={user} />} />
            <Route path="/cvValidation" element={<StudentCvValidationPage connectedUser={user} deconnexion={deconnexion} />} />
            <Route path="*" element={<h1 className="text-center text-white display-1">404 - Page pas trouvé</h1>} />
            <Route path="/validerNouvelleOffre" element={<ValiderNouvelleOffreStagePage connectedUser={user} deconnexion={deconnexion}/>} />
          </Routes>
        </BrowserRouter>
      </Container>
    );
  }

  return <h1 className="vh-100">Oops</h1>
}

export default App;