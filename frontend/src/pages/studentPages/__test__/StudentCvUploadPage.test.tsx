import {render, screen} from "@testing-library/react";
import StudentCvUploadPage from "../StudentCvUploadPage";
import {BrowserRouter} from "react-router-dom";
import * as React from "react";
import {emptyUser} from "../../../App";
import CvStatus from "../../../models/CvStatus";

describe('StudentCvUploadPageTests', () => {
    let status: CvStatus
    beforeEach(() => {
        render(<StudentCvUploadPage connectedUser={emptyUser}/>, {wrapper: BrowserRouter});
        status = {
            state: "En cours",
            message: "Attendez la validation"
        }
    });

    it('InputIsPresentTest', async () => {
        const inputUploaderMonCV = screen.getByTestId("uploaderMonCV")
        expect(inputUploaderMonCV).toBeInTheDocument()
    });

    it('HeaderIsPresentTest', async () => {
        const h4Element = screen.getByRole("heading", {name: /Téléverser CV/i});
        expect(h4Element).toBeInTheDocument();
    });

    it('ButtonIsPresentTest', async () => {
        const buttonElement = screen.getByRole("button", {name: /Envoyer/i});
        expect(buttonElement).toBeInTheDocument();
    });

    it('ErrorIsPresentTest', async () => {
        const erreurElement = screen.getByText("Choix votre CV")
        expect(erreurElement).toBeInTheDocument();
    });

    it('H2IsPresentTest', async () => {
        const h2Element = screen.getByText(/Mon CV/i)
        expect(h2Element).toBeInTheDocument()
    })

    it('H3StateParDefautIsPresentTest', async () => {
        const h3StateElement = screen.getByText(`State : ${status.state}`)
        expect(h3StateElement).toBeInTheDocument()
    })

    it('h3MessageParDefautIsPresentTest', async () => {
        const h3MessageElement = screen.getByText(`Message : ${status.message}`)
        expect(h3MessageElement).toBeInTheDocument()
    })
});