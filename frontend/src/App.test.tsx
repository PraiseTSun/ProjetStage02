import {act, fireEvent, render, screen, waitForElementToBeRemoved} from "@testing-library/react";
import {BrowserRouter} from "react-router-dom";
import {emptyUser} from "./App";
import StudentCvValidationPage from "./pages/StudentCvValidationPage";
import IUser from "./models/IUser";
import CompanyDashboardPage from "./pages/CompanyDashboardPage";
import ConfirmationPage from "./pages/ConfirmationPage";
import FormulaireSoumissionPage from "./pages/FormulaireSoumissionPage";
import GestionnaireDashboardPage from "./pages/GestionnaireDashboardPage";
import StudentDashboardPage from "./pages/StudentDashboardPage";
import LoginPage from "./pages/LoginPage";
import UserValidation from "./pages/UserValidationPage";
import LoginForm from "./components/LoginForm";
import exp from "constants";

describe("StudentCvValidationPage test", () => {

    const MockPage = () => {
        return (
            <BrowserRouter>
                <StudentCvValidationPage connectedUser={emptyUser} deconnexion={() => null}/>
            </BrowserRouter>
        );
    }

    beforeEach(() => {
        global.fetch = jest.fn((url) => {
                if (url === "http://localhost:8080/unvalidatedCvStudents") {
                    return Promise.resolve({
                        ok: true,
                        json: () => Promise.resolve([{
                            "id": 1,
                            "firstName": "Bart",
                            "lastName": "Simpson",
                            "department": "Informatique"
                        }])
                    });
                } else if (url === "http://localhost:8080/validateCv/1") {
                    console.log("validate")
                    return Promise.resolve({
                        ok: true
                    });
                } else if (url === "http://localhost:8080/refuseCv/1") {
                    return Promise.resolve({
                        ok: true
                    });
                } else if (url === "http://localhost:8080/studentCv/") {
                    return Promise.resolve({
                        ok: true,
                        json: () => Promise.resolve([{
                            "prf": []
                        }])
                    });
                } else throw new Error("Bad url call")
            }
        ) as jest.Mock;

        window.alert = jest.fn(() => null) as jest.Mock;
    })

    it("fetchUnvalidatedCvStudentsTest", async () => {
        act(() => {
            render(<MockPage/>)
        });

        expect(await screen.findByText(/Bart/i)).toBeInTheDocument();
        expect(await screen.findByText(/Simpson/i)).toBeInTheDocument();
        expect(await screen.findByText(/Informatique/i)).toBeInTheDocument();
        expect(fetch).toBeCalledTimes(1);
    });

    it("validateCvTest", async () => {
        act(() => {
            render(<MockPage/>)
        });
        const validateButton = await screen.findByRole("button", {name: /O/i});

        act(() => {
            fireEvent.click(validateButton);
        });

        await waitForElementToBeRemoved(() => screen.queryByText(/Bart/i));
        expect(fetch).toBeCalledWith("http://localhost:8080/validateCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("refuseCvTest", async () => {
        act(() => {
            render(<MockPage/>)
        });
        const validateButton = await screen.findByRole("button", {name: /X/i});

        act(() => {
            fireEvent.click(validateButton);
        });

        await waitForElementToBeRemoved(() => screen.queryByText(/Bart/i));
        expect(fetch).toBeCalledWith("http://localhost:8080/refuseCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("getPDFTest", async () => {
        act(() => {
            render(<MockPage/>)
        });
        const cvButton = await screen.findByRole("button", {name: /CV/i});

        act(() => {
            fireEvent.click(cvButton);
        });

        expect(fetch).toBeCalledWith("http://localhost:8080/studentCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    afterEach(() => {
        expect(fetch).toBeCalledWith("http://localhost:8080/unvalidatedCvStudents", expect.anything());
    });
});
describe("CompanyDashboardPage test", () => {


    it("CompanyDashboardLayoutTest", async () => {

        const MockPage = () => {

            return (
                <BrowserRouter>
                    <CompanyDashboardPage deconnexion={() => null} user={company}/>
                </BrowserRouter>
            );
        }

        act(() => {
            render(<MockPage/>)

        })

        expect(await screen.findByText(/Bienvenue Bob Marley/i)).toBeInTheDocument()
        expect(await screen.findByRole("link", {name: /Soumettre une offre de stage/i})).toBeInTheDocument()
        expect(await screen.findByRole("button", {name: /déconnexion/i})).toBeInTheDocument()
    })
    it("CompanyDashboardSoumettreOffreButtonTest", async () => {

        const MockPage = () => {

            return (
                <BrowserRouter>
                    <CompanyDashboardPage deconnexion={() => null} user={company}/>
                </BrowserRouter>
            );
        }

        act(() => {
            render(<MockPage/>)
        })
        const offreStageButton = await screen.findByRole("link", {name: /soumettre une offre de stage/i});

        act(() => {
            fireEvent.click(offreStageButton);
        });
        expect(window.location.href).toBe("http://localhost/soumettreOffre")
        //expect(await screen.findByText(/Formulaire de soumission de stage/i)).toBeInTheDocument();
    })
    it("CompanyDashboardDeconnexionButtonTest", async () => {
        const deconnexion = jest.fn()
        const MockPage = () => {
            return (
                <BrowserRouter>
                    <CompanyDashboardPage deconnexion={deconnexion} user={company}/>
                </BrowserRouter>
            );
        }

        act(() => {
            render(<MockPage/>)
        })
        const deconnexionButton = await screen.findByRole("button", {name: /déconnexion/i});

        act(() => {
            fireEvent.click(deconnexionButton);
        });
        expect(deconnexion).toBeCalledTimes(1)
    })
})

describe("ConfirmationPage test", () => {
    beforeEach(() => {
        jest.mock('react-router-dom', () => ({
            ...jest.requireActual('react-router-dom'),
            useParams:jest.fn(()=>
            {
                return {id: 1}
            }),
            useLocation: jest.fn().mockReturnValue({search:"userType=student"}),
        }))
        global.fetch = jest.fn((url) => {
                if (url === "http://localhost:8080/confirmEmail/student/1") {
                    return Promise.resolve({
                        created: true,
                    });
                } else throw new Error("Bad url call")
            }
        ) as jest.Mock;

        window.alert = jest.fn(() => null) as jest.Mock;
    })
    it("ConfirmationPageLayoutTest", async () => {
        const MockPage = () => {
            return (
                <BrowserRouter>
                    <ConfirmationPage/>
                </BrowserRouter>
            );
        }
        act(() => {
            render(<MockPage/>)
        })
        expect(await screen.findByText(/Succès! Vous pouvez fermez cette page/i)).toBeInTheDocument()
    })
});
const MockCompanyDashboardPage = () => {
    return (
        <BrowserRouter>
            <CompanyDashboardPage deconnexion={() => null} user={company}/>
        </BrowserRouter>
    );
}
const MockConfirmationPage = () => {
    return (
        <BrowserRouter>
            <ConfirmationPage/>
        </BrowserRouter>
    );
}
const MockFormulaireSoumissionPage = () => {
    return (
        <BrowserRouter>
            <FormulaireSoumissionPage user={gestionnaire}/>
        </BrowserRouter>
    );
}
const MockGestionnaireDashboardPage = () => {
    return (
        <BrowserRouter>
            <GestionnaireDashboardPage user={gestionnaire} deconnexion={() => null}/>
        </BrowserRouter>
    );
}
const MockStudentDashboardPage = () => {
    return (
        <BrowserRouter>
            <StudentDashboardPage user={student} deconnexion={() => null}/>
        </BrowserRouter>
    );
}
const MockLoginPage = () => {
    return (
        <BrowserRouter>
            <LoginPage setUser={() => null}/>
        </BrowserRouter>
    );
}
const MockStudentCvValidationPage = () => {
    return (
        <BrowserRouter>
            <StudentCvValidationPage connectedUser={gestionnaire} deconnexion={() => null}/>
        </BrowserRouter>
    );
}
describe("test UserValidationPage", () => {

    const MockPage = () => {
        return (
            <BrowserRouter>
                <UserValidation connectedUser={gestionnaire}/>
            </BrowserRouter>
        );
    }
    beforeEach(() => {
        global.fetch = jest.fn((url) => {
                if (url === "http://localhost:8080/unvalidatedStudents") {
                    return Promise.resolve({
                        ok: true,
                        json: () => Promise.resolve([student])
                    });
                } else if (url === "http://localhost:8080/unvalidatedCompanies") {
                    console.log("validate")
                    return Promise.resolve({
                        ok: true,
                        json: () => Promise.resolve([company])
                    });
                } else if (url === "http://localhost:8080/createGestionnaire") {
                    return Promise.resolve({
                        created: true
                    });
                } else throw new Error("Bad url call")
            }
        ) as jest.Mock;

        window.alert = jest.fn(() => null) as jest.Mock;
    })
})
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

const offres: object = [{
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

describe("test LoginPage", ()=>{
    const setUser = jest.fn()

    it("test text header",()=>{
        render(<LoginPage setUser={setUser}/>)
        const headerElement = screen.getByText(/OSE KILLER/i)
        expect(headerElement).toBeInTheDocument()
    })

    it("test text inscriptionForm",async ()=>{
        act(()=>{
            render(<LoginPage setUser={setUser}/>)
        })
        const linkElement = await screen.findByText(/Nouveau utilisateur\? Inscrivez vous ici./i)
        fireEvent.click(linkElement)
        const elementEntreprise =await screen.getByTestId(/entrepriseInscriptionForm/i)
        const elementEtudiant =await screen.getByText(/Etudiant/i)
        expect(elementEntreprise).toBeInTheDocument()
        expect(elementEtudiant).toBeInTheDocument()

    })

    it("test text formulaireEntreprise avec les infos erreurs",async ()=>{
        act(()=>{
            render(<LoginPage setUser={setUser}/>)
        })

        const linkElement = await screen.findByText(/Nouveau utilisateur\? Inscrivez vous ici./i)
        fireEvent.click(linkElement)
        const elementEntreprise = await screen.getByTestId(/entrepriseInscriptionForm/i)
        fireEvent.click(elementEntreprise)

        //dans la page formulaireEntreprise
        const inputNomElement = await screen.findByTestId("nomInputFormulaireEntreprise")
        const labelNomElement = await screen.findByTestId("nomLabelFormulaireEntreprise")
        const inputPrenomElement = await screen.findByTestId("prenomInputFormulaireEntreprise")
        const labelPrenomElement = await screen.findByTestId("prenomLabelFormulaireEntreprise")
        const inputEntrepriseElement = await screen.findByTestId("inputEntrepriseFormulaireEntreprise")
        const labelEntrepriseElement = await screen.findByTestId("labelEntrepriseFormulaireEntreprise")
        const inputCourrielElement = await screen.findByTestId("inputCourrielFormulaireEntreprise")
        const labelCourrielElement = await screen.findByTestId("labelCourrielFormulaireEntreprise")
        const inputMotDePasseElement = await screen.findByTestId("inputCourrielFormulaireEntreprise")
        const labelMotDePasseElement = await screen.findByTestId("labelCourrielFormulaireEntreprise")
        const inputVerifierMotDePasseElement = await screen.findByTestId("inputCourrielFormulaireEntreprise")
        const labelVerifierMotDePasseElement = await screen.findByTestId("labelCourrielFormulaireEntreprise")
        const labelDepartementElement = await screen.findByTestId("labelDepartementFormulaireEntreprise")
        const selectDepartementElement = await screen.findByTestId("selelctDepartementFormulaireEntreprise")
        const optionsDepartementElement = await screen.getAllByTestId("selelct-option-FormulaireEntreprise")
        const buttonElement = await screen.getByTestId("button_inscrire")

        act(()=>{
            fireEvent.change(inputNomElement, {target: {value: "n"}})
            fireEvent.change(inputPrenomElement, {target: {value: "n"}})
            fireEvent.change(inputEntrepriseElement, {target: {value: "e"}})
            fireEvent.change(inputCourrielElement, {target: {value: "cour"}})
            fireEvent.change(inputMotDePasseElement, {target: {value: 12345}})
            fireEvent.change(inputVerifierMotDePasseElement, {target: {value: 12345678}})
        })

        const erreurPourNomElement = await screen.findByTestId("errorNomFormulaireEntreprise")
        const erreurPourPrenomElement = await screen.findByTestId("errorPrenomFormulaireEntreprise")
        const erreurPourEntrepriseElement = await screen.findByTestId("errorEntrepriseFormulaireEntreprise")
        const erreurPourCourrielElement = await screen.findByTestId("errorCourrielFormulaireEntreprise")
        const erreurPourMotDePasseElement = await screen.findByTestId("errorMotDePasseFormulaireEntreprise")

        expect(erreurPourNomElement).toBeInTheDocument()
        expect(labelNomElement).toBeInTheDocument()
        expect(erreurPourPrenomElement).toBeInTheDocument()
        expect(labelPrenomElement).toBeInTheDocument()
        expect(erreurPourEntrepriseElement).toBeInTheDocument()
        expect(labelEntrepriseElement).toBeInTheDocument()
        expect(erreurPourCourrielElement).toBeInTheDocument()
        expect(labelCourrielElement).toBeInTheDocument()
        expect(erreurPourMotDePasseElement).toBeInTheDocument()
        expect(labelMotDePasseElement).toBeInTheDocument()
        expect(labelVerifierMotDePasseElement).toBeInTheDocument()
        expect(labelDepartementElement).toBeInTheDocument()
        expect(selectDepartementElement).toBeInTheDocument()
        expect(optionsDepartementElement.length).toBe(3)
        expect(buttonElement).toBeInTheDocument()
    })

    it("test text formulaireEtudiant avec les infos erreurs",async ()=>{
        act(()=>{
            render(<LoginPage setUser={setUser}/>)
        })

        const linkElement = await screen.findByText(/Nouveau utilisateur\? Inscrivez vous ici./i)
        fireEvent.click(linkElement)
        const elementEtudiant = await screen.getByText(/Etudiant/i)
        fireEvent.click(elementEtudiant)

        //dans la page formulaireEtudiant
        const inputNomElement = await screen.findByTestId("intputNomFormulaireEtudiant")
        const labelNomElement = await screen.findByTestId("labelNomFormulaireEtudiant")
        const inputPrenomElement = await screen.findByTestId("inputPrenomFormulaireEtudiant")
        const labelPrenomElement = await screen.findByTestId("labelPrenomFormulaireEtudiant")
        const inputCourrielElement = await screen.findByTestId("inputCourrielFormulaireEtudiant")
        const labelCourrielElement = await screen.findByTestId("labelCourrielFormulaireEtudiant")
        const inputMotDePasseElement = await screen.findByTestId("inputMotDePasseFormulaireEtudiant")
        const labelMotDePasseElement = await screen.findByTestId("labelMotDePasseFormulaireEtudiant")
        const inputVerifierMotDePasseElement = await screen.findByTestId("inputVerifierMotDePasseFormulaireEtudiant")
        const labelVerifierMotDePasseElement = await screen.findByTestId("labelVerifierMotDePasseFormulaireEtudiant")
        const labelDepartementElement = await screen.findByTestId("labelDePartementFormulaireEtudiant")
        const selectDepartementElement = await screen.findByTestId("selelctDepartementFormulaireEntreprise")
        const optionsDepartementElement = await screen.getAllByTestId("select-option-DePartementFormulaireEtudiant")
        const buttonElement = await screen.getByTestId("button_inscrire_etudiant")

        act(()=>{
            fireEvent.change(inputNomElement, {target: {value: "e"}})
            fireEvent.change(inputPrenomElement, {target: {value: "e"}})
            fireEvent.change(inputCourrielElement, {target: {value: "cour"}})
            fireEvent.change(inputMotDePasseElement, {target: {value: 12345}})
            fireEvent.change(inputVerifierMotDePasseElement, {target: {value: 12345678}})
        })

        const errorPourNomFormulaireEtudiant = await screen.findByTestId("errorNomFormulaireEtudiant")
        const erreurPourPrenomElement = await screen.findByTestId("errorPrenomFormulaireEtudiant")
        const erreurPourCourrielElement = await screen.findByTestId("errorCourrielFormulaireEtudiant")
        const erreurPourMotDePasseElement = await screen.findByTestId("errorMotDePasseFormulaireEtudiant")

        expect(errorPourNomFormulaireEtudiant).toBeInTheDocument()
        expect(labelNomElement).toBeInTheDocument()
        expect(erreurPourPrenomElement).toBeInTheDocument()
        expect(labelPrenomElement).toBeInTheDocument()
        expect(erreurPourCourrielElement).toBeInTheDocument()
        expect(labelCourrielElement).toBeInTheDocument()
        expect(erreurPourMotDePasseElement).toBeInTheDocument()
        expect(labelMotDePasseElement).toBeInTheDocument()
        expect(labelVerifierMotDePasseElement).toBeInTheDocument()
        expect(labelDepartementElement).toBeInTheDocument()
        expect(selectDepartementElement).toBeInTheDocument()
        expect(optionsDepartementElement.length).toBe(3)
        expect(buttonElement).toBeInTheDocument()
    })

    it("test text LoginForm",async ()=>{
        render(<LoginPage setUser={setUser}/>)
        const tiggkeElementEtudiant =await screen.getByText(/Étudiant/i)
        const tiggkeElementEntreprise =await screen.getByText(/Entreprise/i)
        const tiggkeElementEtudiantGestionnaire =await screen.getByText(/Gestionnaire/i)
        const labelElementCourriel =await screen.getByText(/Adresse courriel/i)
        const inputElementCourriel =await screen.getByTestId("courreielLoginForm")
        const labelElementMotDePasse =await screen.getByTestId("labelmotDePasseLoginForm")
        const inputElementMotDePasse =await screen.getByTestId("motDePasseLoginForm")
        const buttonElement =await screen.getByRole("button", {name:/Connecter/i})

        expect(tiggkeElementEtudiant).toBeInTheDocument()
        expect(tiggkeElementEntreprise).toBeInTheDocument()
        expect(tiggkeElementEtudiantGestionnaire).toBeInTheDocument()
        expect(labelElementCourriel).toBeInTheDocument()
        expect(inputElementCourriel).toBeInTheDocument()
        expect(screen.getByText(/courriel invalide/i)).toBeInTheDocument()
        expect(labelElementMotDePasse).toBeInTheDocument()
        expect(inputElementMotDePasse).toBeInTheDocument()
        expect(screen.getByText(/Mot de passe plus petit que 8 caractères/i)).toBeInTheDocument()
        expect(buttonElement).toBeInTheDocument()
    })
})