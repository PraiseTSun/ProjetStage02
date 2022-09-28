import React, {useEffect} from 'react';
import { useState } from 'react';
import { Container } from 'react-bootstrap';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import ConfirmationPage from './pages/ConfirmationPage';
import LoginPage from './pages/LoginPage';
import IUser from './models/IUser';
import StudentDashboard from './pages/StudentDashboardPage';
import CompanyDashboard from './pages/CompanyDashboardPage';
import GestionnaireDashboard from './pages/GestionnaireDashboardPage';
import UserValidation from './pages/UserValidationPage';
import FormulaireSoumissionPage from './pages/FormulaireSoumissionPage';

export const LOCAL_STORAGE_KEY = "MASSI_BEST_PROGRAMMER_PROJET_STAGE_02_CURRENT_CONNECTED_USER"
const emptyUser: IUser = {
  firstName: "",
  lastName: "",
  userType: "",
  token:""
}

function App() {
  const [user, setUser] = useState(emptyUser)
  const [lastVerifiedTimestamp, setLastVerifiedTimeStamp] = useState(Date.now())
  const [currentlyVerifyingLogin, setCurrentlyVerifyingLogin] = useState(false)
  const [currentlyVerifyingToken, setCurrentlyVerifyingToken] = useState(false)
  const [isValidToken,setValidToken] = useState(true)

  const deconnexion = () => {
    setUser(emptyUser)
    localStorage.removeItem(LOCAL_STORAGE_KEY)
  }

  const verifyToken = async () => {
    if(user == emptyUser){
      return
    }
    const getTokenHeaders = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ "token": user.token})
    };
    const userRes = await fetch("http://localhost:8080/" + user.userType,getTokenHeaders)
    console.log("test")
    if(!userRes.ok){
      alert("Votre session est expiré.")
      setValidToken(false)
      deconnexion()
    }
  }

  const login = async() => {
    if (localStorage.getItem(LOCAL_STORAGE_KEY) != null && user == emptyUser) {
      let user:IUser = JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY)!);
      const getTokenHeaders = {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ "token": user.token})
      };
      const userRes = await fetch("http://localhost:8080/" + user.userType,getTokenHeaders)
      if(!userRes.ok){
        deconnexion()
      }else{
        setUser(JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY)!))
        setValidToken(true)

      }
    }

  }
  const startLogin = async () => {
    if(!currentlyVerifyingLogin) {
      setCurrentlyVerifyingLogin(true)
      await login()
      setCurrentlyVerifyingLogin(false)
    }
  }

  const startVerifyToken = async () => {
   if (!currentlyVerifyingToken && Date.now() - lastVerifiedTimestamp > 3000 && isValidToken){
     setLastVerifiedTimeStamp(Date.now())
     setCurrentlyVerifyingToken(true)
      await verifyToken()
     setCurrentlyVerifyingToken(false)
   }
  }

  startLogin()
  startVerifyToken()
  if (user == emptyUser) {
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

  if (user.userType == "student") {
    return (
      <Container className="vh-100">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<StudentDashboard deconnexion={deconnexion} user={user} />} />
            <Route path="*" element={<h1 className="text-center text-white display-1">404 - Page pas trouvé</h1>} />
          </Routes>
        </BrowserRouter>
      </Container>
    );
  }

  else if (user.userType == "company") {
    return (
      <Container className="vh-100">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<CompanyDashboard deconnexion={deconnexion} user={user} />} />
            <Route path="/soumettreOffre" element={<FormulaireSoumissionPage user={user}/>} />
            <Route path="*" element={<h1 className="text-center text-white display-1">404 - Page pas trouvé</h1>} />
          </Routes>
        </BrowserRouter>
      </Container>
    );
  }

  else if (user.userType == "gestionnaire") {
    return (
      <Container className="vh-100">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<GestionnaireDashboard deconnexion={deconnexion} user={user} />} />
            <Route path="/userValidation" element={<UserValidation connectedUser={user}/>} />
            <Route path="*" element={<h1 className="text-center text-white display-1">404 - Page pas trouvé</h1>} />
          </Routes>
        </BrowserRouter>
      </Container>
    );
  }

  return <h1 className="vh-100">Oops</h1>
}

export default App;
