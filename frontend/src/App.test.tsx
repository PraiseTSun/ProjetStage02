import {render, screen, fireEvent} from "@testing-library/react";
import {BrowserRouter} from "react-router-dom";
import IUser from "./models/IUser";
import {LOCAL_STORAGE_KEY} from "./App";
import ValiderNouvelleOffreStagePage from "./pages/ValiderNouvelleOffreStagePage";
import FormulaireSoumissionPage from "./pages/FormulaireSoumissionPage";

const pdf : any = [37, 80, 68, 70, 45, 49, 46, 55, 10, 10, 49, 32, 48, 32, 111, 98, 106, 32, 32, 37, 32, 101, 110, 116,
    114, 121, 32, 112, 111, 105, 110, 116, 10, 60, 60, 10, 32, 32, 47, 84, 121, 112, 101, 32, 47, 67,
    97, 116, 97, 108, 111, 103, 10, 32, 32, 47, 80, 97, 103, 101, 115, 32, 50, 32, 48, 32, 82, 10, 62,
    62, 10, 101, 110, 100, 111, 98, 106, 10, 10, 50, 32, 48, 32, 111, 98, 106, 10, 60, 60, 10, 32, 32,
    47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 115, 10, 32, 32, 47, 77, 101, 100, 105, 97, 66,
    111, 120, 32, 91, 32, 48, 32, 48, 32, 50, 48, 48, 32, 50, 48, 48, 32, 93, 10, 32, 32, 47, 67, 111,
    117, 110, 116, 32, 49, 10, 32, 32, 47, 75, 105, 100, 115, 32, 91, 32, 51, 32, 48, 32, 82, 32, 93,
    10, 62, 62, 10, 101, 110, 100, 111, 98, 106, 10, 10, 51, 32, 48, 32, 111, 98, 106, 10, 60, 60, 10,
    32, 32, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 10, 32, 32, 47, 80, 97, 114, 101, 110, 116,
    32, 50, 32, 48, 32, 82, 10, 32, 32, 47, 82, 101, 115, 111, 117, 114, 99, 101, 115, 32, 60, 60, 10,
    32, 32, 32, 32, 47, 70, 111, 110, 116, 32, 60, 60, 10, 32, 32, 32, 32, 32, 32, 47, 70, 49, 32, 52,
    32, 48, 32, 82, 32, 10, 32, 32, 32, 32, 62, 62, 10, 32, 32, 62, 62, 10, 32, 32, 47, 67, 111, 110,
    116, 101, 110, 116, 115, 32, 53, 32, 48, 32, 82, 10, 62, 62, 10, 101, 110, 100, 111, 98, 106, 10,
    10, 52, 32, 48, 32, 111, 98, 106, 10, 60, 60, 10, 32, 32, 47, 84, 121, 112, 101, 32, 47, 70, 111,
    110, 116, 10, 32, 32, 47, 83, 117, 98, 116, 121, 112, 101, 32, 47, 84, 121, 112, 101, 49, 10, 32,
    32, 47, 66, 97, 115, 101, 70, 111, 110, 116, 32, 47, 84, 105, 109, 101, 115, 45, 82, 111, 109, 97,
    110, 10, 62, 62, 10, 101, 110, 100, 111, 98, 106, 10, 10, 53, 32, 48, 32, 111, 98, 106, 32, 32, 37,
    32, 112, 97, 103, 101, 32, 99, 111, 110, 116, 101, 110, 116, 10, 60, 60, 10, 32, 32, 47, 76, 101,
    110, 103, 116, 104, 32, 52, 52, 10, 62, 62, 10, 115, 116, 114, 101, 97, 109, 10, 66, 84, 10, 55, 48,
    32, 53, 48, 32, 84, 68, 10, 47, 70, 49, 32, 49, 50, 32, 84, 102, 10, 40, 72, 101, 108, 108, 111, 44,
    32, 119, 111, 114, 108, 100, 33, 41, 32, 84, 106, 10, 69, 84, 10, 101, 110, 100, 115, 116, 114, 101,
    97, 109, 10, 101, 110, 100, 111, 98, 106, 10, 10, 120, 114, 101, 102, 10, 48, 32, 54, 10, 48, 48,
    48, 48, 48, 48, 48, 48, 48, 48, 32, 54, 53, 53, 51, 53, 32, 102, 32, 10, 48, 48, 48, 48, 48, 48, 48,
    48, 49, 48, 32, 48, 48, 48, 48, 48, 32, 110, 32, 10, 48, 48, 48, 48, 48, 48, 48, 48, 55, 57, 32, 48,
    48, 48, 48, 48, 32, 110, 32, 10, 48, 48, 48, 48, 48, 48, 48, 49, 55, 51, 32, 48, 48, 48, 48, 48, 32,
    110, 32, 10, 48, 48, 48, 48, 48, 48, 48, 51, 48, 49, 32, 48, 48, 48, 48, 48, 32, 110, 32, 10, 48,
    48, 48, 48, 48, 48, 48, 51, 56, 48, 32, 48, 48, 48, 48, 48, 32, 110, 32, 10, 116, 114, 97, 105, 108,
    101, 114, 10, 60, 60, 10, 32, 32, 47, 83, 105, 122, 101, 32, 54, 10, 32, 32, 47, 82, 111, 111, 116,
    32, 49, 32, 48, 32, 82, 10, 62, 62, 10, 115, 116, 97, 114, 116, 120, 114, 101, 102, 10, 52, 57, 50,
    10, 37, 37, 69, 79, 70]

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
    pdf: pdf,
    token: gestionnaire.token
},
    {
        nomDeCompagnie: "Pirate",
        department: "Techniques de linformatique",
        position: "position1",
        heureParSemaine: 40,
        adresse: "addresse1",
        pdf: pdf,
        token: gestionnaire.token
    }]
const deconnexion = () => {
    localStorage.removeItem(LOCAL_STORAGE_KEY)
    window.location.href = "/"
}

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
        fireEvent.change(inputPdfFormulaireSoumissionElement , {target:{value:"file.pdf"}})
        fireEvent.click(buttonElement);
    })

}
describe('App', () => {

    it('test valider une nouvelle offre ', async () => {
        render(<FormulaireSoumission user={company} />);
        render(<ValiderNouvelleOffreStage connectedUser={gestionnaire} deconnexion={deconnexion}/>);
        addOffres(offres)
        const element = screen.getByTestId("offre-container")
        const buttonElement = screen.getByRole("button", { name: /O/i})
        const trElement = screen.getAllByTestId("offre-container")
        expect(buttonElement).toBeInTheDocument();
        fireEvent.click(buttonElement)
        expect(trElement.length).toBe(0)
    });

    it('test supprimer une nouvelle offre ', async () => {
        render(<ValiderNouvelleOffreStage connectedUser={gestionnaire} deconnexion={deconnexion}/>);
        render(<ValiderNouvelleOffreStage connectedUser={gestionnaire} deconnexion={deconnexion}/>);
        addOffres(offres)
        const buttonElement = screen.getByRole("button", { name: /X/i})
        const trElement = screen.getAllByTestId("offre-container")
        expect(buttonElement).toBeInTheDocument();
        fireEvent.click(buttonElement)
        expect(trElement.length).toBe(0)
    });
});