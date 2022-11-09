import {act, render, screen} from "@testing-library/react";
import StudentCvUploadPage from "../StudentCvUploadPage";
import {BrowserRouter} from "react-router-dom";
import * as React from "react";
import {emptyUser} from "../../../App";

describe('StudentCvUploadPageTests', () => {
    beforeEach(async () => {
        global.fetch = jest.fn((url) => {
                if (url === "http://localhost:8080/getStatutValidationCV/1") {
                    return Promise.resolve({
                        status: 200,
                        json: () => Promise.resolve({
                            "state": "NOTHING",
                            "message": "",
                        })
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

        const student = emptyUser;
        student.id = "1"

        await act(async () => {
            await render(<StudentCvUploadPage connectedUser={student}/>, {wrapper: BrowserRouter});
        });

    });

    it('InputIsPresentTest', async () => {
        const inputUploaderMonCV = await screen.getByTestId("uploaderMonCV")
        expect(inputUploaderMonCV).toBeInTheDocument()
    });

    it('HeaderIsPresentTest', async () => {
        const h4Element = await screen.getByRole("heading", {name: /Téléverser CV/i});
        expect(h4Element).toBeInTheDocument();
    });

    it('ButtonIsPresentTest', async () => {
        const buttonElement = await screen.getByRole("button", {name: /Envoyer/i});
        expect(buttonElement).toBeInTheDocument();
    });

    it('ErrorIsPresentTest', async () => {
        const erreurElement = await screen.getByText("Choix votre CV")
        expect(erreurElement).toBeInTheDocument();
    });

    it('H2IsPresentTest', async () => {
        const h2Element = await screen.getByText(/Mon CV/i)
        expect(h2Element).toBeInTheDocument()
    })

    it('thStateParDefautIsPresentTest', async () => {
        const hrStateElement = await screen.findByTestId("State")
        expect(hrStateElement).toBeInTheDocument()
    })

    it('thMessageParDefautIsPresentTest', async () => {
        const thMessageElement = await screen.findByTestId(/RefusalMessage/i)
        expect(thMessageElement).toBeInTheDocument()
    })

    it('textCVToValidationParDefautIsPresentTest', async () => {
        const textCVToValidationElement = await screen.getByText(/Cv To Validate :/i)
        expect(textCVToValidationElement).toBeInTheDocument()
    })

    it('buttonCVToValidationParDefautIsPresentTest', async () => {
        const buttonCVToValidationElement = await screen.findByTestId("CvToValidate")
        expect(buttonCVToValidationElement).toBeInTheDocument()
    })
});