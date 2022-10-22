import {act, fireEvent, render, screen} from "@testing-library/react";
import {emptyUser} from "../../../App";
import {BrowserRouter} from "react-router-dom";
import * as React from "react";
import PreviousOffersPage from "../PreviousOffersPage";


describe("PreviousOffersPageTests", () => {
    beforeEach(async () => {
        global.fetch = jest.fn((url) => {
            if (url === "http://localhost:8080/validatedOffers/2023") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve([{
                        id: "1",
                        nomDeCompagnie: "Duff 2023",
                        position: "Delivery Guy",
                        heureParSemaine: "40",
                        pdf: [],
                        salaire: "16",
                        department: "Informatique",
                        adresse: "123 Duff street",
                        token: ""
                    }])
                });
            } else if (url === "http://localhost:8080/validatedOffers/2022") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve([{
                        id: "1",
                        nomDeCompagnie: "Duff 2022",
                        position: "Delivery Guy",
                        heureParSemaine: "40",
                        pdf: [],
                        salaire: "16",
                        department: "Informatique",
                        adresse: "123 Duff street",
                        token: ""
                    }])
                });
            } else if (url === "http://localhost:8080/offerPdf/1") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve({
                        pdf: ["10"],
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

        window.alert = jest.fn(() => null) as jest.Mock;

        await act(async () => {
            render(<PreviousOffersPage connectedUser={emptyUser}/>,
                {wrapper: BrowserRouter});
        });
    });

    it("2023OffersTest", async () => {
        expect(await screen.findByText(/Duff 2023/i)).toBeInTheDocument();
    })

    it("2022OffersTest", async () => {
        const lastYearButton = await screen.findByTestId("2022")

        await act(async () => {
            fireEvent.click(lastYearButton)
        });

        expect(await screen.findByText(/Duff 2022/i)).toBeInTheDocument();
    })

    it("PDFTest", async () => {
        const pdfButton = await screen.findByRole("button", {name: "pdf"})

        await act(async () => {
            fireEvent.click(pdfButton)
        });

        expect(await screen.findByRole("button", {name: "Fermer"})).toBeInTheDocument();
    })
});