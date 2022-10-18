import { act, fireEvent, render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { emptyUser } from "./App";
import OffersListPage from "./pages/OffersListPage";
import StudentCvValidationPage from "./pages/StudentCvValidationPage";
import ValiderNouvelleOffreStagePage from "./pages/ValiderNouvelleOffreStagePage";
import IUser from "./models/IUser";
import UploaderMonCV from "./pages/UploaderMonCV";
import CompanyDashboardPage from "./pages/CompanyDashboardPage";
import ConfirmationPage from "./pages/ConfirmationPage";
import FormulaireSoumissionPage from "./pages/FormulaireSoumissionPage";
import GestionnaireDashboardPage from "./pages/GestionnaireDashboardPage";
import StudentDashboardPage from "./pages/StudentDashboardPage";
import LoginPage from "./pages/LoginPage";
import UserValidation from "./pages/UserValidationPage";
import * as React from "react";

const etudiant: IUser = {
    id: "1",
    token: "34245",
    firstName: "Yan",
    lastName: "Zhou",
    userType: "student",
    cv: ""
}

describe('test page uploader mon cv', () => {

    it('devrait rendre element input', async () => {
        render(<UploaderMonCV user={etudiant} />, { wrapper: BrowserRouter });
        const inputUploaderMonCV = screen.getByTestId("uploaderMonCV")
        expect(inputUploaderMonCV).toBeInTheDocument()
    });

    it('devrait rendre  element header est la ', async () => {
        render(<UploaderMonCV user={etudiant} />, { wrapper: BrowserRouter });
        const h4Element = screen.getByRole("heading", { name: /Téléverser CV/i });
        expect(h4Element).toBeInTheDocument();
    });

    it('devrait rendre  element button est la ', async () => {
        render(<UploaderMonCV user={etudiant} />, { wrapper: BrowserRouter });
        const buttonElement = screen.getByRole("button", { name: /Envoyer/i });
        expect(buttonElement).toBeInTheDocument();
    });

    it('devrait rendre element erreur est la ', async () => {
        render(<UploaderMonCV user={etudiant} />, { wrapper: BrowserRouter });
        const erreurElemeent = screen.getByText("Choix votre CV")
        expect(erreurElemeent).toBeInTheDocument();
    });
});


describe("StudentCvValidationPageTests", () => {

    const MockPage = () => {
        return (
            <BrowserRouter>
                <StudentCvValidationPage connectedUser={emptyUser} deconnexion={() => null} />
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
                return Promise.resolve({
                    ok: true
                });
            } else if (url === "http://localhost:8080/refuseCv/1") {
                return Promise.resolve({
                    ok: true
                });
            } else if (url === "http://localhost:8080/studentCv/1") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve({
                        "pdf": "[10]"
                    })
                });
            } else throw new Error("Bad url call");
        }
        ) as jest.Mock;

        const intersectionObserverMock = () => ({
            observe: () => null,
            unobserve: () => null,
            disconnect: () => null
        })
        window.IntersectionObserver = jest.fn().mockImplementation(intersectionObserverMock);

        window.alert = jest.fn(() => null) as jest.Mock;
    })

    it("fetchUnvalidatedCvStudentsTest", async () => {
        await act(async () => {
            render(<MockPage />);
        });

        expect(await screen.findByText(/Bart/i)).toBeInTheDocument();
        expect(await screen.findByText(/Simpson/i)).toBeInTheDocument();
        expect(await screen.findByText(/Informatique/i)).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/unvalidatedCvStudents", expect.anything());
        expect(fetch).toBeCalledTimes(1);
    });

    it("validateCvTest", async () => {
        await act(async () => {
            render(<MockPage />);
        });

        const validateButton = await screen.findByRole("button", { name: /O/i });
        const studentName = await screen.findByText(/Bart/i);

        await act(async () => {
            fireEvent.click(validateButton);
        });

        expect(studentName).not.toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/validateCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("refuseCvTest", async () => {
        await act(async () => {
            render(<MockPage />);
        });

        const validateButton = await screen.findByRole("button", { name: /X/i });
        const studentName = await screen.findByText(/Bart/i);

        await act(async () => {
            fireEvent.click(validateButton);
        });

        expect(studentName).not.toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/refuseCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("getPDFTest", async () => {
        await act(async () => {
            render(<MockPage />);
        });

        const cvButton = await screen.findByRole("button", { name: /CV/i });

        await act(async () => {
            fireEvent.click(cvButton);
        });

        expect(await screen.findByRole("button", { name: /Fermer/i })).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/studentCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    afterEach(() => {
        expect(fetch).toBeCalledWith("http://localhost:8080/unvalidatedCvStudents", expect.anything());
    });
});

describe("CompanyDashboardPage test", () => {
    const deconnexion = jest.fn()
    const MockPage = () => {
        return (
            <BrowserRouter>
                <CompanyDashboardPage deconnexion={deconnexion} user={company} />
            </BrowserRouter>
        );
    }

    it("CompanyDashboardLayoutTest", async () => {

        await act(async () => {
            render(<MockPage />)

        })

        expect(await screen.findByText(/Bienvenue Bob Marley/i)).toBeInTheDocument()
        expect(await screen.findByRole("link", { name: /Soumettre une offre de stage/i })).toBeInTheDocument()
        expect(await screen.findByRole("button", { name: /déconnexion/i })).toBeInTheDocument()
    })
    it("CompanyDashboardSoumettreOffreButtonTest", async () => {


        await act(async () => {
            render(<MockPage />)
        })
        const offreStageButton = await screen.findByRole("link", { name: /soumettre une offre de stage/i });

        await act(async () => {
            fireEvent.click(offreStageButton);
        });
        expect(window.location.href).toBe("http://localhost/soumettreOffre")
        //expect(await screen.findByText(/Formulaire de soumission de stage/i)).toBeInTheDocument();
    })
    it("CompanyDashboardDeconnexionButtonTest", async () => {


        await act(async () => {
            render(<MockPage />)
        })
        const deconnexionButton = await screen.findByRole("button", { name: /déconnexion/i });

        await act(async () => {
            fireEvent.click(deconnexionButton);
        });
        expect(deconnexion).toBeCalledTimes(1)
    })
})

describe("ConfirmationPage test", () => {
    beforeEach(() => {
        global.fetch = jest.fn(() => {
            return Promise.resolve({
                ok: true,
            });
        }
        ) as jest.Mock;

        window.alert = jest.fn(() => null) as jest.Mock;
    })
    const MockPage = () => {
        return (
            <BrowserRouter>
                <ConfirmationPage />
            </BrowserRouter>
        );
    }
    it("ConfirmationPageLayoutTest", async () => {
        await act(async () => {
            render(<MockPage />)
        })
        expect(await screen.findByText(/Succès! Vous pouvez fermez cette page/i)).toBeInTheDocument()
    })
});

describe("FormulaireSoumissionPageTest", () => {
    const MockPage = () => {
        return (
            <BrowserRouter>
                <FormulaireSoumissionPage user={gestionnaire} />
            </BrowserRouter>
        );
    }
    it("FormulaireSoumissionInvalidFormTest", async () => {
        await act(async () => {
            render(<MockPage />)
        })
        const array = [
            /Nom de la compagnie/i,
            /Département/i,
            /Poste/i,
            /Heures par semaine/i,
            /Salaire horaire/i,
            /Adresse/i,
            /Document PDF/i,
        ]
        for (const item of array) {
            const element = await screen.findAllByText(item)
            expect(element.length).not.toBe(0)
        }
        const bouton = await screen.findByRole("button", { name: /Envoyer/i })
        expect(bouton).toBeInTheDocument()
        await act(async () => {
            fireEvent.click(bouton)
        })
        const champRequis = await screen.findAllByText(/champ requis/i)
        expect(champRequis.length).toBe(5)
        const heures = await screen.findByText(/Nombre d'heures entre 0 et 40/i)
        expect(heures).toBeInTheDocument()
        const salaire = await screen.findByText(/Salaire doit etre plus haut que 15/i)
        expect(salaire).toBeInTheDocument()
    })
})

describe("test UserValidationPage", () => {

    const MockPage = () => {
        return (
            <BrowserRouter>
                <UserValidation connectedUser={gestionnaire} />
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

    it("UserValidationPageStudentList", async () => {

        await act(async () => {
            await render(<MockPage />)
        })
        const etudiantButton = await screen.findByDisplayValue(/Student/i)
        expect(etudiantButton).toBeInTheDocument()
        await act(async () => {
            fireEvent.click(etudiantButton)
        })

        expect(fetch).toBeCalledTimes(1)
        const acceptButton = await screen.findByRole("button", { name: /O/i })
        const name = await screen.findByText(/Bartholomew/i)
        expect(acceptButton).toBeInTheDocument()
        expect(name).toBeInTheDocument()
    });

    it("UserValidationPageCompanyTest", async () => {
        await act(async () => {
            await render(<MockPage />)
        })

        const companyButton = await screen.findByTestId("companyInput");
        expect(companyButton).toBeInTheDocument();

        await act(async () => {
            fireEvent.click(companyButton);
        })
        expect(fetch).toBeCalledTimes(2);

        const acceptButton = await screen.findByRole("button", { name: /O/i })
        const name = await screen.findByText(/Duff/i)
        expect(acceptButton).toBeInTheDocument()
        expect(name).toBeInTheDocument()
    })


    it("UserValidationPageCreateGestTest", async () => {
        await act(async () => {
            render(<MockPage />)
        })
    });

})
const gestionnaire: IUser = {
    id: "1",
    firstName: "Yan",
    lastName: "Zhou",
    userType: "gestionnaire",
    token: "32432",
    cv: ""
}

const company: any = {
    id: "2",
    companyName: "Duff",
    firstName: "Bob",
    lastName: "Marley",
    userType: "company",
    token: "324324332"
}
const student: IUser = {
    id: "3",
    firstName: "Bartholomew",
    lastName: "Kuma",
    userType: "student",
    token: "324324332",
    cv: ""
}

const offres: object[] = [{
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

describe("test LoginPage", () => {
    const setUser = jest.fn()

    it("test text header", () => {
        render(<LoginPage setUser={setUser} />)
        const headerElement = screen.getByText(/OSE KILLER/i)
        expect(headerElement).toBeInTheDocument()
    })

    it("test text inscriptionForm", async () => {
        await act(async () => {
            render(<LoginPage setUser={setUser} />)
        })
        const linkElement = await screen.findByText(/Nouveau utilisateur\? Inscrivez vous ici./i)
        fireEvent.click(linkElement)
        const elementEntreprise = await screen.getByTestId(/entrepriseInscriptionForm/i)
        const elementEtudiant = await screen.getByText(/Etudiant/i)
        expect(elementEntreprise).toBeInTheDocument()
        expect(elementEtudiant).toBeInTheDocument()

    })

    it("test text formulaireEntreprise avec les infos erreurs", async () => {
        await act(async () => {
            render(<LoginPage setUser={setUser} />)
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

        await act(async () => {
            fireEvent.change(inputNomElement, { target: { value: "n" } })
            fireEvent.change(inputPrenomElement, { target: { value: "n" } })
            fireEvent.change(inputEntrepriseElement, { target: { value: "e" } })
            fireEvent.change(inputCourrielElement, { target: { value: "cour" } })
            fireEvent.change(inputMotDePasseElement, { target: { value: 12345 } })
            fireEvent.change(inputVerifierMotDePasseElement, { target: { value: 12345678 } })
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

    it("test text formulaireEtudiant avec les infos erreurs", async () => {
        await act(async () => {
            render(<LoginPage setUser={setUser} />)
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

        await act(async () => {
            fireEvent.change(inputNomElement, { target: { value: "e" } })
            fireEvent.change(inputPrenomElement, { target: { value: "e" } })
            fireEvent.change(inputCourrielElement, { target: { value: "cour" } })
            fireEvent.change(inputMotDePasseElement, { target: { value: 12345 } })
            fireEvent.change(inputVerifierMotDePasseElement, { target: { value: 12345678 } })
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

    it("test text LoginForm", async () => {
        render(<LoginPage setUser={setUser} />)
        const tiggkeElementEtudiant = await screen.getByText(/Étudiant/i)
        const tiggkeElementEntreprise = await screen.getByText(/Entreprise/i)
        const tiggkeElementEtudiantGestionnaire = await screen.getByText(/Gestionnaire/i)
        const labelElementCourriel = await screen.getByText(/Adresse courriel/i)
        const inputElementCourriel = await screen.getByTestId("courreielLoginForm")
        const labelElementMotDePasse = await screen.getByTestId("labelmotDePasseLoginForm")
        const inputElementMotDePasse = await screen.getByTestId("motDePasseLoginForm")
        const buttonElement = await screen.getByRole("button", { name: /Connecter/i })

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

describe("OffersListPageTests", () => {

    const user = emptyUser;
    user.id = "1";
    user.cv = null

    const MockPage = ({ user }: { user: IUser }) => {
        return (
            <BrowserRouter>
                <OffersListPage connectedUser={user} />
            </BrowserRouter>
        );
    }

    beforeEach(() => {
        global.fetch = jest.fn((url) => {
            if (url === "http://localhost:8080/getOffers/1") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve([{
                        "id": 2,
                        "nomDeCompagnie": "Duff Beer",
                        "position": "Delivery Man",
                        "heureParSemaine": "40",
                        "adresse": "123 Joe street",
                        "salaire": "16"
                    }])
                });
            }
            else if (url === "http://localhost:8080/studentApplys/1") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve({
                        studentId: "1",
                        offersId: []
                    })
                });
            }
            else if (url === "http://localhost:8080/applyToOffer/1_2") {
                return Promise.resolve({
                    ok: true
                });
            }
            else if (url === "http://localhost:8080/getOfferStudent/2") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve({
                        "pdf": "[10]"
                    })
                });
            } else throw new Error("Bad url call");
        }) as jest.Mock;

        const intersectionObserverMock = () => ({
            observe: () => null,
            unobserve: () => null,
            disconnect: () => null
        })
        window.IntersectionObserver = jest.fn().mockImplementation(intersectionObserverMock);
    });

    it("getOfferPDFTest", async () => {
        await act(async () => {
            render(<MockPage user={user} />);
        });

        const pdfButton = await screen.findByRole("button", { name: /PDF/i });

        await act(async () => {
            fireEvent.click(pdfButton);
        });

        expect(await screen.findByRole("button", { name: /Fermer/i })).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/getOffers/1", expect.anything());
        expect(fetch).toBeCalledWith("http://localhost:8080/studentApplys/1", expect.anything());
        expect(fetch).toBeCalledWith("http://localhost:8080/getOfferStudent/2", expect.anything());
        expect(fetch).toBeCalledTimes(3);
    });

    it("fetchOffersTest", async () => {
        await act(async () => {
            render(<MockPage user={user} />);
        });

        expect(await screen.findByText(/Duff Beer/i)).toBeInTheDocument();
        expect(await screen.findByText(/Delivery Man/i)).toBeInTheDocument();
        expect(await screen.findByText(/16/i)).toBeInTheDocument();
        expect(await screen.findByText(/40/i)).toBeInTheDocument();
        expect(await screen.findByText(/123 joe street/i)).toBeInTheDocument();
        expect(await screen.findByText(/vous n'avez pas de cv/i)).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/getOffers/1", expect.anything());
        expect(fetch).toBeCalledWith("http://localhost:8080/studentApplys/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("ApplyOfferTest", async () => {
        user.cv = ""
        await act(async () => {
            render(<MockPage user={user} />);
        });

        const pdfButton = await screen.findByRole("button", { name: /Postuler/i });

        await act(async () => {
            fireEvent.click(pdfButton);
        });

        expect(await screen.findByText(/Déjà postulé/i)).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/getOffers/1", expect.anything());
        expect(fetch).toBeCalledWith("http://localhost:8080/studentApplys/1", expect.anything());
        expect(fetch).toBeCalledWith("http://localhost:8080/applyToOffer/1_2", expect.anything());
        expect(fetch).toBeCalledTimes(3);
    });
});

describe("OffresValidationPageTests", () => {

    const MockPage = () => {
        return (
            <BrowserRouter>
                <ValiderNouvelleOffreStagePage connectedUser={emptyUser} deconnexion={() => null} />
            </BrowserRouter>
        );
    }

    beforeEach(() => {
        global.fetch = jest.fn((url) => {
            if (url === "http://localhost:8080/unvalidatedOffers") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve([{
                        id: "1",
                        nomDeCompagnie: "Compagnie de Yan",
                        department: "Informatique",
                        position: "Developpeur",
                        heureParSemaine: "40",
                        salaire: "30",
                        adresse: "123 Rue Pomme",
                    }])
                });
            } else if (url === "http://localhost:8080/validateOffer/1") {
                return Promise.resolve({
                    ok: true
                });
            } else if (url === "http://localhost:8080/removeOffer/1") {
                return Promise.resolve({
                    ok: true
                });
            } else if (url === "http://localhost:8080/offerPdf/1") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve({
                        "pdf": "[10]"
                    })
                });
            } else throw new Error("Bad url call");
        }) as jest.Mock;

        const intersectionObserverMock = () => ({
            observe: () => null,
            unobserve: () => null,
            disconnect: () => null
        })
        window.IntersectionObserver = jest.fn().mockImplementation(intersectionObserverMock) as jest.Mock;

        window.alert = jest.fn(() => null) as jest.Mock;
    })

    it("fetchUnvalidatedOffersTest", async () => {
        await act(async () => {
            render(<MockPage />);
        });

        expect(await screen.findByText(/Compagnie de Yan/i)).toBeInTheDocument();
        expect(await screen.findByText(/Informatique/i)).toBeInTheDocument();
        expect(await screen.findByText(/Developpeur/i)).toBeInTheDocument();
        expect(await screen.findByText(/30\$\/h/i)).toBeInTheDocument();
        expect(await screen.findByText(/40/i)).toBeInTheDocument();
        expect(await screen.findByText(/123 rue pomme/i)).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/unvalidatedOffers", expect.anything());
        expect(fetch).toBeCalledTimes(1);
    });

    it("validateOfferTest", async () => {
        await act(async () => {
            render(<MockPage />);
        });

        const companyName = await screen.findByText(/Compagnie de yan/i)
        const validateButton = await screen.findByRole("button", { name: /O/i });

        await act(async () => {
            fireEvent.click(validateButton);
        });

        expect(companyName).not.toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/validateOffer/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("refuseOfferTest", async () => {
        await act(async () => {
            render(<MockPage />);
        });

        const companyName = await screen.findByText(/Compagnie de Yan/i);
        const validateButton = await screen.findByRole("button", { name: /X/i });

        await act(async () => {
            fireEvent.click(validateButton);
        });

        expect(companyName).not.toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/removeOffer/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("getPDFTest", async () => {
        await act(async () => {
            render(<MockPage />);
        });

        const cvButton = await screen.findByRole("button", { name: /pdf/i });

        await act(async () => {
            fireEvent.click(cvButton);
        });

        expect(await screen.findByRole("button", { name: /Fermer/i })).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/offerPdf/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it('test il y a le champs header ', async () => {
        await act(async () => {
            render(<MockPage />);
        });

        const h1Element = screen.getByRole("heading", { name: /Validation des offres/i });
        expect(h1Element).toBeInTheDocument();
    });

    it('test il y a le champs Link ', async () => {
        await act(async () => {
            render(<MockPage />);
        });

        const LinkElement = screen.getByRole("link", { name: /Home/i });
        expect(LinkElement).toBeInTheDocument();

    });

    it('test il y a le champs tr ', async () => {
        await act(async () => {
            render(<MockPage />);
        });

        const trElement = await screen.getAllByTestId("offre-container");
        expect(trElement.length).toBe(1);
    });

    it('test il y a le champs table ', async () => {
        await act(async () => {
            render(<MockPage />);
        });

        const tableElement = screen.getByTestId("tableValiderNouvelleOffreStage");
        expect(tableElement).toBeInTheDocument();
    });
});
describe("test la page StudentDashboard", () => {
    const deconnexion = jest.fn()
    const MockStudentDashboardPage = () => {
        return (
            <BrowserRouter>
                <StudentDashboardPage user={student} deconnexion={deconnexion} />
            </BrowserRouter>
        );
    }

    it('test button déconnexion ', async () => {
        act(() => {
            render(<MockStudentDashboardPage />);
        });
        const button = await screen.findByRole("button", { name: /Déconnexion/i });

        act(() => {
            fireEvent.click(button);
        });

        expect(deconnexion).toBeCalledTimes(1)

    });
    it('test title ', async () => {
        act(() => {
            render(<MockStudentDashboardPage />);
        });

        const h1 = await screen.findByTestId("titleStudentDashboard")
        expect(h1).toBeInTheDocument()

    });

    it('test link ', async () => {
        act(() => {
            render(<MockStudentDashboardPage />);
        });
        const linkListe = await screen.findByRole("link", { name: /Liste de stages/i });

        act(() => {
            fireEvent.click(linkListe);
        });
        expect(window.location.href).toEqual('http://localhost/offres');
    });
})

describe("test la page gestionnaireDashboard", () => {
    const deconnexion = jest.fn()
    const MockGestionnaireDashboardPage = () => {
        return (
            <BrowserRouter>
                <GestionnaireDashboardPage user={gestionnaire} deconnexion={deconnexion} />
            </BrowserRouter>
        );
    }

    it('test button déconnexion ', async () => {
        act(() => {
            render(<MockGestionnaireDashboardPage />);
        });
        const button = await screen.findByRole("button", { name: /Déconnexion/i });

        act(() => {
            fireEvent.click(button);
        });
        expect(deconnexion).toBeCalledTimes(1)


    });
    it('test title ', async () => {
        act(() => {
            render(<MockGestionnaireDashboardPage />);
        });

        const h1 = await screen.findByTestId("titleGestionnaireDashboard")
        expect(h1).toBeInTheDocument()

    });

    it('test link ', async () => {
        act(() => {
            render(<MockGestionnaireDashboardPage />);
        });
        const linkUserValidation = await screen.findByRole("link", { name: /Validation des utilisateurs/i });
        const linkCvValidation = await screen.findByRole("link", { name: /Validation des curriculums vitae des étudiants/i })
        const linkValiderNouvelleOffre = await screen.findByRole("link", { name: /Validation nouvelle offre stage/i })
        act(() => {
            fireEvent.click(linkUserValidation);
        });
        expect(window.location.href).toEqual('http://localhost/userValidation');
        act(() => {
            fireEvent.click(linkCvValidation);
        });
        expect(window.location.href).toEqual('http://localhost/cvValidation');
        act(() => {
            fireEvent.click(linkValiderNouvelleOffre);
        });
        expect(window.location.href).toEqual('http://localhost/validerNouvelleOffre');
    });
})
