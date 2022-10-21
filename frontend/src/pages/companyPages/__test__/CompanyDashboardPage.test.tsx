import {BrowserRouter} from "react-router-dom";
import CompanyDashboardPage from "../CompanyDashboardPage";
import {act, fireEvent, render, screen} from "@testing-library/react";
import React from "react";
import IUser from "../../../models/IUser";

describe("CompanyDashboardPageTests", () => {

    let deconnexion: Function
    let company: IUser

    beforeEach(async () => {
        deconnexion = jest.fn()

        company = {
            id: "2",
            companyName: "Duff",
            cv: "",
            firstName: "Bob",
            lastName: "Marley",
            userType: "company",
            token: "324324332"
        }


        await act(async () => {
            render(<CompanyDashboardPage deconnexion={deconnexion} user={company}/>, {wrapper: BrowserRouter})
        });
    });

    it("CompanyDashboardLayoutTest", async () => {
        expect(await screen.findByText(`Bienvenue ${company.firstName} ${company.lastName}`)).toBeInTheDocument();
        expect(await screen.findByRole("link", {name: /Soumettre une offre de stage/i})).toBeInTheDocument();
        expect(await screen.findByRole("button", {name: /déconnexion/i})).toBeInTheDocument();
    })

    it("CompanyDashboardSoumettreOffreButtonTest", async () => {
        const offreStageButton = await screen.findByRole("link", {name: /soumettre une offre de stage/i});

        await act(async () => {
            fireEvent.click(offreStageButton);
        });

        expect(window.location.href).toBe("http://localhost/soumettreOffre")
    })

    it("CompanyDashboardDeconnexionButtonTest", async () => {
        const deconnexionButton = await screen.findByRole("button", {name: /déconnexion/i});

        await act(async () => {
            fireEvent.click(deconnexionButton);
        });
        expect(deconnexion).toBeCalledTimes(1)
    })
})