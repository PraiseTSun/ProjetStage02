import {BrowserRouter} from "react-router-dom";
import GestionnaireDashboardPage from "../GestionnaireDashboardPage";
import {act, fireEvent, render, screen} from "@testing-library/react";
import React from "react";
import IUser from "../../../models/IUser";

describe("GestionnaireDashboardTests", () => {
    let deconnexion: Function
    let gestionnaire: IUser

    beforeEach(async () => {
        gestionnaire = {
            id: "1",
            firstName: "Yan",
            lastName: "Zhou",
            userType: "gestionnaire",
            token: "32432",
            companyName: "",
            cv: ""
        }

        deconnexion = jest.fn()

        await act(async () => {
            render(<GestionnaireDashboardPage user={gestionnaire}
                                              deconnexion={deconnexion}/>, {wrapper: BrowserRouter});
        });
    });

    it('DeconnectionButtonTest', async () => {
        const button = await screen.findByRole("button", {name: /Déconnexion/i});

        act(() => {
            fireEvent.click(button);
        });

        expect(deconnexion).toBeCalledTimes(1);
    });

    it('HasTitleTest', async () => {
        expect(await screen.findByText(`Bienvenue ${gestionnaire.firstName} ${gestionnaire.lastName}`))
            .toBeInTheDocument();
    });

    it('LinkTest', async () => {
        const linkUserValidation = await screen.findByRole("link",
            {name: /Validation des utilisateurs/i});
        const linkCvValidation = await screen.findByRole("link",
            {name: /Validation des curriculums vitae des étudiants/i})
        const linkValiderNouvelleOffre = await screen.findByRole("link",
            {name: /Validation nouvelle offre stage/i})

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