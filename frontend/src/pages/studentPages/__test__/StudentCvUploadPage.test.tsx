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
                        ok: true,
                        json: () => Promise.resolve([{
                            "State": "NOTHING",
                            "Message": "",
                        }])
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

    it('H3StateParDefautIsPresentTest', async () => {
        const h3StateElement = await screen.getByText(`State : NOTHING`)
        expect(h3StateElement).not.toBeInTheDocument()
        expect(fetch).toBeCalledWith("http://localhost:8080/getStatutValidationCV/", expect.anything());
        expect(fetch).toBeCalledTimes(1);
    })

    it('h3MessageParDefautIsPresentTest', async () => {
        const h3MessageElement = screen.getByText(`Message :`)
        expect(h3MessageElement).toBeInTheDocument()
    })
});