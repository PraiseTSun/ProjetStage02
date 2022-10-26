import React from "react";
import CompanyOffersPage from "../CompanyOffersPage";
import {act, fireEvent, render, screen} from "@testing-library/react";
import {BrowserRouter} from "react-router-dom";
import IUser from "../../../models/IUser";
import IOffer from "../../../models/IOffer";

describe("CompanyOfferPageTests", () => {
    const company: IUser = {
        id: "1",
        token: "",
        firstName: "Duff",
        lastName: "Man",
        cv: "",
        companyName: "Duff Beer",
        userType: "company",
    }

    const offer: IOffer = {
        pdf: [],
        token: "",
        heureParSemaine: "40",
        salaire: "20",
        adresse: "123 Duff Street",
        id: "2",
        nomDeCompagnie: "Duff Beer",
        position: "Delivery Guy",
        department: "Informatique"
    }

    const student: IUser = {
        id: "3",
        token: "",
        firstName: "Bart",
        lastName: "Simpson",
        cv: "",
        companyName: "",
        userType: "",
    }

    beforeEach(async () => {
        global.fetch = jest.fn((url) => {
                if (url === "http://localhost:8080/company/validatedOffers/1") {
                    return Promise.resolve({
                        ok: true,
                        json: () => Promise.resolve([offer])
                    });
                } else if (url === "http://localhost:8080/offer/2/applications") {
                    return Promise.resolve({
                        ok: true,
                        json: () => Promise.resolve({applicants: [student]})
                    });
                } else if (url === "http://localhost:8080/studentCv/3") {
                    return Promise.resolve({
                        ok: true,
                        json: () => Promise.resolve({pdf: "[10]"})
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

        await act(async () => {
            render(<CompanyOffersPage connectedUser={company}/>, {wrapper: BrowserRouter})
        });
    })

    it("HeaderIsPresentTest", async () => {
        expect(await screen.findByText(/Mes offres/i)).toBeInTheDocument();
    });

    it("OfferIsPresentTest", async () => {
        expect(await screen.findByText(offer.position)).toBeInTheDocument();
        expect(await screen.findByText(offer.department)).toBeInTheDocument();
        expect(await screen.findByText(offer.nomDeCompagnie)).toBeInTheDocument();
    });

    it("StudentCvTest", async () => {
        const applicantsButton = await screen.findByRole("button", {name: /Applicants/i});

        await act(async () => {
            fireEvent.click(applicantsButton);
        });

        const cvButton = await screen.findByRole("button", {name: /cv/i});

        await act(async () => {
            fireEvent.click(cvButton);
        });

        expect(await screen.findByRole("button", {name: /fermer/i})).toBeInTheDocument();
    });

});