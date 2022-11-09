import React from "react";
import {BrowserRouter} from "react-router-dom";
import UserValidation from "../UserValidationPage";
import {act, fireEvent, render, screen} from "@testing-library/react";
import IUser from "../../../models/IUser";

describe("UserValidationPageTests", () => {
    let gestionnaire: IUser
    let student: IUser
    let company: IUser

    beforeEach(async () => {
        gestionnaire = {
            id: "1",
            firstName: "Yan",
            lastName: "Zhou",
            userType: "gestionnaire",
            token: "32432",
            companyName: "",
            cv: "",
            cvToValidate: ""
        }

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

        company = {
            id: "2",
            companyName: "Duff",
            firstName: "Bob",
            lastName: "Marley",
            userType: "company",
            token: "324324332",
            cv: "",
            cvToValidate: ""
        }

        global.fetch = jest.fn((url) => {
                if (url === "http://localhost:8080/unvalidatedStudents") {
                    return Promise.resolve({
                        ok: true,
                        json: () => Promise.resolve([student])
                    });
                } else if (url === "http://localhost:8080/unvalidatedCompanies") {
                    return Promise.resolve({
                        ok: true,
                        json: () => Promise.resolve([company])
                    });
                } else if (url === "http://localhost:8080/createGestionnaire") {
                    return Promise.resolve({
                        created: true
                    });
                } else throw new Error("Bad url call")
            }
        ) as jest.Mock;

        window.alert = jest.fn(() => null) as jest.Mock;

        await act(async () => {
            render(<UserValidation connectedUser={gestionnaire}/>, {wrapper: BrowserRouter});
        });
    })

    it("UserValidationPageStudentList", async () => {
        const etudiantButton = await screen.findByDisplayValue(/Student/i)

        await act(async () => {
            fireEvent.click(etudiantButton)
        })

        expect(fetch).toBeCalledTimes(1)
        const acceptButton = await screen.findByRole("button", {name: /O/i})
        const name = await screen.findByText(/Bartholomew/i)
        expect(acceptButton).toBeInTheDocument()
        expect(name).toBeInTheDocument()
    });

    it("UserValidationPageCompanyTest", async () => {
        const companyButton = await screen.findByTestId("companyInput");
        expect(companyButton).toBeInTheDocument();

        await act(async () => {
            fireEvent.click(companyButton);
        })
        expect(fetch).toBeCalledTimes(2);

        const acceptButton = await screen.findByRole("button", {name: /O/i})
        const name = await screen.findByText(/Duff/i)
        expect(acceptButton).toBeInTheDocument()
        expect(name).toBeInTheDocument()
    })
})