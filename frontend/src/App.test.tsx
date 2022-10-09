import {render, screen, fireEvent} from "@testing-library/react";
import {BrowserRouter} from "react-router-dom";
import IUser from "./models/IUser";
import UploaderMonCV from "./pages/UploaderMonCV";

const etudiant: IUser = {
    id : 1,
    token : "34245",
    firstName: "Yan",
    lastName: "Zhou",
    userType: "gestionnaire"

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
        fireEvent.change(inputPdfFormulaireSoumissionElement , {target:{value:offre.pdf.firstName}})
        fireEvent.click(buttonElement);
    })

}
describe('App', () => {

    it('test page uploader mon cv ', async () => {
        render(<UploaderMonCV user={etudiant}/>);
        const h4Element = screen.getByRole("heading", {name: /Document CV/i});
        const buttonElement = screen.getByRole("button", {name: /Envoyer/i});
        const inputUploaderMonCV = screen.getByTestId("uploaderMonCV")
        expect(h4Element).toBeInTheDocument();
        expect(buttonElement).toBeInTheDocument();

    });

});