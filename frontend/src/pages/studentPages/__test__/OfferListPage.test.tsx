import {BrowserRouter} from "react-router-dom";
import OffersListPage from "../OffersListPage";
import {act, fireEvent, render, screen} from "@testing-library/react";
import * as React from "react";
import IUser from "../../../models/IUser";

describe("OffersListPageTests", () => {
    let user: IUser

    beforeEach(async () => {
        user = {
            id: "1",
            companyName: "",
            lastName: "Simpson",
            firstName: "Peter",
            cv: null,
            token: "",
            userType: "",
            cvToValidate: ""
        }

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
            } else if (url === "http://localhost:8080/studentApplys/1") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve({
                        studentId: "1",
                        offersId: []
                    })
                });
            } else if (url === "http://localhost:8080/applyToOffer/1_2") {
                return Promise.resolve({
                    ok: true
                });
            } else if (url === "http://localhost:8080/getOfferStudent/2") {
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

        await act(async () => {
            render(<OffersListPage connectedUser={user}/>, {wrapper: BrowserRouter});
        });
    });

    it("getOfferPDFTest", async () => {
        const pdfButton = await screen.findByRole("button", {name: /PDF/i});

        await act(async () => {
            fireEvent.click(pdfButton);
        });

        expect(await screen.findByRole("button", {name: /Fermer/i})).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/getOffers/1", expect.anything());
        expect(fetch).toBeCalledWith("http://localhost:8080/studentApplys/1", expect.anything());
        expect(fetch).toBeCalledWith("http://localhost:8080/getOfferStudent/2", expect.anything());
        expect(fetch).toBeCalledTimes(3);
    });

    it("fetchOffersTest", async () => {
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
            render(<OffersListPage connectedUser={user}/>, {wrapper: BrowserRouter});
        });

        const pdfButton = await screen.findByRole("button", {name: /Postuler/i});

        await act(async () => {
            fireEvent.click(pdfButton);
        });

        expect(await screen.findByText(/Déjà postulé/i)).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/getOffers/1", expect.anything());
        expect(fetch).toBeCalledWith("http://localhost:8080/studentApplys/1", expect.anything());
        expect(fetch).toBeCalledWith("http://localhost:8080/applyToOffer/1_2", expect.anything());
        // 5 Because of rerender
        expect(fetch).toBeCalledTimes(5);
    });
});