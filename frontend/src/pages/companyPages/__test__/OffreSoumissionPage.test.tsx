import {BrowserRouter} from "react-router-dom";
import OffreSoumissionPage from "../OffreSoumissionPage";
import {act, fireEvent, render, screen} from "@testing-library/react";
import React from "react";
import IUser from "../../../models/IUser";

describe("OffreSoumissionPageTests", () => {
    let company: IUser

    beforeEach(async () => {
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
            render(<OffreSoumissionPage user={company}/>, {wrapper: BrowserRouter});
        });
    });

    it("OffreSoumissionInvalidFormTest", async () => {
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

        const bouton = await screen.findByRole("button", {name: /Envoyer/i})
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