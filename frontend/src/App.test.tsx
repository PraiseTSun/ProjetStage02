import {render, screen, fireEvent} from "@testing-library/react";
import IUser from "./models/IUser";
import UploaderMonCV from "./pages/UploaderMonCV";

const etudiant: IUser = {
    id : 1,
    token : "34245",
    firstName: "Yan",
    lastName: "Zhou",
    userType: "student"
}

describe('App', () => {

    it('test page uploader mon cv ', async () => {

            render(<UploaderMonCV user={etudiant}/>);
            const inputUploaderMonCV = screen.getByTestId("uploaderMonCV")
            const h4Element = screen.getByRole("heading", {name: /Document CV/i});
            const buttonElement = screen.getByRole("button", {name: /Envoyer/i});
            fireEvent.change(inputUploaderMonCV , {target: new File([], "test.pdf", {type: "application/pdf"})})
            expect(h4Element).toBeInTheDocument();
            expect(buttonElement).toBeInTheDocument();
            fireEvent.click(buttonElement);
            expect(screen.getByText("Choix votre CV")).toBeInTheDocument()
    });
});