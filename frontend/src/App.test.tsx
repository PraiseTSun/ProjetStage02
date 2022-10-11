import { act, fireEvent, render, screen, waitForElementToBeRemoved } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { emptyUser } from "./App";
import StudentCvValidationPage from "./pages/StudentCvValidationPage";
import IUser from "./models/IUser";
import CompanyDashboardPage from "./pages/CompanyDashboardPage";
import ConfirmationPage from "./pages/ConfirmationPage";
import FormulaireSoumissionPage from "./pages/FormulaireSoumissionPage";
import GestionnaireDashboardPage from "./pages/GestionnaireDashboardPage";
import StudentDashboardPage from "./pages/StudentDashboardPage";
import LoginPage from "./pages/LoginPage";
import UserValidation from "./pages/UserValidationPage";

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

const MockCompanyDashboardPage = () => {
    return (
        <BrowserRouter>
            <CompanyDashboardPage  deconnexion={()=>null} user={company}/>
        </BrowserRouter>
    );
}
const MockConfirmationPage = () => {
    return (
        <BrowserRouter>
            <ConfirmationPage />
        </BrowserRouter>
    );
}
const MockFormulaireSoumissionPage = () => {
    return (
        <BrowserRouter>
            <FormulaireSoumissionPage user={gestionnaire}  />
        </BrowserRouter>
    );
}
const MockGestionnaireDashboardPage = () => {
    return (
        <BrowserRouter>
            <GestionnaireDashboardPage user={gestionnaire} deconnexion={()=>null}/>
        </BrowserRouter>
    );
}
const MockStudentDashboardPage = () => {
    return (
        <BrowserRouter>
            <StudentDashboardPage user={student} deconnexion={()=>null}/>
        </BrowserRouter>
    );
}
const MockLoginPage = () => {
    return (
        <BrowserRouter>
            <LoginPage setUser={()=>null}/>
        </BrowserRouter>
    );
}
const MockStudentCvValidationPage = () => {
    return (
        <BrowserRouter>
            <StudentCvValidationPage connectedUser={gestionnaire} deconnexion={()=>null}/>
        </BrowserRouter>
    );
}
const MockUserValidationPage = () => {
    return (
        <BrowserRouter>
            <UserValidation connectedUser={gestionnaire} />
        </BrowserRouter>
    );
}
const gestionnaire: IUser = {
    firstName: "Yan",
    lastName: "Zhou",
    userType: "gestionnaire",
    token: "32432"
}

const company: IUser = {
    firstName: "Bob",
    lastName: "Marley",
    userType: "company",
    token: "324324332"
}
const student: IUser = {
    firstName: "Bartholomew",
    lastName: "Kuma",
    userType: "student",
    token: "324324332"
}

const offres : object = [{
    nomDeCompagnie: "Desjardins",
    department: "Techniques de linformatique",
    position: "position",
    heureParSemaine: 40,
    adresse: "addresse",
    pdf: new FileReader(),
    token: gestionnaire.token
},
    {
        nomDeCompagnie: "Pirate",
        department: "Techniques de linformatique",
        position: "position1",
        heureParSemaine: 40,
        adresse: "addresse1",
        pdf: new FileReader(),
        token: gestionnaire.token
    }]
