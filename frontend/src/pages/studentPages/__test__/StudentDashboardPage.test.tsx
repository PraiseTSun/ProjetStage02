import {BrowserRouter} from "react-router-dom";
import StudentDashboardPage from "../StudentDashboardPage";
import {act, fireEvent, render, screen} from "@testing-library/react";
import * as React from "react";
import IUser from "../../../models/IUser";

describe("StudentDashBoardTest", () => {
    let deconnexion: Function
    let student: IUser

    beforeEach(async () => {
        student = {
            id: "3",
            firstName: "Bartholomew",
            lastName: "Kuma",
            userType: "student",
            token: "324324332",
            cv: "",
            companyName: "",
            cvToValidate: ""
        }

        deconnexion = jest.fn()

        await act(async () => {
            render(<StudentDashboardPage user={student} deconnexion={deconnexion}/>,
                {wrapper: BrowserRouter});
        });

    });

    it('DeconnectionButtonTest', async () => {
        const button = await screen.findByRole("button", {name: /Déconnexion/i});

        act(() => {
            fireEvent.click(button);
        });

        expect(deconnexion).toBeCalledTimes(1)
    });

    it('TitleIsPresentTest', async () => {
        const h1 = await screen.findByText(`Bienvenue ${student.firstName} ${student.lastName}`);
        expect(h1).toBeInTheDocument();
    });

    it('LinkTest', async () => {
        const linkListe = await screen.findByRole("link", {name: /Liste de stages/i});

        act(() => {
            fireEvent.click(linkListe);
        });

        expect(window.location.href).toEqual("http://localhost/offres");
    });
})