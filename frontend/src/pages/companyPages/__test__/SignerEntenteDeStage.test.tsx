import IUser from "../../../models/IUser";
import {act, fireEvent, render, screen} from "@testing-library/react";
import {BrowserRouter} from "react-router-dom";
import React from "react";
import SignerEntenteDeStage from "../SignerEntenteDeStage";

describe("SignerEntenteDeStagePageTests", () => {
    let company: IUser

    beforeEach(async () => {
        company = {
            id: "1",
            companyName: "Duff",
            cv: "",
            firstName: "Bob",
            lastName: "Marley",
            userType: "company",
            token: "324324332"
        }

        global.fetch = jest.fn((url) => {
            if (url === "http://localhost:8080/companyContracts/1") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve([{
                        "contratId": 1,
                        "description": "Contrat va siger par moi"
                    },
                        {
                            "contratId": 2,
                            "description": "Contrat va siger par moi"
                        },
                    ])
                });
            } else if (url === "http://localhost:8080/companySignatureContract") {
                return Promise.resolve({
                    ok: true
                });
            } else throw new Error("Bad url call");
        }) as jest.Mock;

        const intersectionObserverMock = () => ({
            observe: () => null,
            unobserve: () => null,
            disconnect: () => null
        })

        window.IntersectionObserver = jest.fn().mockImplementation(intersectionObserverMock);

        await act(async () => {
            render(<SignerEntenteDeStage user={company}/>, {wrapper: BrowserRouter});
        });
    });

    it("linkTest", async ()=>{
        const linkElement = await screen.findByRole("link", {name: /Home/i})
        expect(linkElement).toBeInTheDocument()
    })

    it("lesChampsThTest", async () => {
        const array = [
            /First Name/i,
            /Last Name/i,
            /Description/i,
            /Ententes/i
        ]

        for (const item of array) {
            const element = await screen.findAllByText(item)
            expect(element.length).not.toBe(0)
        }
    })

    it("tableTest", async () => {
        const array = [
            /Bob/i,
            /Marley/i,
            /Contrat va siger par moi/i,
            /Signer/i
        ]
        expect(fetch).toBeCalledWith("http://localhost:8080/companyContracts/1", expect.anything());
        expect(fetch).toBeCalledTimes(1);

        for (const item of array) {
            const element = await screen.findAllByText(item)
            expect(element.length).toBe(2)
        }
    })
})