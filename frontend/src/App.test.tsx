import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { emptyUser } from "./App";
import StudentCvValidationPage from "./pages/StudentCvValidationPage";

describe("StudentCvValidationPage test", () => {

    global.fetch = jest.fn(() =>
        Promise.resolve({
            ok: true,
            json: () => Promise.resolve([{
                "id": 1,
                "firstName": "Bart",
                "lastName": "Simpson",
                "department": "Informatique"
            }]),
        }),
    ) as jest.Mock;

    window.alert = jest.fn(() => null) as jest.Mock;

    const MockPage = () => {
        return (
            <BrowserRouter>
                <StudentCvValidationPage connectedUser={emptyUser} deconnexion={() => null} />
            </BrowserRouter>
        );
    }

    it("Successful fetch", async () => {
        render(<MockPage />);
        await new Promise((r) => setTimeout(r, 1000));

        const studentName = screen.getByText("Bart");
        expect(studentName).toBeInTheDocument();
        expect(fetch).toBeCalledTimes(1);
    });
});