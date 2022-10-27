import {act, render, screen} from "@testing-library/react";
import * as React from "react";
import {BrowserRouter} from "react-router-dom";
import EvaluerLeMilieuDeStage from "../EvaluerLeMilieuDeStage";
import IUser from "../../../models/IUser";

describe("EvaluerLeMilieuDeStagePageTests", () => {
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
        await act(async () => {
            render(<EvaluerLeMilieuDeStage user={gestionnaire}/>, {wrapper: BrowserRouter});
        });
    });

    it("elementsIsPresentTest", async () => {
        expect(await screen.findByRole("link",{name: /Home/i})).toBeInTheDocument();
        expect(await screen.findByText(/Évaluation des offres/i)).toBeInTheDocument();

    });
    it("thIsPresentTest", async ()=>{
        expect(await screen.findByText(/Compagnie/i)).toBeInTheDocument();
        expect(await screen.findByText(/Position/i)).toBeInTheDocument();
        expect(await screen.findByText(/Heures par semaine/i)).toBeInTheDocument();
        expect(await screen.findByText(/Salaire/i)).toBeInTheDocument();
        expect(await screen.findByText(/Adresse/i)).toBeInTheDocument();
        expect(await screen.findByText(/Évalation/i)).toBeInTheDocument();
    })
});