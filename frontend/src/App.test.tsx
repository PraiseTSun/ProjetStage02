import {render, screen, fireEvent} from "@testing-library/react";
import {BrowserRouter} from "react-router-dom";
import IUser from "./models/IUser";
import StudentCvValidationPage from "./pages/StudentCvValidationPage";
import ValiderNouvelleOffreStagePage from "./pages/ValiderNouvelleOffreStagePage";
import FormulaireSoumissionPage from "./pages/FormulaireSoumissionPage";
import { emptyUser } from "./App";

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

const offres : object = [{
    nomDeCompagnie: "Desjardins",
    department: "Techniques de linformatique",
    position: "position",
    heureParSemaine: 40,
    adresse: "addresse",
    pdf: new File([""], "file1.pdf"),
    token: gestionnaire.token
},
    {
        nomDeCompagnie: "Pirate",
        department: "Techniques de linformatique",
        position: "position1",
        heureParSemaine: 40,
        adresse: "addresse1",
        pdf: new File([""], "file2.pdf"),
        token: gestionnaire.token
    }]
const mockdeconnexion  = jest.fn()

const ValiderNouvelleOffreStage = ({connectedUser, deconnexion}:{connectedUser : IUser, deconnexion : Function}) => {
    return (
        <BrowserRouter>
            <ValiderNouvelleOffreStagePage connectedUser={connectedUser} deconnexion={deconnexion}/>
        </BrowserRouter>
    )
}

const FormulaireSoumission = ({user}:{user : IUser}) => {
    return (
        <BrowserRouter>
            <FormulaireSoumissionPage user={user} />
        </BrowserRouter>
    )
}
const addOffres = (offres : any) => {
    const controlNomCompanyElement= screen.getByTestId("nomCompanyFormulaireSoumission")
    const controlDepartmentElement = screen.getByTestId("departmentFormulaireSoumission")
    const controlPosteElement = screen.getByTestId("posteFormulaireSoumission")
    const controlHoursFormulaireSoumissionElement = screen.getByTestId("hoursFormulaireSoumission")
    const controlAddressFormulaireSoumissionElement = screen.getByTestId("addressFormulaireSoumission")
    const inputPdfFormulaireSoumissionElement = screen.getByTestId("pdfFormulaireSoumission")
    const buttonElement = screen.getByTestId("envoyerFormulaireSoumission")

    offres.forEach((offre : any)=>{
        fireEvent.change(controlNomCompanyElement , {target:{value:offre.nomDeCompagnie}})
        fireEvent.change(controlDepartmentElement , {target:{value:offre.department}})
        fireEvent.change(controlPosteElement , {target:{value:offre.position}})
        fireEvent.change(controlHoursFormulaireSoumissionElement , {target:{value:offre.heureParSemaine}})
        fireEvent.change(controlAddressFormulaireSoumissionElement , {target:{value:offre.adresse}})
        fireEvent.change(inputPdfFormulaireSoumissionElement , {target:{value:offre.pdf.name}})
        fireEvent.click(buttonElement);
    })

}
describe('App', () => {


    it('test il y a pas de offre qui a besoin de valider ', async () => {
        render(<ValiderNouvelleOffreStage connectedUser={gestionnaire} deconnexion={mockdeconnexion}/>);
        const h1Element = screen.getByRole("heading", {name: /Validation de offre/i});
        const LinkElement = screen.getByRole("link", {name: /Home/i});
        const trElement = screen.getAllByTestId("offre-container");
        const tableElement = screen.getByTestId("tableValiderNouvelleOffreStage");
        expect(h1Element).toBeInTheDocument();
        expect(LinkElement).toBeInTheDocument();
        expect(tableElement).toBeInTheDocument();
        expect(trElement.length).toBe(1);
    });

    it('test valider une nouvelle offre ', async () => {
        render(<FormulaireSoumission user={company}/>);
        render(<ValiderNouvelleOffreStage connectedUser={gestionnaire} deconnexion={mockdeconnexion}/>);
        addOffres(offres)

        const buttonElement = screen.getByRole("button", {name: /O/i})
        const trElement = screen.getAllByTestId("offre-container")
        expect(buttonElement).toBeInTheDocument();
        fireEvent.click(buttonElement)
        expect(trElement.length).toBe(1)
    });

    it('test supprimer une nouvelle offre ', async () => {
        render(<FormulaireSoumission user={company}/>);
        render(<ValiderNouvelleOffreStage connectedUser={gestionnaire} deconnexion={mockdeconnexion}/>);
        addOffres(offres)

        const buttonElement = screen.getByRole("button", {name: /x/i})
        const trElements = screen.getAllByTestId("offre-container")

        expect(buttonElement).toBeInTheDocument();
        fireEvent.click(buttonElement)
        expect(trElements.length).toBe(1)
    });
});
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