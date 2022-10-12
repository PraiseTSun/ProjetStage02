import {render, screen, fireEvent, waitForElementToBeRemoved, act} from "@testing-library/react";
import {BrowserRouter} from "react-router-dom";
import IUser from "./models/IUser";
import StudentCvValidationPage from "./pages/StudentCvValidationPage";
import ValiderNouvelleOffreStagePage from "./pages/ValiderNouvelleOffreStagePage";
import FormulaireSoumissionPage from "./pages/FormulaireSoumissionPage";
import {emptyUser} from "./App";
import {wait} from "@testing-library/user-event/dist/utils";

const gestionnaire: IUser = {
    firstName: "Yan",
    lastName: "Zhou",
    userType: "gestionnaire",
    token: "32432"
}
const mockdeconnexion = jest.fn()

const ValiderNouvelleOffreStage = ({connectedUser, deconnexion}: { connectedUser: IUser, deconnexion: Function }) => {
    return (
        <BrowserRouter>
            <ValiderNouvelleOffreStagePage connectedUser={connectedUser} deconnexion={deconnexion}/>
        </BrowserRouter>
    )
}

describe('App', () => {

    it('test il y a le champs header ', async () => {
        render(<ValiderNouvelleOffreStage connectedUser={gestionnaire} deconnexion={mockdeconnexion}/>);
        const h1Element = screen.getByRole("heading", {name: /Validation de offre/i});
        expect(h1Element).toBeInTheDocument();
    });

    it('test il y a le champs Link ', async () => {
        render(<ValiderNouvelleOffreStage connectedUser={gestionnaire} deconnexion={mockdeconnexion}/>);

        const LinkElement = screen.getByRole("link", {name: /Home/i});
        expect(LinkElement).toBeInTheDocument();

    });

    it('test il y a le champs tr ', async () => {
        render(<ValiderNouvelleOffreStage connectedUser={gestionnaire} deconnexion={mockdeconnexion}/>);

        const trElement = screen.getAllByTestId("offre-container");
        expect(trElement.length).toBe(1);
    });

    it('test il y a le champs table ', async () => {
        render(<ValiderNouvelleOffreStage connectedUser={gestionnaire} deconnexion={mockdeconnexion}/>);

        const tableElement = screen.getByTestId("tableValiderNouvelleOffreStage");
        expect(tableElement).toBeInTheDocument();
    });
});