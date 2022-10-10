import { act, fireEvent, render, screen, waitForElementToBeRemoved } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { emptyUser } from "./App";
import StudentCvValidationPage from "./pages/StudentCvValidationPage";

describe("StudentCvValidationPage test", () => {

    const MockPage = () => {
        return (
            <BrowserRouter>
                <StudentCvValidationPage connectedUser={emptyUser} deconnexion={() => null} />
            </BrowserRouter>
        );
    }

    beforeEach(() => {
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
            }
            else if (url === "http://localhost:8080/validateCv/1") {
                console.log("validate")
                return Promise.resolve({
                    ok: true
                });
            }
            else if (url === "http://localhost:8080/refuseCv/1") {
                return Promise.resolve({
                    ok: true
                });
            }
            else if (url === "http://localhost:8080/studentCv/") {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve([{
                        "prf": []
                    }])
                });
            }
            else throw new Error("Bad url call")
        }
        ) as jest.Mock;

        window.alert = jest.fn(() => null) as jest.Mock;
    })

    it("fetchUnvalidatedCvStudentsTest", async () => {
        act(() => {
            render(<MockPage />)
        });

        expect(await screen.findByText(/Bart/i)).toBeInTheDocument();
        expect(await screen.findByText(/Simpson/i)).toBeInTheDocument();
        expect(await screen.findByText(/Informatique/i)).toBeInTheDocument();
        expect(fetch).toBeCalledTimes(1);
    });

    it("validateCvTest", async () => {
        act(() => {
            render(<MockPage />)
        });
        const validateButton = await screen.findByRole("button", { name: /O/i });

        act(() => {
            fireEvent.click(validateButton);
        });

        await waitForElementToBeRemoved(() => screen.queryByText(/Bart/i));
        expect(fetch).toBeCalledWith("http://localhost:8080/validateCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("refuseCvTest", async () => {
        act(() => {
            render(<MockPage />)
        });
        const validateButton = await screen.findByRole("button", { name: /X/i });

        act(() => {
            fireEvent.click(validateButton);
        });

        await waitForElementToBeRemoved(() => screen.queryByText(/Bart/i));
        expect(fetch).toBeCalledWith("http://localhost:8080/refuseCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    it("getPDFTest", async () => {
        act(() => {
            render(<MockPage />)
        });
        const cvButton = await screen.findByRole("button", { name: /CV/i });

        act(() => {
            fireEvent.click(cvButton);
        });

        expect(fetch).toBeCalledWith("http://localhost:8080/studentCv/1", expect.anything());
        expect(fetch).toBeCalledTimes(2);
    });

    afterEach(() => {
        expect(fetch).toBeCalledWith("http://localhost:8080/unvalidatedCvStudents", expect.anything());
    });
});