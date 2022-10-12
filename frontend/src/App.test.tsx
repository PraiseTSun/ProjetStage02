import {render, screen, fireEvent, act} from "@testing-library/react";
import IUser from "./models/IUser";
import UploaderMonCV from "./pages/UploaderMonCV";
import {arrayBuffer} from "stream/consumers";

const etudiant: IUser = {
    id: 1,
    token: "34245",
    firstName: "Yan",
    lastName: "Zhou",
    userType: "student"
}

describe('test page uploader mon cv', () => {

    it('devrait rendre element input', async () => {
        render(<UploaderMonCV user={etudiant}/>);
        const inputUploaderMonCV = screen.getByTestId("uploaderMonCV")
        expect(inputUploaderMonCV).toBeInTheDocument()
    });

    it('devrait rendre  element header est la ', async () => {
        render(<UploaderMonCV user={etudiant}/>);
        const h4Element = screen.getByRole("heading", {name: /Document CV/i});
        expect(h4Element).toBeInTheDocument();
    });

    it('devrait rendre  element button est la ', async () => {
        render(<UploaderMonCV user={etudiant}/>);
        const buttonElement = screen.getByRole("button", {name: /Envoyer/i});
        expect(buttonElement).toBeInTheDocument();
    });

    it('devrait rendre  element erreur est la ', async () => {
        render(<UploaderMonCV user={etudiant}/>);
        const erreurElemeent = screen.getByText("Choix votre CV")
        expect(erreurElemeent).toBeInTheDocument();
    });
});