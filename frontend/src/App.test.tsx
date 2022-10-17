import { render, screen, fireEvent, waitForElementToBeRemoved, act } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { emptyUser } from "./App";
import OffersListPage from "./pages/OffersListPage";
import StudentCvValidationPage from "./pages/StudentCvValidationPage";
import ValiderNouvelleOffreStagePage from "./pages/ValiderNouvelleOffreStagePage";
import IUser from "./models/IUser";
import UploaderMonCV from "./pages/UploaderMonCV";

const etudiant: IUser = {
    id: "1",
    token: "34245",
    firstName: "Yan",
    lastName: "Zhou",
    userType: "student"
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
            }
            else if (url === "http://localhost:8080/validateCv/1") {
                return Promise.resolve({
                    ok: true
                });
            }
            else if (url === "http://localhost:8080/refuseCv/1") {
                return Promise.resolve({
                    ok: true
                });
            }
            else if (url === "http://localhost:8080/studentCv/1") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve({
                        "pdf": "[10]"
                    })
                });
            }
            else throw new Error("Bad url call");
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

        const studentName = await screen.findByText(/Bart/i);
        const validateButton = await screen.findByRole("button", { name: /O/i });

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

        const studentName = await screen.findByText(/Bart/i);
        const validateButton = await screen.findByRole("button", { name: /X/i });

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

describe("OffersListPageTests", () => {

    const MockPage = () => {
        const user = emptyUser;
        user.id = "1";

        return (
            <BrowserRouter>
                <OffersListPage connectedUser={user} deconnexion={() => null} />
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
                        "adresse": "123 Joe street"
                    }])
                });
            }
            else if (url === "http://localhost:8080/getOfferStudent/2") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve({
                        "pdf": "[10]"
                    })
                });
            }
            else throw new Error("Bad url call");
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
            render(<MockPage />);
        });

        const pdfButton = await screen.findByRole("button", { name: /PDF/i });

        await act(async () => {
            fireEvent.click(pdfButton);
        });

        expect(await screen.findByRole("button", { name: /Fermer/i })).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/getOffers/1", expect.anything());
        expect(fetch).toBeCalledWith("http://localhost:8080/getOfferStudent/2", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("fetchOffersTest", async () => {
        await act(async () => {
            render(<MockPage />);
        });

        expect(await screen.findByText(/Duff Beer/i)).toBeInTheDocument();
        expect(await screen.findByText(/40/i)).toBeInTheDocument();
        expect(await screen.findByText(/Delivery Man/i)).toBeInTheDocument();
        expect(await screen.findByText(/40/i)).toBeInTheDocument();
        expect(await screen.findByText(/123 joe street/i)).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/getOffers/1", expect.anything());
        expect(fetch).toBeCalledTimes(1);
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
                        adresse: "123 Rue Pomme",
                    }])
                });
            }
            else if (url === "http://localhost:8080/validateOffer/1") {
                return Promise.resolve({
                    ok: true
                });
            }
            else if (url === "http://localhost:8080/removeOffer/1") {
                return Promise.resolve({
                    ok: true
                });
            }
            else if (url === "http://localhost:8080/offerPdf/1") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve({
                        "pdf": "[10]"
                    })
                });
            }
            else throw new Error("Bad url call");
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
        expect(await screen.findByText(/40/i)).toBeInTheDocument();
        expect(await screen.findByText(/123 rue pomme/i)).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/unvalidatedOffers", expect.anything());
        expect(fetch).toBeCalledTimes(1);
    });

    it("validateOfferTest", async () => {
        await act(async () => {
            render(<MockPage />);
        });

        const companyName = await screen.findByText(/Compagnie de Yan/i);
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

        const trElement = screen.getAllByTestId("offre-container");
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
