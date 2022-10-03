import React from 'react';
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
import ValiderNouvelleOffreStagePage from "./pages/ValiderNouvelleOffreStagePage";
import PdfPage from "./pages/PdfPage";

export const LOCAL_STORAGE_KEY = "MASSI_BEST_PROGRAMMER_PROJET_STAGE_02_CURRENT_CONNECTED_USER"
const emptyUser: IUser = {
  firstName: "",
  lastName: "",
  userType: ""
}

function App() {
  const [user, setUser] = useState(emptyUser)
  const [isOvrirPDF, setIsOvrirPDF] = useState(false)
  const [pdfid, setPdfid] = useState(0)
  const [offresAttendreValide, setOffresAttendreValide] = useState([ {
      "id" : "2",
      "nomDeCompagnie" : "conpagnie",
      "department" : "Technologie de genie Informatique",
      "position": "position",
      "heureParSemaine": 40,
      "adresse" : "3232 rue bell",
      "pdf" : "monfichier.pdf"
  },
      {
          "id" : "2",
          "nomDeCompagnie" : "conpagnie",
          "department" : "Technologie de genie Informatique",
          "position": "position",
          "heureParSemaine": 40,
          "adresse" : "3232 rue bell",
          "pdf" : "monfichier.pdf"
      }])

  const deconnexion = () => {
    setUser(emptyUser)
    localStorage.removeItem(LOCAL_STORAGE_KEY)
  }

  if (localStorage.getItem(LOCAL_STORAGE_KEY) != null && user == emptyUser) {
    setUser(JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY)!))
  }

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
            <Route path="/soumettreOffre" element={<FormulaireSoumissionPage />} />
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
            <Route path="/userValidation" element={<UserValidation />} />
            <Route path="*" element={<h1 className="text-center text-white display-1">404 - Page pas trouvé</h1>} />
            <Route path="/validerOffreStage" element={<ValiderNouvelleOffreStagePage setPdfId={setPdfid} /> }></Route>
            <Route path="/pdf" element={<PdfPage setPdfId={setPdfid} pdfId={pdfid}/>} />
          </Routes>
        </BrowserRouter>
      </Container>
    );
  }

  return <h1 className="vh-100">Oops</h1>
}

export default App;
