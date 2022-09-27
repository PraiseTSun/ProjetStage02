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

export const LOCAL_STORAGE_KEY = "MASSI_BEST_PROGRAMMER_PROJET_STAGE_02_CURRENT_CONNECTED_USER"
const emptyUser: IUser = {
  firstName: "",
  lastName: "",
  userType: ""
}

function App() {
  const [user, setUser] = useState(emptyUser)
  const [offresAttendreValide, setOffresAttendreValide] = useState([])

  const deconnexion = () => {
    setUser(emptyUser)
    localStorage.removeItem(LOCAL_STORAGE_KEY)
  }
  const fetchOffresAttendreValide = async () => {
    const res = await fetch(`http://localhost:8080/offresValide`)
    const data = await res.json()

    if(res.status === 200){
      setOffresAttendreValide(data)
    }
  }

  const valideOffre = async (id : number) => {
     const res = await fetch(`http://localhost:8080/offres/${id}`,
         {
           method:'PUT',
           headers:{
                'Content-Type':'application/json',
           },
           body:JSON.stringify(id)
         })
  }

  const deleteOffre = async (id : number) => {
    const res = await fetch(`http://localhost:8080/offres/${id}`,
        {
          method:'DELETE',
          headers:{
            'Content-Type':'application/json',
          },
          body:JSON.stringify(id)
        })
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
            <Route path="/validerOffres" element={<ValiderNouvelleOffreStagePage  fetchOffresAttendreValide={fetchOffresAttendreValide}  offresAttendreValide={offresAttendreValide} valideOffre={valideOffre} deleteOffre={deleteOffre}/>}></Route>
          </Routes>
        </BrowserRouter>
      </Container>
    );
  }

  return <h1 className="vh-100">Oops</h1>
}

export default App;
