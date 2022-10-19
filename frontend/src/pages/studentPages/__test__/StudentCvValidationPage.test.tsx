import {BrowserRouter} from "react-router-dom";
import StudentCvValidationPage from "../StudentCvValidationPage";
import {emptyUser} from "../../../App";
import {act, fireEvent, render, screen} from "@testing-library/react";
import * as React from "react";

describe("StudentCvValidationPageTests", () => {
    beforeEach(async () => {
        global.fetch = jest.fn((url) => {
                if (url === "http://localhost:8080/unvalidatedCvStudents") {
                    return Promise.resolve({
                        ok: true,
                        json: () => Promise.resolve([{
                            "id": 1,
                            "firstName": "Bart",
                            "lastName": "Simpson",
                            "department": "Informatique"
                        }])
                    });
                } else if (url === "http://localhost:8080/validateCv/1") {
                    return Promise.resolve({
                        ok: true
                    });
                } else if (url === "http://localhost:8080/refuseCv/1") {
                    return Promise.resolve({
                        ok: true
                    });
                } else if (url === "http://localhost:8080/studentCv/1") {
                    return Promise.resolve({
                        ok: true,
                        json: () => Promise.resolve({
                            "pdf": "[10]"
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

        await act(async () => {
            render(<StudentCvValidationPage connectedUser={emptyUser} deconnexion={() => null}/>,
                {wrapper: BrowserRouter});
        });
    })

    it("FetchUnvalidatedCvStudentsTest", async () => {
        expect(await screen.findByText(/Bart/i)).toBeInTheDocument();
        expect(await screen.findByText(/Simpson/i)).toBeInTheDocument();
        expect(await screen.findByText(/Informatique/i)).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/unvalidatedCvStudents", expect.anything());
        expect(fetch).toBeCalledTimes(1);
    });

    it("ValidateCvTest", async () => {
        const validateButton = await screen.findByRole("button", {name: /O/i});
        const studentName = await screen.findByText(/Bart/i);

        await act(async () => {
            fireEvent.click(validateButton);
        });

        expect(studentName).not.toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/validateCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("RefuseCvTest", async () => {
        const validateButton = await screen.findByRole("button", {name: /X/i});
        const studentName = await screen.findByText(/Bart/i);

        await act(async () => {
            fireEvent.click(validateButton);
        });

        expect(studentName).not.toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/refuseCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("GetPdfTest", async () => {
        const cvButton = await screen.findByRole("button", {name: /CV/i});

        await act(async () => {
            fireEvent.click(cvButton);
        });

        expect(await screen.findByRole("button", {name: /Fermer/i})).toBeInTheDocument();
        expect(fetch).toBeCalledWith("http://localhost:8080/studentCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    afterEach(() => {
        expect(fetch).toBeCalledWith("http://localhost:8080/unvalidatedCvStudents", expect.anything());
    });
});