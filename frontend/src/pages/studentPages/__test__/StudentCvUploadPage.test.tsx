import {render, screen} from "@testing-library/react";
import StudentCvUploadPage from "../StudentCvUploadPage";
import {BrowserRouter} from "react-router-dom";
import * as React from "react";
import {emptyUser} from "../../../App";

describe('StudentCvUploadPageTests', () => {
    beforeEach(() => {
        render(<StudentCvUploadPage connectedUser={emptyUser}/>, {wrapper: BrowserRouter});
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
        const erreurElemeent = screen.getByText("Choix votre CV")
        expect(erreurElemeent).toBeInTheDocument();
    });
});