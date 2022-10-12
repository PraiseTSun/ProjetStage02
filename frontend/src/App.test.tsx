import { render, screen, fireEvent, waitForElementToBeRemoved, act } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { emptyUser } from "./App";
import OffersListPage from "./pages/OffersListPage";
import StudentCvValidationPage from "./pages/StudentCvValidationPage";
import ValiderNouvelleOffreStagePage from "./pages/ValiderNouvelleOffreStagePage";

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
        act(() => {
            render(<MockPage />);
        });

        expect(await screen.findByText(/Bart/i)).toBeInTheDocument();
        expect(await screen.findByText(/Simpson/i)).toBeInTheDocument();
        expect(await screen.findByText(/Informatique/i)).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/unvalidatedCvStudents", expect.anything());
        expect(fetch).toBeCalledTimes(1);
    });

    it("validateCvTest", async () => {
        act(() => {
            render(<MockPage />);
        });
        const validateButton = await screen.findByRole("button", { name: /O/i });

        act(() => {
            fireEvent.click(validateButton);
        });

        await waitForElementToBeRemoved(() => screen.queryByText(/Bart/i));
        expect(fetch).toBeCalledWith("http://localhost:8080/validateCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("refuseCvTest", async () => {
        act(() => {
            render(<MockPage />);
        });
        const validateButton = await screen.findByRole("button", { name: /X/i });

        act(() => {
            fireEvent.click(validateButton);
        });

        await waitForElementToBeRemoved(() => screen.queryByText(/Bart/i));
        expect(fetch).toBeCalledWith("http://localhost:8080/refuseCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("getPDFTest", async () => {
        act(() => {
            render(<MockPage />);
        });

        const cvButton = await screen.findByRole("button", { name: /CV/i });

        act(() => {
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
        act(() => {
            render(<MockPage />);
        });

        const pdfButton = await screen.findByRole("button", { name: /PDF/i });

        act(() => {
            fireEvent.click(pdfButton);
        });

        expect(await screen.findByRole("button", { name: /Fermer/i })).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/getOffers/1", expect.anything());
        expect(fetch).toBeCalledWith("http://localhost:8080/getOfferStudent/2", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("fetchOffersTest", async () => {
        act(() => {
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
        act(() => {
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
        act(() => {
            render(<MockPage />);
        });
        const validateButton = await screen.findByRole("button", { name: /O/i });

        act(() => {
            fireEvent.click(validateButton);
        });

        await waitForElementToBeRemoved(() => screen.queryByText(/Compagnie de yan/i));
        expect(fetch).toBeCalledWith("http://localhost:8080/validateOffer/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("refuseOfferTest", async () => {
        act(() => {
            render(<MockPage />);
        });
        const validateButton = await screen.findByRole("button", { name: /X/i });

        act(() => {
            fireEvent.click(validateButton);
        });

        await waitForElementToBeRemoved(() => screen.queryByText(/Compagnie de Yan/i));
        expect(fetch).toBeCalledWith("http://localhost:8080/removeOffer/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("getPDFTest", async () => {
        act(() => {
            render(<MockPage />);
        });

        const cvButton = await screen.findByRole("button", { name: /pdf/i });

        act(() => {
            fireEvent.click(cvButton);
        });

        expect(await screen.findByRole("button", { name: /Fermer/i })).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/offerPdf/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it('test il y a le champs header ', async () => {
        act(() => {
            render(<MockPage />);
        });

        const h1Element = screen.getByRole("heading", { name: /Validation des offres/i });
        expect(h1Element).toBeInTheDocument();
    });

    it('test il y a le champs Link ', async () => {
        act(() => {
            render(<MockPage />);
        });

        const LinkElement = screen.getByRole("link", { name: /Home/i });
        expect(LinkElement).toBeInTheDocument();

    });

    it('test il y a le champs tr ', async () => {
        act(() => {
            render(<MockPage />);
        });

        const trElement = screen.getAllByTestId("offre-container");
        expect(trElement.length).toBe(1);
    });

    it('test il y a le champs table ', async () => {
        act(() => {
            render(<MockPage />);
        });

        const tableElement = screen.getByTestId("tableValiderNouvelleOffreStage");
        expect(tableElement).toBeInTheDocument();
    });
});
