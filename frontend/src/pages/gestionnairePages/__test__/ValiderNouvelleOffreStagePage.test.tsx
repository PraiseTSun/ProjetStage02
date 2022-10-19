import {BrowserRouter} from "react-router-dom";
import ValiderNouvelleOffreStagePage from "../ValiderNouvelleOffreStagePage";
import {emptyUser} from "../../../App";
import {act, fireEvent, render, screen} from "@testing-library/react";
import * as React from "react";

describe("OffresValidationPageTests", () => {
    beforeEach(async () => {
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

        await act(async () => {
            render(<ValiderNouvelleOffreStagePage connectedUser={emptyUser}/>,
                {wrapper: BrowserRouter});
        });
    })

    it("FetchUnvalidatedOffersTest", async () => {
        expect(await screen.findByText(/Compagnie de Yan/i)).toBeInTheDocument();
        expect(await screen.findByText(/Informatique/i)).toBeInTheDocument();
        expect(await screen.findByText(/Developpeur/i)).toBeInTheDocument();
        expect(await screen.findByText(/30\$\/h/i)).toBeInTheDocument();
        expect(await screen.findByText(/40/i)).toBeInTheDocument();
        expect(await screen.findByText(/123 rue pomme/i)).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/unvalidatedOffers", expect.anything());
        expect(fetch).toBeCalledTimes(1);
    });

    it("ValidateOfferTest", async () => {
        const companyName = await screen.findByText(/Compagnie de yan/i)
        const validateButton = await screen.findByRole("button", {name: /O/i});

        await act(async () => {
            fireEvent.click(validateButton);
        });

        expect(companyName).not.toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/validateOffer/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("RefuseOfferTest", async () => {
        const companyName = await screen.findByText(/Compagnie de Yan/i);
        const validateButton = await screen.findByRole("button", {name: /X/i});

        await act(async () => {
            fireEvent.click(validateButton);
        });

        expect(companyName).not.toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/removeOffer/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("GetPDFTest", async () => {
        const cvButton = await screen.findByRole("button", {name: /pdf/i});

        await act(async () => {
            fireEvent.click(cvButton);
        });

        expect(await screen.findByRole("button", {name: /Fermer/i})).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/offerPdf/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("HeaderIsPresentTest", async () => {
        const h1Element = screen.getByRole("heading", {name: /Validation des offres/i});
        expect(h1Element).toBeInTheDocument();
    });

    it("LinkTIsPresentest", async () => {
        const LinkElement = screen.getByRole("link", {name: /Home/i});
        expect(LinkElement).toBeInTheDocument();
    });

    it("TableElementIsPresentTest", async () => {
        const tableElement = await screen.findByRole("table");
        expect(tableElement).toBeInTheDocument();
    });
});